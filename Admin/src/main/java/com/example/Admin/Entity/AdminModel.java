package com.example.Admin.Entity;



import java.sql.Date;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

@Entity
@Table(name = "Bus_Route")
public class AdminModel {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Bus_number") 
	private int Bus_number;
		
	@Column(name="source") 
	private String source;
	
	@Column(name="destinations") 
	private String destinations;
	
	@Column(name="price") 
	private Integer price;

	public int getBus_number() {
		return Bus_number;
	}

	public void setBus_number(int bus_number) {
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

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	

	
	

}



	
