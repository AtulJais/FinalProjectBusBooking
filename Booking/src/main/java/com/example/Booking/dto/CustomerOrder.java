package com.example.Booking.dto;

import java.util.*;


public class CustomerOrder {



	private Integer Booking_number;
	
	
	private int Bus_number;
	
	private String source;

	private String destinations;

	private int Nos_of_seats;
	
	private String status;

	public int getBooking_number() {
		return Booking_number;
	}

	public void setBooking_number(int booking_number) {
		Booking_number = booking_number;
	}

	public Integer getBus_number() {
		return Bus_number;
	}

	public void setBus_number(Integer bus_number) {
		Bus_number = bus_number;
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
	

	private List<PassengerDTO> passenger_model;
	
	

	public List<PassengerDTO> getPassenger_model() {
		return passenger_model;
	}

	public void setPassenger_model(List<PassengerDTO> passenger_model) {
		this.passenger_model = passenger_model;
	}


	

	public int getAvailableSeat() {
		return availableSeat;
	}

	public void setAvailableSeat(int availableSeat) {
		this.availableSeat = availableSeat;
	}


	private int availableSeat;

	

}





	



