package com.example.Payment.Service;

import java.util.List;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Payment.Entity.Paymentmodel;
import com.example.Payment.Repository.PaymentRepository;

@Service
public class PaymentService {

	Logger logger = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	PaymentRepository paymentRepository;
	
	public  List<Paymentmodel> fetchPaymentDetails() {
		
		  logger.info("Inside getPayment method");
		 
		 
		 List<Paymentmodel> bm= paymentRepository.findAll();
			return bm;
		
		
	
		// TODO Auto-generated method stub
		
	}

	public Paymentmodel addPaymentDetail(Paymentmodel paymentmodel) {
		  logger.info("Inside addPaymentDetail  method");
		  Paymentmodel bm= paymentRepository.save(paymentmodel);
		return bm;
	}

	public Paymentmodel updatePaymentDetail(Paymentmodel paymentmodel) {
		  logger.info("Inside updatePaymentDetail  method ");
		  Paymentmodel bm= paymentRepository.save(paymentmodel);
		// TODO Auto-generated method stub
		return bm;
	}

	public Optional<Paymentmodel> getPaymentdetailsById(int id) {
		 logger.info("Inside getPaymentdetailsById  method ");
		Optional<Paymentmodel> AdminModel = paymentRepository.findById(id);
		return AdminModel;
		
	}

	public String deletePAymentDetailsById(int id) {
		try {
			logger.info("Inside deletePAymentDetailsById  method");
			paymentRepository.deleteById(id);
		}
		catch(Exception e)
		{
		return "No data found for id or issue came";
		}
		return "Deleted Succesfully";
	}
	
	public Boolean updatePaymentDetail(String bookingNumber) {
		 int i = paymentRepository.updatePaymentDetail(bookingNumber);
		 if(i ==1)
		 {
		return true;
		 }
		 else
		 {
				return false; 
		 }
	}
}


