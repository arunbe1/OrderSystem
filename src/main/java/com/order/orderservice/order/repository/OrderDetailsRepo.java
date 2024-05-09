package com.order.orderservice.order.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.order.orderservice.models.OrderDetails;

public interface OrderDetailsRepo extends CrudRepository<OrderDetails,Integer> {
	
	@Query(value = "select * from order_details where order_id = :orderId", nativeQuery = true)
	 Iterable<OrderDetails> getOrderDetailsForOrderId(@Param("orderId") Integer orderId) throws Exception;

}
