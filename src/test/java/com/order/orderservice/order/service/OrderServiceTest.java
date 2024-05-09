package com.order.orderservice.order.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.order.orderservice.models.Order;
import com.order.orderservice.models.OrderDetails;
import com.order.orderservice.models.Orders;
import com.order.orderservice.order.service.impl.OrderServiceImpl;

@SpringBootTest
public class OrderServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);
	
	private static RestTemplate mockRestTemplate;
	
	private static OrderServiceImpl orderService;
	
	private static Orders actualOrders;
	
	@BeforeAll
	static void  setup() {
		mockRestTemplate = mock(RestTemplate.class);
		orderService = new OrderServiceImpl();
		actualOrders = new Orders();
		
		Order order = new Order();
		order.setOrderId(1);
		order.setOrderAmount(new BigDecimal(5.0));
		Date now = new Date();
		Timestamp temp = new Timestamp(now.getTime());
		order.setCreatedTimestamp(temp);
		order.setOrderBy("Test");
		order.setOrderDate(temp);
		actualOrders.setOrder(order);
		
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setCreatedBy("Testing");
		orderDetails.setCreatedTimestamp(temp);
		orderDetails.setItemId(1);
		orderDetails.setOrderDetailId(1);
		orderDetails.setOrderId(1);
		orderDetails.setQuantity(5);
		orderDetails.setTotalValue(new BigDecimal(5.0));
		List<OrderDetails> lsOrderDetails = new ArrayList<>();
		lsOrderDetails.add(orderDetails);
		actualOrders.setOrderDetails(lsOrderDetails);
	}

	
	@Test
	public void addOrder() {
		try {
			when(orderService.addOrder(actualOrders)).thenReturn(actualOrders);
			Orders resultOrders = orderService.addOrder(actualOrders);
			assertThat(actualOrders.getOrder()).equals(resultOrders.getOrder());
			assertThat(actualOrders.getOrderDetails()).equals(resultOrders.getOrderDetails());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Add order test case failed:{}", e.getMessage());
		}
			
	}
	
	@Test
	public void testOrderExists() {
		try {
			when(orderService.getOrderDetails(1)).thenReturn(actualOrders);
			Orders resultOrder = orderService.getOrderDetails(1);
			assertThat(actualOrders.getOrder()).equals(resultOrder.getOrder());
			assertThat(actualOrders.getOrderDetails()).equals(resultOrder.getOrderDetails());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Get valid order test case failed:{}", e.getMessage());
		}
	}
	
	@Test
	public void testOrdeNotrExists() {
		try {
			when(orderService.getOrderDetails(5)).thenReturn(null);
			assertNull(orderService.getOrderDetails(5));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Get invalid order test case failed:{}", e.getMessage());
		}
	}
	
	@Test
	public void testGetAllOrders() {
		List<Orders> lsActualOrders = new ArrayList<>();
		lsActualOrders.add(actualOrders);
		try {
			when(orderService.getAllOrders()).thenReturn(lsActualOrders);
			Iterable<Orders> resultOrder = orderService.getAllOrders();
			for(Orders orders: resultOrder) {
				assertThat(actualOrders.getOrder()).equals(orders.getOrder());
				assertThat(actualOrders.getOrderDetails()).equals(orders.getOrderDetails());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Get valid order test case failed:{}", e.getMessage());
		}
	}
	
	@Test
	public void testGetAllOrderWithError() {
		try {
			when(orderService.getAllOrders()).thenReturn(null);
			assertNull(orderService.getAllOrders());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Get valid order test case failed:{}", e.getMessage());
		}
	}
	

}
