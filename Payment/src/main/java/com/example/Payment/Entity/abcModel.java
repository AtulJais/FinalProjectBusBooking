package com.example.Payment.Entity;
import java.sql.Date;
import java.time.LocalDate;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
@Entity
@Table(name = "abc_n")
public class abcModel {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="payment_id") 
	private int payment_id;
	
	

	

	
	@Column(name="available_seats") 
	private String Available_seats;






	public int getPayment_id() {
		return payment_id;
	}






	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
	}






	public String getAvailable_seats() {
		return Available_seats;
	}






	public void setAvailable_seats(String available_seats) {
		Available_seats = available_seats;
	}

	
}