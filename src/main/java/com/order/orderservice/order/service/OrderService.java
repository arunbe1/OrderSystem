package com.order.orderservice.order.service;

import com.order.orderservice.models.Order;
import com.order.orderservice.models.Orders;

public interface OrderService {
	
	public Orders addOrder(Orders order) throws Exception;

	public Orders getOrderDetails(Integer orderId) throws Exception;
	
	public Iterable<Orders> getAllOrders() throws Exception;
}
