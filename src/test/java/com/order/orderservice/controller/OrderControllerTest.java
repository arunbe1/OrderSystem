package com.order.orderservice.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.orderservice.item.service.impl.ItemServiceImpl;
import com.order.orderservice.models.Item;
import com.order.orderservice.models.Order;
import com.order.orderservice.models.OrderDetails;
import com.order.orderservice.models.Orders;
import com.order.orderservice.order.service.impl.OrderServiceImpl;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
	
	@MockBean
	private OrderServiceImpl orderService;
	
	@MockBean
	private ItemServiceImpl itemService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static Item actualItem;
	
	private static Orders actualOrders;
	
	@BeforeAll
	static void  setup() {
		actualItem = new Item();
		actualItem.setItemId(1);
		actualItem.setItemName("Testing Item Name");
		actualItem.setItemDescription("Testing Item Description");
		actualItem.setCreatedBy("Arun");
		actualItem.setItemValue(BigDecimal.valueOf(0.5));
		Date now = new Date();
		Timestamp temp = new Timestamp(now.getTime());
		actualItem.setCreatedTimeStamp(temp);
		actualItem.setIsAvailable(true);
		
		actualOrders = new Orders();
		
		Order order = new Order();
		order.setOrderId(1);
		order.setOrderAmount(new BigDecimal(5.0));
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
	void shouldCreateItem() throws Exception {
		mockMvc.perform(post("/order/addItem").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actualItem)))
				.andExpect(status().isOk());
	}
	
	@Test
	void shouldReturnItem() throws Exception {
		when(itemService.getItem(1)).thenReturn(actualItem);

		mockMvc.perform(get("/order/getItem?ItemId=1")).andExpect(status().isOk())
		.andExpect(jsonPath("$.itemId").value(actualItem.getItemId()))
		.andExpect(jsonPath("$.itemName").value(actualItem.getItemName()))
		.andExpect(jsonPath("$.itemDescription").value(actualItem.getItemDescription()))
		.andExpect(jsonPath("$.itemValue").value(actualItem.getItemValue()));
		
	}
	
	@Test
	void shouldReturnNotFoundItem() throws Exception {
		when(itemService.getItem(10)).thenReturn(null);
		  mockMvc.perform(get("/order/getItem?ItemId=10")).andExpect(status().isOk())
		.andExpect(jsonPath("$").doesNotExist());
		
	}
	
	@Test
	void shouldGetAllItem() throws Exception {
		List<Item> items = new ArrayList<>();
		items.add(actualItem);
		when(itemService.getAllItem()).thenReturn(items);
		  mockMvc.perform(get("/order/getAllItems")).andExpect(status().isOk())
		.andExpect(jsonPath("$.size()").value(items.size()));
		
	}
	
	@Test
	void shouldCreateOrder() throws Exception {
		mockMvc.perform(post("/order/saveOrder").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actualOrders)))
				.andExpect(status().isOk());
	}
	
	@Test
	void shouldReturnOrder() throws Exception {
		when(orderService.getOrderDetails(1)).thenReturn(actualOrders);
		mockMvc.perform(get("/order/findOrderById?orderId=1")).andExpect(status().isOk())
		.andExpect(jsonPath("$.order.orderId").value(actualOrders.getOrder().getOrderId()))
		.andExpect(jsonPath("$.order.orderAmount").value(actualOrders.getOrder().getOrderAmount()))
		.andExpect(jsonPath("$.orderDetails[0].orderDetailId").value(actualOrders.getOrderDetails().get(0).getOrderDetailId()))
		.andExpect(jsonPath("$.orderDetails[0].orderId").value(actualOrders.getOrderDetails().get(0).getOrderId()))
		.andExpect(jsonPath("$.orderDetails[0].quantity").value(actualOrders.getOrderDetails().get(0).getQuantity()))
		.andExpect(jsonPath("$.orderDetails[0].totalValue").value(actualOrders.getOrderDetails().get(0).getTotalValue())
				);
		
	}
	
	@Test
	void shouldReturnNotFoundOrder() throws Exception {
		when(orderService.getOrderDetails(10)).thenReturn(null);
		  mockMvc.perform(get("/order/findOrderById?orderId=10")).andExpect(status().isOk())
		.andExpect(jsonPath("$").doesNotExist());
		
	}
	
	@Test
	void shouldGetAllOrder() throws Exception {
		List<Orders> lsOrders = new ArrayList<>();
		lsOrders.add(actualOrders);
		when(orderService.getAllOrders()).thenReturn(lsOrders);
		  mockMvc.perform(get("/order/findAllOrder")).andExpect(status().isOk())
		.andExpect(jsonPath("$.size()").value(lsOrders.size()));
		
	}
	
	

}
