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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="items")
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="item_code", updatable = false, nullable = false)
	private Integer itemId;
	
	@Column(name="item_name",  nullable = false)
	private String itemName;
	
	@Column(name="item_description")
	private String itemDescription;
	
	
	@Column(name="item_price")
	private BigDecimal itemValue;
	
	@Column(name="created_ts")
	private Timestamp createdTimeStamp;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="is_available")
	private Boolean isAvailable;
	
	@PrePersist
	protected void onCreate() {
		Date now = new Date();
		Timestamp temp = new Timestamp(now.getTime());
		createdTimeStamp = temp;
	}

}
