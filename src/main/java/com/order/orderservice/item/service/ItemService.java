package com.order.orderservice.item.service;

import java.util.List;
import java.util.Optional;

import com.order.orderservice.models.Item;

public interface ItemService {
	
	public Item addItem(Item item) throws Exception;
	public Item getItem(Integer itemId) throws Exception;
	public Iterable<Item> getAllItem() throws Exception;
	

}
