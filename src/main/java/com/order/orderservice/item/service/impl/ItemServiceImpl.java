package com.order.orderservice.item.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.orderservice.item.repository.ItemRepository;
import com.order.orderservice.item.service.ItemService;
import com.order.orderservice.models.Item;

@Service
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	private ItemRepository itemRepo;

	
    /*
     * Method to save item details to database
     * 
     * @Input Item details
     * 
     * @Output Item details
     */
	@Override
	public Item addItem(Item item) throws Exception {
		return itemRepo.save(item);
	}

    /*
     * Method to retrieve item from database based for specific id
     * 
     * @Input Item id
     * 
     * @Output Item details
     */
	@Override
	public Item getItem(Integer itemId) throws Exception {
		Optional<Item> resultItem = itemRepo.findById(itemId);
		if(resultItem.isPresent()) {
			return resultItem.get();
		} else {
			return null;
		}
	}
	
    /*
     * Method to retrieve all items from database
     * 
     * 
     * @Output All items details from database
     */
	@Override
	public Iterable<Item> getAllItem() throws Exception {
		return  itemRepo.findAll();
	}

}
