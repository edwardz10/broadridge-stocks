package com.broadridge.stocks.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TemporalType;

@Entity
@Table(name = "bet")
public class Bet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "bet_id")
	private Long betId;

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

	@Column(name = "amount")
	private Integer amount;

    @Column(name = "timestamp", nullable = false)
	@javax.persistence.Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	public Bet() {
	}
	
	public Bet(Product product, Integer amount) {
		this.product = product;
		this.amount = amount;
	}

	public Long getBetId() {
		return betId;
	}

	public void setBetId(Long betId) {
		this.betId = betId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "product=" + product
			+ ", amount = " + amount
			+ ", timestamp = " + timestamp;
	}
}
