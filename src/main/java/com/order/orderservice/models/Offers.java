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
@Table(name="offers")
public class Offers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="offer_id", updatable = false, nullable = false)
	private Integer offerId;
	
	@Column(name="offer_details", updatable = false, nullable = false)
	private String offer_details;
	
	@Column(name="offer_type", updatable = false, nullable = false)
	private String offerType;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="created_ts")
	private Timestamp createdTimeStamp;
	
	@Column(name="start_date")
	private Timestamp startDate;
	
	@Column(name="end_date")
	private Timestamp endDate;
	
	
	@PrePersist
	protected void onCreate() {
		Date now = new Date();
		Timestamp temp = new Timestamp(now.getTime());
		createdTimeStamp = temp;
	}
	
	
}
