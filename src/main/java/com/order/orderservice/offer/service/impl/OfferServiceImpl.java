package com.order.orderservice.offer.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.orderservice.models.Offers;
import com.order.orderservice.offer.repository.OfferRepository;
import com.order.orderservice.offer.service.OfferService;

@Service
public class OfferServiceImpl implements OfferService{
	
	@Autowired
	OfferRepository offerRepo;

	@Override
	public Offers addOffer(Offers offer) throws Exception {
		
		return offerRepo.save(offer);
	}

	@Override
	public Offers getOffer(Integer Id) throws Exception {
		Optional<Offers> offers = offerRepo.findById(Id);
		if(offers.isPresent()) {
			return offers.get();
		} else {
			return null;
		}

	}

	@Override
	public Iterable<Offers> getAlloffers() throws Exception {
		// TODO Auto-generated method stub
		return offerRepo.findAll();
	}

}
