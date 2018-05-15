package com.cblue.store.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cblue.store.cart.service.CartService;
import com.cblue.store.common.jedis.JedisClient;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.util.CookieUtils;
import com.cblue.store.common.util.JsonUtils;
import com.cblue.store.pojo.TbItem;
import com.cblue.store.pojo.TbUser;
import com.cblue.store.service.ItemService;

@Controller
public class CartController {

	/**
	 * 在未登录状态下， 1》首先从cookie获得购物车信息 2》如果cookie中的购物车信息中包含这个商品，我们只是数量上添加
	 * 3》如果cookie中的购物车信息中不包含这个商品，我们把是这个商品添加到购物车中 4》把所有购物车的信息，重新保存到cookie中
	 */
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;

	@Value("${CART_PREFIX}")
	private String CART_PREFIX; // cart

	@RequestMapping("/cart/add/{ItemId}")
	public String addCart(@PathVariable Long ItemId,
			@RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 如果request中包含user对象，把数据添加redis中
		TbUser tbUser = (TbUser) request.getAttribute("user");
		if (tbUser != null) {
			// 这个数据写入到redis
			cartService.addCart(tbUser.getId(), ItemId, num);
			return "cartSuccess";
		}
		// 1》首先从cookie获得购物车信息
		List<TbItem> listTbItem = getCartListFromCookie(request);
		// 2》如果cookie中的购物车信息中包含这个商品，我们只是数量上添加
		boolean flag = false;
		for (TbItem tbItem : listTbItem) {
			// 购物车中包含要添加的商品
			System.out.println(ItemId.longValue() == tbItem.getId());
			System.out.println(ItemId.longValue());
			System.out.println(tbItem.getId());
			if (ItemId.longValue() == tbItem.getId()) {
				flag = true;
				// 数量类加
				tbItem.setNum(tbItem.getNum() + num);
				break;
			}
		}
		// 3》如果cookie中的购物车信息中不包含这个商品，我们把是这个商品添加到购物车中
		if (!flag) {
			TbItem tbItem = itemService.getItemById(ItemId);
			// 设置数量
			tbItem.setNum(num);
			listTbItem.add(tbItem);
		}
		// 4》把所有购物车的信息，重新保存到cookie中
		CookieUtils.setCookie(request, response, CART_PREFIX,
				JsonUtils.objectToJson(listTbItem), true);
		return "cartSuccess";
	}

	// 从cookie中获取购物车的信息
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		String jsonStr = CookieUtils.getCookieValue(request, CART_PREFIX, true);
		// 如果jsonStr是一个空，说明之前购物车中没有数据，我们就创建一个空的集合，返回出去
		if (StringUtils.isBlank(jsonStr)) {
			return new ArrayList<TbItem>();
		}
		List<TbItem> ibItems = JsonUtils.jsonToList(jsonStr, TbItem.class);
		return ibItems;

	}

	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request,
			HttpServletResponse response) {
		// 查询数据
		List<TbItem> listTbList = getCartListFromCookie(request);
		// 判断用户是否登录
		TbUser tbUser = (TbUser) request.getAttribute("user");
		// 说明之前登录了
		if (tbUser != null) {
			// 需要从reids中查询数据，并和cookie中的数据进行合并
			cartService.mergeCart(tbUser.getId(), listTbList);
			// 把本地的cookie中数据删除
			CookieUtils.deleteCookie(request, response, CART_PREFIX);
			listTbList = cartService.getCartList(tbUser.getId());
		}

		request.setAttribute("cartList", listTbList);
		return "cart";
	}

	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public ResponseResult updateCartNum(@PathVariable Long itemId,@PathVariable int num,HttpServletRequest request,HttpServletResponse response){
		//判断用户是否登录
		TbUser tbUser =(TbUser) request.getAttribute("user");
		//说明之前登录了
		if(tbUser!=null){
			 //操作redis
			return cartService.updateCartNumToRedis(tbUser.getId(),itemId,num);
		 }
		//获得提交过来的数据，更新cookie商品的数量
		  //1 查询cookie中，查询对象
		 List<TbItem>  listTbList =  getCartListFromCookie(request);
		 for(TbItem tbItem:listTbList){
			 if(itemId.longValue()==tbItem.getId()){
				 //修改数量
				 tbItem.setNum(num);
				 break;
			 }
		 }
		 CookieUtils.setCookie(request, response, CART_PREFIX, JsonUtils.objectToJson(listTbList), true);
		 return ResponseResult.ok();
	}

	// 删除购物车中的某个商品
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,
			HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否登录
		TbUser tbUser =(TbUser) request.getAttribute("user");
		//说明之前登录了
		if(tbUser!=null){
			//操作redis
			cartService.deleteCartItemFromRedis(tbUser.getId(),itemId);
			return "redirect:/cart/cart.html";	
		}
				
		// 首先从cookie获得所有商品
		List<TbItem> listTbList = getCartListFromCookie(request);
		// 从所有商品找到要删除的商品，进行删除
		for (TbItem tbItem : listTbList) {
			if (itemId.longValue() == tbItem.getId()) {
				// 删除商品
				listTbList.remove(tbItem);
				break;
			}
		}
		// 把新的数据保存到cookie
		CookieUtils.setCookie(request, response, CART_PREFIX,
				JsonUtils.objectToJson(listTbList), true);
		// 还需要跳回购物车页面
		return "redirect:/cart/cart.html";

	}

}
