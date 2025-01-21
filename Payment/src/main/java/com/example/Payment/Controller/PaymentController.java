package com.example.Payment.Controller;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Payment.Entity.Paymentmodel;
import com.example.Payment.Repository.PaymentRepository;
import com.example.Payment.Service.PaymentService;
import com.example.Payment.dto.CustomerOrder;
import com.example.Payment.dto.OrderEvent;
import com.example.Payment.dto.PaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;






@RestController
@EnableAutoConfiguration
@RequestMapping("Payment")
public class PaymentController {
	
	Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private PaymentRepository paymentrepository;
	
	
	
	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaOrderTemplate;
	
	@Autowired
	private KafkaTemplate<String, PaymentEvent> kafkaTemplate;
	
	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplateBooking;
		
	
		@GetMapping(value = "greet" ,produces = "application/json;charset=UTF-8")
		public String greettest()
		{
			logger.info("Inside Welocome API for payment");
			return "Hello to Payment World";
		}
		
		
		@GetMapping("fetchPaymentDetails")
		public ResponseEntity<?> fetchPaymentDetails()
		{		 
			try
			{
			logger.info("Inside fetchPaymentDetails method");
			return ResponseEntity.ok(paymentService.fetchPaymentDetails());
			}
			catch(Exception e)
			{
				logger.info("Issue in fetchPaymentDetails details");
				return ResponseEntity.ok("Issue in fetching data");
			}

			
		
		}
		@PostMapping("addPaymentDetail")
		public ResponseEntity<?> addPaymentDetail(@RequestBody Paymentmodel paymentmodel)
		{		 
			try
			{
				
			logger.info("Inside add addPaymentDetail");	
			return ResponseEntity.ok(paymentService.addPaymentDetail(paymentmodel));
			}
			catch(Exception e)
			{
				logger.info("Issue in adding Payment Details ");	
				return ResponseEntity.ok("Issue in adding Payment Details data");
			}
		}
       
		@PutMapping("updatePaymentDetail")
		public ResponseEntity<?> updatePaymentDetail(@RequestBody Paymentmodel paymentmodel)
		{		 
			try
			{
			logger.info("Inside updatePaymentDetail");
			return ResponseEntity.ok(paymentService.updatePaymentDetail(paymentmodel));
			}
			catch(Exception e)
			{
				logger.info("Inside updatePaymentDetail");
				return ResponseEntity.ok("Issue in Updating Payment Data");
			}

			
		
		}
		
		@GetMapping("/getPaymentDetailsById/{id}")
		public ResponseEntity<?> getBookigdetailsById(@PathVariable int id) {
			try {
			logger.info("Inside getPaymentDetailsById");
			Optional<Paymentmodel> AdminModel=paymentService.getPaymentdetailsById(id);
			return ResponseEntity.ok(AdminModel);
			}
			catch(Exception e)
			{
				return ResponseEntity.ok("Issue in getPaymentDetailsById  data");
			}
		
	        

		}
		
		@DeleteMapping("/deletePAymentDetailsById/{id}")
		public ResponseEntity<?> deleteBookigdetailsById(@PathVariable int id) {
			try {
			logger.info("Inside deletePAymentDetailsById");
			String a=paymentService.deletePAymentDetailsById(id);
			
			
	        return ResponseEntity.ok(a);
			}
			catch(Exception e)
			{
				return ResponseEntity.ok("Issue in deletePAymentDetailsById  data");
			}
		}
	//new booking payment process using Kafka
		@KafkaListener(topics = "new-orders", groupId = "orders-group")
		public ResponseEntity<?>  processPayment(String event) throws JsonMappingException, JsonProcessingException {
			System.out.println("Recieved event for payment " + event);
			OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
			PaymentEvent paymentEvent = new PaymentEvent();
			CustomerOrder order = orderEvent.getOrder();
			Paymentmodel payment = new Paymentmodel();
			
			try {

	            payment.setBooking_number(order.getBooking_number());
	            payment.setPayment_date(new Date());
	            payment.setStatus("Success");
				logger.info("updating in payment table");
				Paymentmodel pay = 	paymentService.addPaymentDetail(payment);
				
			int payid=	pay.getPayment_id();
    			logger.info("updated in payment table");
				paymentEvent.setOrder(orderEvent.getOrder());
				paymentEvent.setType("PAYMENT_CREATED");
				paymentEvent.setPid(payid);
				logger.info("Message send to inventory");
				kafkaTemplate.send("new-payments", paymentEvent);
				return ResponseEntity.ok(paymentEvent);
			} catch (Exception e) {

				payment.setStatus("FAILED");
				paymentService.addPaymentDetail(payment);
				logger.info("Exception in payment update");
				OrderEvent oe = new OrderEvent();
				oe.setOrder(order);
				oe.setType("ORDER_REVERSED");
				kafkaOrderTemplate.send("reversed-orders", orderEvent);
				return ResponseEntity.ok("Exception in saving Payment and error message send to inventory");
			}
		
		}

		
		//kafka change
		@PutMapping("updatePaymentStatus/{bookingNumber}")
		public Boolean updatePaymentDetail(@PathVariable String bookingNumber)
		{		 
			try
			{
			logger.info("Inside updatePaymentStatus");
			if(paymentService.updatePaymentDetail(bookingNumber))
           {
				return true;
           }

			}
			catch(Exception e)
			{
				logger.info("Inside updatePaymentDetail");
				return false;
			}
			return null;

			
		
		}
		
		@KafkaListener(topics = "reversed-payments", groupId = "payments-group")
		public void reversePayment(String event) {
			System.out.println("Inside reverse payment for order "+event);
			
			try {
				PaymentEvent paymentEvent = new ObjectMapper().readValue(event, PaymentEvent.class);

				CustomerOrder order = paymentEvent.getOrder();

				Optional<Paymentmodel> payments = this.paymentrepository.findById(paymentEvent.getPid());

				payments.ifPresent(o -> {
					o.setStatus("FAILED");
					this.paymentrepository.save(o);
				});

				OrderEvent orderEvent = new OrderEvent();
				orderEvent.setOrder(paymentEvent.getOrder());
				orderEvent.setType("ORDER_REVERSED");
				kafkaTemplateBooking.send("reversed-orders", orderEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

}