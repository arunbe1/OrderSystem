package com.order.orderservice.order.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.order.orderservice.models.Order;

@Transactional
public interface OrdersRepo extends CrudRepository<Order, Integer> {
	

}
