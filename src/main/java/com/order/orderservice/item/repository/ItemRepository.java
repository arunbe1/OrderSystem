package com.order.orderservice.item.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.order.orderservice.models.Item;



@Transactional
public interface ItemRepository extends CrudRepository<Item, Integer>{
	
	
//	@Query(value = "select * from movie_details details where details.movie_language = :language", 
//			nativeQuery = true)
//	Iterable<Items> getSpecificItem(Integer item);
}
