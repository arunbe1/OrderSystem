package com.order.orderservice.item.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.order.orderservice.models.Item;



@Transactional
public interface ItemRepository extends CrudRepository<Item, Integer>{
	
}
