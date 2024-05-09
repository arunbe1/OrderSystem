package com.order.orderservice.item.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.order.orderservice.item.service.impl.ItemServiceImpl;
import com.order.orderservice.models.Item;

@SpringBootTest
public class ItemServiceTests {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemServiceTests.class);
	
	private static RestTemplate mockRestTemplate;
	
	private static ItemServiceImpl itemService;
	
	private static Item actualItem;
	
	
	@BeforeAll
	static void  setup() {
		mockRestTemplate = mock(RestTemplate.class);
		itemService = new ItemServiceImpl();
		actualItem = new Item();
		actualItem.setItemId(1);
		actualItem.setItemName("Testing Item Name");
		actualItem.setItemDescription("Testing Item Description");
		actualItem.setCreatedBy("Arun");
		Date now = new Date();
		Timestamp temp = new Timestamp(now.getTime());
		actualItem.setCreatedTimeStamp(temp);
		actualItem.setIsAvailable(true);

		
	}
	
	@Test
	public void addItem() {
		try {
			when(itemService.addItem(actualItem)).thenReturn(actualItem);
			Item resultItem = itemService.addItem(actualItem);
			assertThat(actualItem.getItemId()).isEqualTo(resultItem.getItemId());
			assertThat(actualItem.getItemName()).isEqualTo(resultItem.getItemName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Add Item test case failed:{}", e.getMessage());
		}
	}
	
	@Test
	public void testItemExists() {
		try {
			when(itemService.getItem(1)).thenReturn(actualItem);
			Item resultItem = itemService.getItem(1);
			assertThat(actualItem.getItemId()).isEqualTo(resultItem.getItemId());
			assertThat(actualItem.getItemName()).isEqualTo(resultItem.getItemName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Get valid item test case failed:{}", e.getMessage());
		}
	}
	
	@Test
	public void testItemNotExists() {
		try {
			when(itemService.getItem(2)).thenReturn(null);
			assertNull(itemService.getItem(2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Get invalid item test case failed:{}", e.getMessage());
		}
	}
	
	@Test
	public void testGetAllItem() {
		List<Item> actualItems = new ArrayList<>();
		actualItems.add(actualItem);

		try {
			when(itemService.getAllItem()).thenReturn(actualItems);
			Iterable<Item> resultItems = itemService.getAllItem();
			for(Item item : resultItems) {
				assertThat(actualItems.get(0).getItemId()).isEqualTo(item.getItemId());
				assertThat(actualItems.get(0).getItemName()).isEqualTo(item.getItemName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Get all item test cases failed:{}", e.getMessage());
		}
	}
	
	@Test
	public void testGetAllItemWithError() {
		List<Item> actualItems = new ArrayList<>();
		actualItems.add(actualItem);
		try {
			when(itemService.getAllItem()).thenReturn(null);
			assertNull(itemService.getAllItem());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Get all item invalid test case failed:{}", e.getMessage());
		}
	}
	

}
