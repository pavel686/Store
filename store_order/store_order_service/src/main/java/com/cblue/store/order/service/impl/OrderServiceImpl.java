package com.cblue.store.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cblue.store.common.jedis.JedisClient;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.mapper.TbOrderItemMapper;
import com.cblue.store.mapper.TbOrderMapper;
import com.cblue.store.mapper.TbOrderShippingMapper;
import com.cblue.store.order.pojo.OrderInfo;
import com.cblue.store.order.service.OrderService;
import com.cblue.store.pojo.TbOrderItem;
import com.cblue.store.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService {

	//定义一个订单key值，将来保存redis的计数器
	private String ORDER_ID_GEN_KEY="ORDER_ID_GEN_KEY";
	//订单id的累加的初始值
	private String  ORDRE_ID_START="238393";
	
	
	//定义一个订单key值，将来保存redis的计数器
	private String ORDER_ITEM_ID_GEN_KEY="ORDER_ITEM_ID_GEN_KEY";
	//订单id的累加的初始值
	private String  ORDRE_ITEM__ID_START="1";
	
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;
	
	private String ADD_CART = "ADD_CART:";
	
	@Override
	public ResponseResult createOrder(OrderInfo orderInfo) {
		//初始化订单id
		if(!jedisClient.exists(ORDER_ID_GEN_KEY)){
			jedisClient.set(ORDER_ID_GEN_KEY, ORDRE_ID_START);//ORDER_ID_GEN_KEY   238393
		}
		//生成新的订单值
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();//238394
		orderInfo.setOrderId(orderId);
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//把订单信息插入到对应表中
		tbOrderMapper.insert(orderInfo);
		
		
		if(!jedisClient.exists(ORDER_ITEM_ID_GEN_KEY)){
			jedisClient.set(ORDER_ITEM_ID_GEN_KEY, ORDRE_ITEM__ID_START);//1
		}
		//获得订单明细表的对象
	   List<TbOrderItem> tbOrderItemList = orderInfo.getOrderItems();
	   for(TbOrderItem tbOrderItem:tbOrderItemList){
		   String orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
		   tbOrderItem.setId(orderItemId);
		   tbOrderItem.setOrderId(orderId);
		   //把订单详情保存
		   tbOrderItemMapper.insert(tbOrderItem);
		   
	   }
	   
	   //保存物流信息
	   TbOrderShipping tbOrderShipping = orderInfo.getOrderShipping();
	   tbOrderShipping.setOrderId(orderId);
	   tbOrderShipping.setCreated(new Date());
	   tbOrderShipping.setUpdated(new Date());
	   tbOrderShippingMapper.insert(tbOrderShipping);
		
	   //把生成的订单号返回;
	   return ResponseResult.ok(orderId);
	}

	@Override
	public void clearCart(Long userId) {
		// TODO Auto-generated method stub
		jedisClient.del(ADD_CART+userId);
	}

}
