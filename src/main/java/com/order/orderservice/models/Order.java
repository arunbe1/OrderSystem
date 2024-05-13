package com.order.orderservice.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="order_id", updatable = false, nullable = false)
	private Integer orderId;
	
	@Column(name="order_amount", updatable = false, nullable = false)
	private BigDecimal orderAmount;
	
	
	@Column(name="order_date", updatable = false, nullable = false)
	private Timestamp orderDate;
	
	@Column(name="order_by")
	private String orderBy;
	
	@Column(name="created_ts")
	private Timestamp createdTimestamp;
	
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name="order_id", referencedColumnName="order_id")
//	public List<OrderDetails> orderDetails;
	
	@PrePersist
	protected void onCreate() {
		Date now = new Date();
		Timestamp temp = new Timestamp(now.getTime());
		createdTimestamp = temp;
		orderDate = temp;
	}
	
	
}
