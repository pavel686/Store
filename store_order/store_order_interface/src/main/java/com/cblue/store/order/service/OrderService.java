package com.cblue.store.order.service;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.order.pojo.OrderInfo;

public interface OrderService {
	//创建订单
	ResponseResult createOrder(OrderInfo orderInfo);

	void clearCart(Long userId);

}
