package com.example.Payment.Entity;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

@Entity
@Transactional
@Table(name = "Payment_detail")
public class Paymentmodel {
	

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="payment_id") 
	private int payment_id;
	
	@Column(name="Booking_number") 
	private Integer Booking_number;
	
	@Column(name="Status") 
	private String Status;
	
	public String getStatus() {
		return Status;
	}



	public void setStatus(String status) {
		Status = status;
	}



	@Column(name="payment_date") 
	private LocalDate payment_date;



	public int getPayment_id() {
		return payment_id;
	}



	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
	}



	public Integer getBooking_number() {
		return Booking_number;
	}



	public void setBooking_number(Integer booking_number) {
		Booking_number = booking_number;
	}



	public LocalDate getPayment_date() {
		return payment_date;
	}



	public void setPayment_date(java.util.Date date) {
		this.payment_date = java.time.LocalDate.now();
	}




	
	

}
