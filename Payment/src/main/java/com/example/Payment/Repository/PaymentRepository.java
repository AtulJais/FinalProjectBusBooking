package com.example.Payment.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Payment.Entity.Paymentmodel;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface PaymentRepository  extends JpaRepository<Paymentmodel, Integer>{

	@Transactional
	@Modifying
	@Query(value="update Payment_detail set Status = 'Refund' ,PAYMENT_DATE = CURRENT_TIMESTAMP where BOOKING_NUMBER = ?1" ,nativeQuery = true)
	int updatePaymentDetail(String bookingNumber);

	
	@Query(value="select payment_id,booking_number,status,payment_date from Payment_detail where BOOKING_NUMBER = ?1" ,nativeQuery = true)
	List<Object> findByBooking(int booking_number);

}
