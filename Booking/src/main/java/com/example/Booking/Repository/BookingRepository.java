package com.example.Booking.Repository;

import java.util.Optional;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.Booking.Entity.BookingModel;

public interface BookingRepository  extends JpaRepository<BookingModel, Integer>{



	@Transactional
	@Modifying
	@Query(value="update BOOKING_DETAIL set booking_date = CURDATE() ,status = ?1  where booking_number = ?2" ,nativeQuery = true)
	int updateBusSeatDetails(String status, int bookingNumber);


	
	

}
