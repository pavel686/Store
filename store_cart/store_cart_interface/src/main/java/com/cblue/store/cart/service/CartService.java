package com.cblue.store.cart.service;

import java.util.List;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.pojo.TbItem;

public interface CartService {
	
	//添加购物信息到redis
	ResponseResult addCart(Long userId,Long itemId,int num);
    //合并cookie和redis中的数据
	void mergeCart(Long userId, List<TbItem> listTbList);
	//根据用户id，查询redis，获得某个用户的所有购物车信息
	List<TbItem> getCartList(Long userId);
	//修改redis中商品数量
	ResponseResult updateCartNumToRedis(Long userId, Long itemId, int num);
	//从redis中删除购物车内容
	void deleteCartItemFromRedis(Long userId, Long itemId);

}
