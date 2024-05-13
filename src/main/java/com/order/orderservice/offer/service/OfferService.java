package com.order.orderservice.offer.service;

import com.order.orderservice.models.Offers;

public interface OfferService {
	
	Offers addOffer(Offers offer) throws Exception;
	Offers getOffer(Integer Id) throws Exception;
	Iterable<Offers> getAlloffers() throws Exception;

}
