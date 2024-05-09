package com.order.orderservice.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="order_details")
public class OrderDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="order_detail_id", updatable = false, nullable = false)
	private Integer orderDetailId;
	
	@Column(name="order_id", updatable = false, nullable = false)
	private Integer orderId;
	
	
	@Column(name="item_code", updatable = false, nullable = false)
	private Integer itemId;
	
	@Column(name="quantity", updatable = false, nullable = false)
	private Integer quantity;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="created_ts")
	private Timestamp createdTimestamp;
	
	@Transient
	private BigDecimal totalValue;
	
	@Transient
	private String itemName;
	
	@PrePersist
	protected void onCreate() {
		Date now = new Date();
		Timestamp temp = new Timestamp(now.getTime());
		createdTimestamp = temp;
	}
}
