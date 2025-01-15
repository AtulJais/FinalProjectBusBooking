package com.example.Booking.Service;

import java.util.List;
import java.util.Optional;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Booking.Controller.BookingMainController;
import com.example.Booking.Entity.BookingModel;
import com.example.Booking.Repository.BookingRepository;




@Service
public class BookingService {

	
	Logger logger = LoggerFactory.getLogger(BookingService.class);
	@Autowired
	BookingRepository bookingRepository;
	
	public  List<BookingModel> getBookingDetails() {
		logger.info("inside getBookingDetails method");
		 List<BookingModel> bm= bookingRepository.findAll();
		
		 
		return bm;
		// TODO Auto-generated method stub
		
	}

	public Boolean addUserDetails(BookingModel bookingModel) {
		logger.info("inside addUserDetails method");
		try
		{
		 BookingModel bm= bookingRepository.save(bookingModel);
			return true;
		}
		catch(Exception e)
		{
		return false;
		}
	}
	
	public Boolean addUserDetailsinventory(BookingModel bookingModel) {
		try { 
			
			logger.info("inside addUserDetailsinventory method");
		BookingModel bm= bookingRepository.save(bookingModel);
		return true;
		}
		catch(Exception e)
		{
			logger.info("exciption in addUserDetailsinventory method");
		return false;
		}
	}

	public BookingModel editUserDetails(BookingModel bookingModel) {
		
		logger.info("inside editUserDetails method");
		 BookingModel bm= bookingRepository.save(bookingModel);
		// TODO Auto-generated method stub
		return bm;
	}

	public Optional<BookingModel> getBookigdetailsById(int id) {
		logger.info("inside getBookigdetailsById method");
		Optional<BookingModel> bookingModel = bookingRepository.findById(id);
		return bookingModel;
		
	}

	public String deleteBookigdetailsById(int id) {
		try {
			logger.info("inside deleteBookigdetailsById method");
		  bookingRepository.deleteById(id);
		}
		catch(Exception e)
		{
			logger.info("exception in deleteBookigdetailsById method");
		return "No data found for id or issue came";
		}
		return "Deleted Succesfully";
	}

	public BookingModel addUserDetailsinventorys(BookingModel bookingModel) {
		BookingModel bm = 	bookingRepository.save(bookingModel);
		// TODO Auto-generated method stub
		return bm;
	}

	public Boolean updateBooking(String status, int bookingNumber, int availableSeats) {
	int i = bookingRepository.updateBusSeatDetails(status,bookingNumber);
		
		if (i > 0) {
			return true;

		} else {
			
		return false;
		}
		
	}
	public Optional<BookingModel> getBookigdetailsByBN(int bookingNumber) {
		Optional<BookingModel> bookingModel = bookingRepository.findById(bookingNumber);
		return bookingModel;
	}


}
