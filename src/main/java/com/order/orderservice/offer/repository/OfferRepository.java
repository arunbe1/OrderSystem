package com.order.orderservice.offer.repository;

import org.springframework.data.repository.CrudRepository;

import com.order.orderservice.models.Offers;

public interface OfferRepository extends CrudRepository<Offers, Integer> {

}
