package com.example.Booking.Entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.sql.Date;
import java.time.LocalDate;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Transactional
@Table(name = "BOOKING_DETAIL")
public class BookingModel {
	
	
	@Id 
	@Column(name="Booking_number") 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int Booking_number;
	
	@Column(name="Bus_number") 
	private int Bus_number;
	
	
	
	@Column(name="booking_date") 
	private LocalDate booking_date;
	
	@Column(name="source") 
	private String source;
	
	@Column(name="destinations") 
	private String destinations;
	
	@Column(name="Nos_of_seats") 
	private Integer Nos_of_seats;
	
	@Column(name="status") 
	private String status;

	public int getBooking_number() {
		return Booking_number;
	}

	public void setBooking_number(int booking_number) {
		Booking_number = booking_number;
	}

	public int getBus_number() {
		return Bus_number;
	}

	public void setBus_number(int bus_number) {
		Bus_number = bus_number;
	}

	public LocalDate getBooking_date() {
		return booking_date;
	}

	public void setBooking_date(java.util.Date date) {
		this.booking_date = java.time.LocalDate.now();
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestinations() {
		return destinations;
	}

	public void setDestinations(String destinations) {
		this.destinations = destinations;
	}

	public Integer getNos_of_seats() {
		return Nos_of_seats;
	}

	public void setNos_of_seats(Integer nos_of_seats) {
		Nos_of_seats = nos_of_seats;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="Booking_number",referencedColumnName = "Booking_number")
	private List<PassengerModel> passenger_model;
	
	

	public List<PassengerModel> getPassenger_model() {
		return passenger_model;
	}

	public void setPassenger_model(List<PassengerModel> passenger_model) {
		this.passenger_model = passenger_model;
	}

	@Override
	public String toString() {
		return "BookingModel [Booking_number=" + Booking_number + ", Bus_number=" + Bus_number + ", booking_date="
				+ booking_date + ", source=" + source + ", destinations=" + destinations + ", Nos_of_seats="
				+ Nos_of_seats + ", status=" + status + "]";
	}
	
	
	

}



	
