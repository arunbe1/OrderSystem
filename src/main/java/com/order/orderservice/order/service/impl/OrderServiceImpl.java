package com.order.orderservice.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.order.orderservice.item.repository.ItemRepository;
import com.order.orderservice.models.Item;
import com.order.orderservice.models.Offers;
import com.order.orderservice.models.Order;
import com.order.orderservice.models.OrderDetails;
import com.order.orderservice.models.Orders;
import com.order.orderservice.offer.repository.OfferRepository;
import com.order.orderservice.order.repository.OrderDetailsRepo;
import com.order.orderservice.order.repository.OrdersRepo;
import com.order.orderservice.order.service.OrderService;
import com.order.orderservice.util.OrderServiceUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private OrdersRepo ordersRepo;
	
	@Autowired
	private OrderDetailsRepo orderDetailsRepo;
	
	@Autowired
	private ItemRepository itemRepo;
	
	@Autowired
	private OfferRepository offersRepo;

	
    /*
     * Method to save order details to database
     * 
     * @Input order details
     * 
     * @Output order details
     */
	@Override
	@Transactional(rollbackOn = Exception.class)
	public Orders addOrder(Orders orders) throws Exception {
		
		List<OrderDetails> saveOrderedDetails = new ArrayList<>();
		if(orders  != null && orders.getOrder() != null) {
			logger.debug("Order Amount is:{}", orders.getOrder().getOrderAmount());
			Order order = ordersRepo.save(orders.getOrder());
			orders.setOrder(order);
			if(!ObjectUtils.isEmpty(order.getOrderId()))  {
				logger.debug("order id before saving order details:{}", order.getOrderId());
				List<OrderDetails> lsOrderDetails = orders.getOrderDetails();
				for(OrderDetails orderDetails: lsOrderDetails) {
					orderDetails.setOrderId(order.getOrderId());
					OrderDetails orderDet = orderDetailsRepo.save(orderDetails);
					saveOrderedDetails.add(orderDet);
					
				}
			}
		}
		orders.setOrderDetails(saveOrderedDetails);
		
		
		return orders;
	}

    /*
     * Method to retrieve order details for a given orderid
     * 
     * @Input order id
     * 
     * @Output order details
     */
	@Override
	public Orders getOrderDetails(Integer orderId) throws Exception {
		
		Optional<Order> order = ordersRepo.findById(orderId);
		List<OrderDetails> lsOrderDetails = new ArrayList<>();
		BigDecimal totalValue = BigDecimal.ZERO;
		BigDecimal totalOrderValue = BigDecimal.ZERO;
		Orders orders = new Orders();
		if(order.isPresent()) {
			orders.setOrder(order.get());
		}
		Iterable<OrderDetails> orderDetails = orderDetailsRepo.getOrderDetailsForOrderId(orderId);
		for(OrderDetails orderDet: orderDetails) {
			Optional<Item> optItem = itemRepo.findById(orderDet.getItemId());
			if(optItem.isPresent()) {
				Item item = optItem.get();
				orderDet.setItemName(item.getItemName());
				Integer offerId = item.getOfferId();
				// Offer present then calculate the total value using offer
				if(!ObjectUtils.isEmpty(offerId)) {
				Optional<Offers> offers = offersRepo.findById(offerId);
					if(offers.isPresent()) {
						Offers offer = offers.get();
						if(Objects.nonNull(offer.getOffer_details())) {
							totalValue = OrderServiceUtil.calculateProductAmount(offer.getOffer_details(), orderDet.getQuantity(), item.getItemValue());
						} else {
							totalValue = item.getItemValue().multiply(BigDecimal.valueOf(orderDet.getQuantity()));
						}
					} 
					// Offer not present then calculate the total value directly
				} else {
					totalValue = item.getItemValue().multiply(BigDecimal.valueOf(orderDet.getQuantity()));
				}
				totalOrderValue = totalOrderValue.add(totalValue);
				orderDet.setTotalValue(totalValue);
				lsOrderDetails.add(orderDet);
			}
		}
		orders.setOrderDetails(lsOrderDetails);
		if(Objects.nonNull(orders.getOrder())) {
			orders.getOrder().setOrderAmount(totalOrderValue);
		}
		return orders;
	}

    /*
     * Method to retrieve all order details from database
     * @Input order id
     * 
     * @Output order details
     */
	@Override
	public Iterable<Orders> getAllOrders() throws Exception {
		Iterable<Order> allOrders = ordersRepo.findAll();
		List<Orders> lsOrder = new ArrayList<>();
		BigDecimal totalValue = BigDecimal.ZERO;
		for(Order order: allOrders) {
			Orders orders = new Orders();
			List<OrderDetails> lsOrderDetails = new ArrayList<>();
			Iterable<OrderDetails> orderDetails = orderDetailsRepo.getOrderDetailsForOrderId(order.getOrderId());
			for(OrderDetails orderDet: orderDetails) {
				Optional<Item> optItem = itemRepo.findById(orderDet.getItemId());
				if(optItem.isPresent()) {
					Item item = optItem.get();
					orderDet.setItemName(item.getItemName());
					Integer offerId = item.getOfferId();
					// Offer present then calculate the total value using offer
					if(!ObjectUtils.isEmpty(offerId)) {
					Optional<Offers> offers = offersRepo.findById(offerId);
						if(offers.isPresent()) {
							Offers offer = offers.get();
							if(Objects.nonNull(offer.getOffer_details())) {
								totalValue = OrderServiceUtil.calculateProductAmount(offer.getOffer_details(), orderDet.getQuantity(), item.getItemValue());
							} else {
								totalValue = item.getItemValue().multiply(BigDecimal.valueOf(orderDet.getQuantity()));
							}
						} 
						// Offer not present then calculate the total value directly
					} else {
						totalValue = item.getItemValue().multiply(BigDecimal.valueOf(orderDet.getQuantity()));
					}
					
					orderDet.setTotalValue(totalValue);
					lsOrderDetails.add(orderDet);
				}
			}
			orders.setOrder(order);
			orders.setOrderDetails(lsOrderDetails);
			lsOrder.add(orders);
			
		}
		
		return lsOrder;
	
	}

}
