package com.example.Inventory.Entity;

import java.sql.Date;
import java.time.LocalDate;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;


@Entity
@Table(name = "transaction")
public class BusInventoryModel {

    @Id
	@Column(name="Bus_number") 
	private String Bus_number;
	
	

	
	
	@Column(name="last_updated_Date") 
	private LocalDate last_updated_Date;
	
	@Column(name="available_seats") 
	private String Available_seats;

	public String getBus_number() {
		return Bus_number;
	}

	public void setBus_number(String bus_number) {
		Bus_number = bus_number;
	}

	public LocalDate getLast_updated_Date() {
		return last_updated_Date;
	}

	public void setLast_updated_Date(LocalDate localDate) {
	
		this.last_updated_Date = java.time.LocalDate.now();
	}

	public String getAvailable_seats() {
		return Available_seats;
	}

	public void setAvailable_seats(String available_seats) {
		Available_seats = available_seats;
	}

	@Override
	public String toString() {
		return "BusInventoryModel [Bus_number=" + Bus_number + ", last_updated_Date=" + last_updated_Date
				+ ", Available_seats=" + Available_seats + "]";
	}
	
}