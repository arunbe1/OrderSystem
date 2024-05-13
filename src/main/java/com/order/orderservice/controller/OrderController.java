package com.order.orderservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.order.orderservice.item.service.impl.ItemServiceImpl;
import com.order.orderservice.models.Item;
import com.order.orderservice.models.Offers;
import com.order.orderservice.models.Orders;
import com.order.orderservice.offer.service.impl.OfferServiceImpl;
import com.order.orderservice.order.service.impl.OrderServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/order")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
	@Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ItemServiceImpl itemService;
    
    @Autowired
    private OrderServiceImpl orderService;
    
    @Autowired
    private OfferServiceImpl offerService;
    
    /*
     * Method to Add Item to the database
     * 
     * @Input Item object 
     * @Input ServletRequest
     * @Input ServletResponse
     * 
     * @Output ResponseEntity with Item object
     */
    
    @PostMapping("/addItem")
    public ResponseEntity<Item> addItem(@RequestBody Item item,HttpServletRequest request, HttpServletResponse response) {
    	logger.debug("Getting called in addItem method: {}",  item.getItemName());
    	try {
    		return ResponseEntity.ok(itemService.addItem(item));
    	}catch(Exception e) {
    		logger.error("Error while adding the item:{},", item.getItemName());
    		return ResponseEntity.badRequest().build();
    	}

    }
    
    /*
     * Method to retrieve item details based on item id
     * 
     * @Input item id
     * @Input ServletRequest
     * @Input ServletResponse
     * 
     * @Output ResponseEntity with Item object
     */
    @GetMapping("/getItem")
    public ResponseEntity<Item> getItem(@RequestParam("ItemId") Integer itemId,HttpServletRequest request, HttpServletResponse response) {
    	logger.debug("Getting called in getItem method: {}",  itemId);
    	try {
    		return ResponseEntity.ok(itemService.getItem(itemId));
    	}catch(Exception e) {
    		logger.error("Error while fetching the item:{},", e.getMessage());
    		return ResponseEntity.badRequest().build();
    	}

    }
    
    /*
     * Method to retrieve all item details based on item id
     * 
     * @Input ServletRequest
     * @Input ServletResponse
     * 
     * @Output ResponseEntity with Item object
     */
    @GetMapping("/getAllItems")
    public ResponseEntity<Object[]> getAllItems(HttpServletRequest request, HttpServletResponse response) {
    	logger.debug("Getting called in getAllItems method:");
    	try {
    		Iterable<Item> items = itemService.getAllItem();
    		List<Item> lsItems = new ArrayList<>();
    		items.forEach(lsItems::add);
    		return ResponseEntity.ok(lsItems.toArray());
    	}catch(Exception e) {
    		logger.error("Error while fetching all item:{}", e.getMessage());
    		return ResponseEntity.badRequest().build();
    	}

    }
    
    /*
     * Method to save order details in database
     * 
     * @Input Order details
     * @Input ServletRequest
     * @Input ServletResponse
     * 
     * @Output ResponseEntity with Order object
     */
    @PostMapping("/saveOrder")
    public ResponseEntity<Orders> saveOrder(@RequestBody Orders order,HttpServletRequest request, HttpServletResponse response) {
    	if(order != null) {
    		try {
    			if(!CollectionUtils.isEmpty(order.getOrderDetails()) && Objects.nonNull(order.getOrderDetails().get(0))) {
    				logger.debug("Getting called inside saveOrder:{}", order.getOrderDetails().get(0).getItemId());
    				
    			}
    			order = orderService.addOrder(order);
			} catch (Exception e) {
				logger.error("Error while saving order:{}", e.getMessage());
				return ResponseEntity.badRequest().build();
			}
    	}
    	return  ResponseEntity.ok(order);
    }
    
    
    /*
     * Method to retrieve order based on order id
     * 
     * @Input order Id
     * @Input ServletRequest
     * @Input ServletResponse
     * 
     * @Output ResponseEntity with Order object
     */
    @GetMapping("/findOrderById")
    public ResponseEntity<Orders> getAllOrder(@RequestParam("orderId") Integer orderId, HttpServletRequest request, HttpServletResponse response) {

    	try {
			return  ResponseEntity.ok(orderService.getOrderDetails(orderId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error while fetching order:{}", e.getMessage());
			return ResponseEntity.badRequest().build();
		}
    }
    
    
    /*
     * Method to retrieve all existing orders from database
     * 
     * @Input ServletRequest
     * @Input ServletResponse
     * 
     * @Output ResponseEntity with Order object
     */
    @GetMapping("/findAllOrder")
    public ResponseEntity<Object[]> getAllOrders(HttpServletRequest request, HttpServletResponse response) {

		
    	logger.debug("Getting called in getAllOrders method:");
    	try {
    		Iterable<Orders> orders = orderService.getAllOrders();
    		List<Orders> lsOrders = new ArrayList<>();
    		orders.forEach(lsOrders::add);
    		return ResponseEntity.ok(lsOrders.toArray());
    	}catch(Exception e) {
    		logger.error("Error while fetching all orders:{}", e.getMessage());
    		return ResponseEntity.badRequest().build();
    	}
    }
    
    
    @PostMapping("/addOffer")
    public ResponseEntity<Offers> saveOffer(@RequestBody Offers offer, HttpServletRequest request, HttpServletResponse response) {
    	
    	try {
    		Offers resultOffer = offerService.addOffer(offer);
    		return ResponseEntity.ok(resultOffer);
    		} catch(Exception e) {
    			return ResponseEntity.badRequest().build();
    		}
    	
    }
    
    @GetMapping("/findOfferById")
    public ResponseEntity<Offers> getOfferById(@RequestParam("offerId") Integer offerId, HttpServletRequest request, HttpServletResponse response) {

    	try {
			return  ResponseEntity.ok(offerService.getOffer(offerId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error while fetching order:{}", e.getMessage());
			return ResponseEntity.badRequest().build();
		}
    }
    
    @GetMapping("/findAllOffer")
    public ResponseEntity<Object[]> getAllOffer(HttpServletRequest request, HttpServletResponse response) {

		
    	logger.debug("Getting called in getAllOrders method:");
    	try {
    		Iterable<Offers> offers = offerService.getAlloffers();
    		List<Offers> lsOffers = new ArrayList<>();
    		offers.forEach(lsOffers::add);
    		return ResponseEntity.ok(lsOffers.toArray());
    	}catch(Exception e) {
    		logger.error("Error while fetching all orders:{}", e.getMessage());
    		return ResponseEntity.badRequest().build();
    	}
    }
    
}

