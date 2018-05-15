package com.cblue.store.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cblue.store.cart.service.CartService;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.order.pojo.OrderInfo;
import com.cblue.store.order.service.OrderService;
import com.cblue.store.pojo.TbItem;
import com.cblue.store.pojo.TbUser;


@Controller
public class OrderController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("order/order-cart")
	public String showOrder(HttpServletRequest request){
		//获取收货信息列表
		//获取支付信息
		//从redis中获取商品数据
		TbUser tbUser = (TbUser) request.getAttribute("user");
		//根据用户的id获得商品信息
		List<TbItem> tbItems = cartService.getCartList(tbUser.getId());
	    request.setAttribute("cartList", tbItems);
		return "order-cart";
	}
	

	//这个是订单提交
	@RequestMapping(value="order/create",method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		//获取用户信息
		TbUser tbUser = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(tbUser.getId());
		orderInfo.setBuyerNick(tbUser.getUsername());
		//创建订单
		ResponseResult responseResult = orderService.createOrder(orderInfo);
		//清空购物车
		if(responseResult.getStatus()==200){
			orderService.clearCart(tbUser.getId());
		}
		//把订单id，还有需要支付的金额放到request
		request.setAttribute("orderId",responseResult.getData());
		request.setAttribute("payment",orderInfo.getPayment());
		System.out.println(responseResult.getData()+"----"+orderInfo.getPayment());
		return "success";
	}
	
}
