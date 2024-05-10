package com.order.orderservice.util;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderServiceUtil {

	public static final Logger logger = LoggerFactory.getLogger(OrderServiceUtil.class);
	
	public static BigDecimal calculateProductAmount(String offerDetails, Integer itemQuantity, BigDecimal itemPrice) {
		BigDecimal itemTotalAmount = BigDecimal.ZERO;
		
		if(!ObjectUtils.isEmpty(offerDetails)  && offerDetails.equalsIgnoreCase(OrderDetailConstants.BUY_ONE_GET_ONE_OFFER)) {
			if(itemQuantity >= 2) {
				Integer quantity = itemQuantity / 2;
				Integer remainingQuantity = itemQuantity % 2;
				itemTotalAmount = itemPrice.multiply(BigDecimal.valueOf(quantity));
				if(remainingQuantity > 0) {
					itemTotalAmount = itemTotalAmount.add(itemPrice.multiply(BigDecimal.valueOf(remainingQuantity)));
				}
			} else 
				itemTotalAmount = itemPrice;
			} else if(!ObjectUtils.isEmpty(offerDetails)  && offerDetails.equalsIgnoreCase(OrderDetailConstants.OFFER_PRICE)) {
					String[] offers = OrderDetailConstants.OFFER_PRICE.split(" ");
					int quantity = itemQuantity/Integer.parseInt(offers[0]);
					int remainingQuantity = itemQuantity % Integer.parseInt(offers[0]);
					int multiplicationQuantity = Integer.parseInt(offers[offers.length-1]);
					itemTotalAmount = itemPrice.multiply((BigDecimal.valueOf(quantity)).multiply(BigDecimal.valueOf(multiplicationQuantity)));
					if(remainingQuantity > 0) {
						itemTotalAmount = itemTotalAmount.add(itemPrice.multiply(BigDecimal.valueOf(remainingQuantity)));
					}
					
					
			}
		
		return itemTotalAmount;
		
	}
}
