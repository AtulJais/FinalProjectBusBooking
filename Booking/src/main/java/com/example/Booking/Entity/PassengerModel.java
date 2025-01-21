package com.example.Booking.Entity;

import java.sql.Date;
import java.time.LocalDate;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Transactional
@Table(name = "PASSENDER_DETAIL")
public class PassengerModel {
	
	
	@Id
	@Column(name="Passengerid") 
	private String Passengerid;
	
	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
	 */
	
	
	public String getPassengerid() {
		return Passengerid;
	}



	public void setPassengerid(String passengerid) {
		Passengerid = passengerid;
	}



	public String getBooking_number() {
		return Booking_number;
	}



	public void setBooking_number(String booking_number) {
		Booking_number = booking_number;
	}


    
	@Column(name="Booking_number") 
	private String Booking_number;
	
}
