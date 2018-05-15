package com.cblue.store.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cblue.store.cart.service.CartService;
import com.cblue.store.common.jedis.JedisClient;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.util.JsonUtils;
import com.cblue.store.mapper.TbItemMapper;
import com.cblue.store.pojo.TbItem;
import com.cblue.store.pojo.TbUser;
import com.cblue.store.service.ItemService;

@Service
public class CartServiceImpl implements CartService {

	/**
	 * 把商品信息放到redis中
	 * 思路：首先需要去redis中查询该用户下是否有这个商品
	 * 如果有这个商品，我们只是修改数量
	 * 如果没有这个商品，添加一条整个记录（userid，itemid，商品的json格式数据）
	 */
	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbItemMapper tbItemMapper;
	
	private String ADD_CART = "ADD_CART:";
	
	/**
	 * jedisClient.hset(ADD_CART+userId,itemId.toString(),JsonUtils.objectToJson(tbItem));
	 * 
	 * key   （filed   value）
	 * userid  itemId  商品json格式
	 * 1        10000           手机json格式
	 * 1       20000             冰箱json格式
	 * 
	 * 
	 * 
	 */
	@Override
	public ResponseResult addCart(Long userId, Long itemId, int num) {
		//首先需要去redis中查询该用户下是否有这个商品
		boolean exists = jedisClient.hexists(ADD_CART+userId,itemId.toString());
		//如果商品存在，修改商品数量
		if(exists){
			String jsonStr = jedisClient.hget(ADD_CART+userId,itemId.toString());
			TbItem tbItem = JsonUtils.jsonToPojo(jsonStr, TbItem.class);
			tbItem.setNum(num);
			jedisClient.hset(ADD_CART+userId,itemId.toString(),JsonUtils.objectToJson(tbItem));
			return ResponseResult.ok();
		}
		//如果商品不存在，添加一条新的商品
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		tbItem.setNum(num);
		jedisClient.hset(ADD_CART+userId,itemId.toString(),JsonUtils.objectToJson(tbItem));
		return ResponseResult.ok();

	}

	/**
	 * 把cookie中购物车信息放到redis中
	 * @param id 用户id
	 */
	@Override
	public void mergeCart(Long userid, List<TbItem> listTbList) {
	   for(TbItem tbItem:listTbList){
		   addCart(userid, tbItem.getId(), tbItem.getNum());
	   }
	}

	/**
	 * 根据用户的id查询redis，获得该用户的购物车信息（购物车会有多个商品）
	 * userId    Itemid     商品json格式数据
	 *  1         10000              手机json格式
	 *  1         20000             冰箱json格式
	 */
	@Override
	public List<TbItem> getCartList(Long userId) {
		//从redis中获得购物车中所有商品的json字符串
		List<String> jsonStrs = jedisClient.hvals(ADD_CART+userId);
		List<TbItem> tbItems = new ArrayList<TbItem>();
		for(String jsonStr:jsonStrs){
			System.out.println(jsonStr);
			TbItem tbItem = JsonUtils.jsonToPojo(jsonStr, TbItem.class);
			tbItems.add(tbItem);
		}
		return tbItems;
	}

	/**
	 * 思路：
	 * 1.先从redis中查询该商品的信息，json格式
	 * 2.json格式转化对象，修改商品数量
	 * 3.把商品对象重新保存到redis中
	 */
	@Override
	public ResponseResult updateCartNumToRedis(Long userId, Long itemId, int num) {
		// TODO Auto-generated method stub
		String jsonStr = jedisClient.hget(ADD_CART+userId.toString(), itemId.toString());
		TbItem tbItem = JsonUtils.jsonToPojo(jsonStr, TbItem.class);
		tbItem.setNum(num);
		jedisClient.hset(ADD_CART+userId,itemId.toString(),JsonUtils.objectToJson(tbItem));
		return ResponseResult.ok();
	}

	@Override
	public void deleteCartItemFromRedis(Long userId, Long itemId) {
		// TODO Auto-generated method stub
		jedisClient.hdel(ADD_CART+userId.toString(), itemId.toString());
		
	}

}
