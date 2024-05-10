package com.order.orderservice.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.order.orderservice.models.Item;
import com.order.orderservice.models.Order;
import com.order.orderservice.models.OrderDetails;
import com.order.orderservice.models.Orders;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8081)
public class OrderControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderControllerTest.class);
	
    @Autowired
    protected TestRestTemplate testRestTemplate;
	
	private static Item actualItem;
	
	private static Orders actualOrders;
	
	@Autowired
	ObjectMapper objectMapper;
	
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
	public void addItem() throws JsonProcessingException {
		
		String addItem_URL = "/order/addItem";
		String itemJson = objectMapper.writeValueAsString(actualItem);
		
		stubFor(post(urlEqualTo(addItem_URL))
				.withHeader("Authorization", equalTo("Bearer foo"))
				.withRequestBody(equalToJson(itemJson, true, true))
				.willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(itemJson)));
		
		 ResponseEntity<Item> resultItem = testRestTemplate.postForEntity(addItem_URL, actualItem, Item.class);
		 
		 assertThat(resultItem.getStatusCode()).isEqualTo(HttpStatus.OK);
		 assertThat(Objects.requireNonNull(resultItem.getBody()).getItemId().equals(actualItem.getItemId()));
		
	}
	
	
	@Test
	public void getItem() throws JsonProcessingException {
		
		String getItem_URL = "/order/getItem?ItemId=1";
		String itemJson = objectMapper.writeValueAsString(actualItem);
		
		stubFor(get(urlEqualTo(getItem_URL))
				.withHeader("Authorization", equalTo("Bearer foo"))
				.willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(itemJson)));
		
		 ResponseEntity<Item> resultItem = testRestTemplate.getForEntity(getItem_URL, Item.class);
		 
		 assertThat(resultItem.getStatusCode()).isEqualTo(HttpStatus.OK);
		 assertThat(Objects.requireNonNull(resultItem.getBody()).getItemId().equals(actualItem.getItemId()));
		
	}
	
	@Test
	public void getItemNotExists() throws JsonProcessingException {
		
		String getItem_URL = "/order/getItem?ItemId=5";
		String itemJson = objectMapper.writeValueAsString(new Item());
		
		stubFor(get(urlEqualTo(getItem_URL))
				.withHeader("Authorization", equalTo("Bearer foo"))
				.willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(itemJson)));
		
		 ResponseEntity<Item> resultItem = testRestTemplate.getForEntity(getItem_URL, Item.class);
		 
		 assertThat(resultItem.getStatusCode()).isEqualTo(HttpStatus.OK);
		 assertNull(resultItem.getBody());
		
	}
	
	
	@Test
	public void getAllItem() throws IOException {
		
		String getAllItem_URL = "/order/getAllItems";
		List<Item> items = new ArrayList<>();
		items.add(actualItem);
		final StringWriter sw = new StringWriter();
		objectMapper.writeValue(sw, items);
		String itemJson = sw.toString();
		
		stubFor(get(urlEqualTo(getAllItem_URL))
				.withHeader("Authorization", equalTo("Bearer foo"))
				.willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(itemJson)));
		
		 ResponseEntity<Item[]> resultItem = testRestTemplate.getForEntity(getAllItem_URL, Item[].class);
		 
		 assertThat(resultItem.getStatusCode()).isEqualTo(HttpStatus.OK);
		 assertThat(Objects.requireNonNull(resultItem.getBody()[0]).getItemId().equals(actualItem.getItemId()));
		
	}
	
	
	@Test
	public void addOrder() throws JsonProcessingException {
		
		String addOrder_URL = "/order/saveOrder";
		String ordersJson = objectMapper.writeValueAsString(actualOrders);
		
		stubFor(post(urlEqualTo(addOrder_URL))
				.withHeader("Authorization", equalTo("Bearer foo"))
				.withRequestBody(equalToJson(ordersJson, true, true))
				.willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(ordersJson)));
		
		 ResponseEntity<Orders> resultOrders = testRestTemplate.postForEntity(addOrder_URL, actualOrders, Orders.class);
		 
		 assertThat(resultOrders.getStatusCode()).isEqualTo(HttpStatus.OK);
		 assertThat(Objects.requireNonNull(resultOrders.getBody()).getOrder().equals(actualOrders.getOrder()));
		
	}
	
	@Test
	public void testValidOrder() throws JsonProcessingException {
		String getOrder_URL = "/order/findOrderById?orderId=1";
		String orderJson = objectMapper.writeValueAsString(actualOrders);
		
		stubFor(get(urlEqualTo(getOrder_URL))
				.withHeader("Authorization", equalTo("Bearer foo"))
				.willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(orderJson)));
		
		 ResponseEntity<Orders> resultOrders = testRestTemplate.getForEntity(getOrder_URL, Orders.class);

		 assertThat(resultOrders.getStatusCode()).isEqualTo(HttpStatus.OK);
		 assertThat(Objects.requireNonNull(resultOrders.getBody()).getOrder().equals(actualOrders.getOrder()));
	}
	
	@Test
	public void testValidOrderNotExists() throws JsonProcessingException {
		
		String getOrder_URL = "/order/findOrderById?orderId=10";
		String orderJson = objectMapper.writeValueAsString(new Orders());
		
		stubFor(get(urlEqualTo(getOrder_URL))
				.withHeader("Authorization", equalTo("Bearer foo"))
				.willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(orderJson)));
		
		 ResponseEntity<Orders> resultOrders = testRestTemplate.getForEntity(getOrder_URL, Orders.class);
		 
		 assertThat(resultOrders.getStatusCode()).isEqualTo(HttpStatus.OK);
		 Object results = resultOrders.getBody();
		 assertNotNull(results);
		
	}
	
	@Test
	public void getAllOrders() throws IOException {
		
		String getAllOrder_URL = "/order/findAllOrder";
		List<Orders> lsOrders = new ArrayList<>();
		lsOrders.add(actualOrders);
		final StringWriter sw = new StringWriter();
		objectMapper.writeValue(sw, lsOrders);
		String ordersJson = sw.toString();
		
		stubFor(get(urlEqualTo(getAllOrder_URL))
				.withHeader("Authorization", equalTo("Bearer foo"))
				.willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(ordersJson)));
		
		 ResponseEntity<Orders[]> resultOrders = testRestTemplate.getForEntity(getAllOrder_URL, Orders[].class);
		 
		 assertThat(resultOrders.getStatusCode()).isEqualTo(HttpStatus.OK);
		 assertThat(Objects.requireNonNull(resultOrders.getBody()[0]).getOrder().equals(actualOrders.getOrder()));
		
	}
	


}
