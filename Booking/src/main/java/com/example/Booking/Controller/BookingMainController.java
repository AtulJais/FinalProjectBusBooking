package com.example.Booking.Controller;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;


import com.example.Booking.Entity.BookingModel;
import com.example.Booking.Entity.PassengerModel;
import com.example.Booking.Repository.BookingRepository;
import com.example.Booking.Service.BookingService;
import com.example.Booking.dto.CustomerOrder;
import com.example.Booking.dto.InventoryEvent;
import com.example.Booking.dto.OrderEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;





@RestController
@EnableAutoConfiguration
@RequestMapping("Booking")
public class BookingMainController {
	Logger logger = LoggerFactory.getLogger(BookingMainController.class);
	
	@Autowired
	BookingService bookingService;
	
	@Autowired
	BookingRepository bookingRepository;
	
	@Autowired
	@Qualifier("Inventory")
	private WebClient webClient;
	
	@Autowired
	@Qualifier("Payment")
	private WebClient webClient2;
	
	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;

	
	
	@GetMapping("greet")
	public String greettest()
	{
		logger.info("Inside Welocome API for Booking");
	
		return "Hello to Booking World";
	}
	
	@GetMapping("fetchBookingDetails")
	public ResponseEntity<?> getBookingDetails()
	{		 
		try
		{
			logger.info("Inside fetchBookingDetails controller for Booking");
		return ResponseEntity.ok(bookingService.getBookingDetails());
		}
		catch(Exception e)
		{
			logger.info("Issue in fetching data"+e);
			return ResponseEntity.ok("Issue in fetching data");
		}

		
	
	}
	//Simple Add booking API without using KAfka implementation
	@PostMapping("addBookingDetails")
	public ResponseEntity<?> addBookingDetails(@RequestBody BookingModel bookingModel)
	{	
		Map<String, String> map = new HashMap<String, String>();
		try
		{
			
			logger.info("Inside addBookingDetails controller for Booking");	
			
			if( bookingService.addUserDetails(bookingModel))
			{
				map.put("Message", "Booking created for bus number: "+bookingModel.getBus_number());
				
				map.put("status_code", "201");
				return ResponseEntity.ok(map);
				
			}
			else
			{
				logger.info("Issue in adding booking data");
				map.put("Message", "Issue in adding booking data");
				map.put("status_code", "500");
				return ResponseEntity.ok(map);
			}
		
		}
		catch(Exception e)
		{
			logger.info("Issue in adding booking data"+e);
			
			
		}
		 return ResponseEntity.ok(map);

		
	
	}
	
	@PutMapping("updateBookingDetails")
	public ResponseEntity<?> updateBookingDetails(@RequestBody BookingModel bookingModel)
	{		 
		try
		{
			logger.info("Inside updateBookingDetails controller for Booking");	
		return ResponseEntity.ok(bookingService.editUserDetails(bookingModel));
		}
		catch(Exception e)
		{
			return ResponseEntity.ok("Issue in Updating booking data");
		}

		
	
	}
	

	
	@GetMapping("/getBookigdetailsById/{id}")
	public ResponseEntity<Optional<BookingModel>> getBookigdetailsById(@PathVariable int id) {
		logger.info("Inside getBookigdetailsById controller for Booking");	
		Optional<BookingModel> bookingModel=bookingService.getBookigdetailsById(id);
        return ResponseEntity.ok(bookingModel);

	}
	
	
	@DeleteMapping("/deleteBookigdetailsById/{id}")
	public ResponseEntity<?> deleteBookigdetailsById(@PathVariable int id) {
		logger.info("Inside deleteBookigdetailsById controller for Booking");
		Map<String, Integer> successmap = new HashMap<String, Integer>();
		try {
		String a=bookingService.deleteBookigdetailsById(id);
		successmap.put("Booking deleted successfully for:", id);
		}
		catch(Exception e)
		{
			successmap.put("Booking deletion failed for:",id);	
		}
        return ResponseEntity.ok(successmap);

	}
	
	//add or create booking details using kafka
	@PostMapping("availableSeats/{id}")
	public ResponseEntity<?> getavailableSeatss(@RequestBody BookingModel bookingModel , @PathVariable String id)
	{
		CustomerOrder co = new CustomerOrder();
		Map<String, Object> successmap = new HashMap<String, Object>();
		Map<String, Object> failmap = new HashMap<String, Object>();
		String available_seats = null;
	try {
		logger.info("Inside available seat webflux call to get details of bus nos from inventory microservices");
		String busDetails=webClient.get().uri("/Inventory/getBusRouteDetailsById/"+id)
	                         .retrieve()
	                                 .bodyToMono(String.class)
	                                         .block();
		
		
		
		JSONObject jsonObj= new JSONObject(busDetails.toString());
		
	if (jsonObj.has("available_seats") ) {
		logger.info("Finding out available seat for required bus number");	
		available_seats =	(String) jsonObj.get("available_seats");
		System.out.print(available_seats);
		}
	
	}
	catch(Exception e)
	{
		logger.info("issue in calling inventrory microservices "+e);
		failmap.put("message", "Issue in calling inventory microservices");
		failmap.put("status_code", HttpStatus.SC_INTERNAL_SERVER_ERROR);
		return ResponseEntity.ok(failmap);
	}
		//Finding out passanger count from passanger model
		ArrayList _listquery = (ArrayList) bookingModel.getPassenger_model();
		
		//checking available seat with passanger count
		if (Integer.parseInt(available_seats)   > _listquery.size() )
		{
			logger.info("checking available seat with passanger count");
			try
			{
				logger.info("Inserting  booking detail and status as pending");
				bookingModel.setBus_number(Integer.parseInt(id));
       			bookingModel.setStatus("Pending");
       			
       			
				BookingModel bm = bookingService.addUserDetailsinventorys(bookingModel);
				co.setStatus(bm.getStatus());
			    co.setBooking_number(bm.getBooking_number());
				co.setBus_number(bm.getBus_number());
				co.setDestinations(bm.getDestinations());
				co.setNos_of_seats(bm.getNos_of_seats());
				co.setSource(bm.getSource());
				co.setAvailableSeat(Integer.parseInt(available_seats));
				co.setPassenger_model(_listquery);
				
				//send messgae through kafka to payment for new booking
				OrderEvent event = new OrderEvent();
			    event.setOrder(co);
				event.setType("ORDER_CREATED");
				logger.info("Message send to payment for new booking/new order created");
				kafkaTemplate.send("new-orders", event);
				
				successmap.put("Booking NO :", bm.getBooking_number());
				successmap.put("Status :", bm.getStatus());
				successmap.put("Nos_of_seats :", bm.getNos_of_seats());
				successmap.put("Source", bm.getSource());
				successmap.put("Destination", bm.getDestinations());
				successmap.put("Booking_Date",bm.getBooking_date());
				return ResponseEntity.ok(successmap);
		
			}
			catch(Exception e)
			{
				logger.info("Exception occured in booking seat updating status as failed ");
				bookingModel.setStatus("Failed");
				bookingService.addUserDetailsinventorys(bookingModel);		 
		        failmap.put("message", "Issue in Updating booking data");
			    failmap.put("status_code", HttpStatus.SC_INTERNAL_SERVER_ERROR);
			    return ResponseEntity.ok(failmap);
			}
		}
		else
		{
			   failmap.put("message", "Available seat is less than passenger number");
			   failmap.put("status_code", HttpStatus.SC_INTERNAL_SERVER_ERROR);
			   return ResponseEntity.ok(failmap);
			
		}
		
		
	}
	
	@KafkaListener(topics = "new-booking", groupId = "orders-group")
	public ResponseEntity<?>  CompleteBooking(String event) throws JsonMappingException, JsonProcessingException {
		System.out.println("Recieved event for payment " + event);
		InventoryEvent p = new ObjectMapper().readValue(event, InventoryEvent.class);

		CustomerOrder order = p.getOrder();
		BookingModel booking = new BookingModel();
		
		try {

			ArrayList _listquery = (ArrayList) booking.getPassenger_model();
		    booking.setBooking_date(new Date());
			int availableSeats =order.getAvailableSeat();
			String Status = "Confirmed";
			int bookingNumber = order.getBooking_number();
			logger.info("updating in booking table");
			boolean a = bookingService.updateBooking(Status,bookingNumber,availableSeats);
			logger.info("updated in booking table");
			return ResponseEntity.ok("Booking saved and confirmed");
			
		} catch (Exception e) {

			logger.info("Exception in booking update");
			return ResponseEntity.ok("Exception in saving booking ");
		}
	
	}
	//kafka implementation to cancel booking
	@KafkaListener(topics = "reversed-orders", groupId = "orders-group")
	public void reverseOrder(String event) {
		System.out.println("Inside reverse order for order "+event);
		
		try {
			OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);

			Optional<BookingModel> order = bookingRepository.findById(orderEvent.getOrder().getBooking_number());

			order.ifPresent(o -> {
				o.setStatus("FAILED");
				this.bookingRepository.save(o);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//api to cancel booking/order
	@PutMapping("CancelBooking/{bookingNumber}")
	public ResponseEntity<?> CancelBooking( @PathVariable int bookingNumber)
	{
		Map<String, Object> failmap = new HashMap<String, Object>();
		Optional<BookingModel> bookingModel=bookingService.getBookigdetailsByBN(bookingNumber);
		
		BookingModel	bm  =  bookingModel.get();
		String Status = null;
		int nosOfSeat = bm.getNos_of_seats();
		int busNos= bm.getBus_number();
		
		if(bm.getStatus().equalsIgnoreCase("Confirmed") )
		{
			try {
			bm.setStatus("Cancelled");
			
			bookingService.editUserDetails(bm);
			failmap.put("message", "Issue in Updating booking data");
			String paymentStatusUpdated=webClient2.put().uri("/Payment/updatePaymentStatus/"+bookingNumber)
                   .retrieve()
                           .bodyToMono(String.class)
                                    .block();
			failmap.put("message", "Issue in updating Payment data");	
			String seatUpdated=webClient.put().uri("/Inventory/updateSeat/"+busNos+"/"+nosOfSeat)
                   .retrieve()
                           .bodyToMono(String.class)
                                   .block();
			failmap.put("message", "Issue in updating Bus inventory");
			
			return ResponseEntity.ok(bm);
			}
			
			catch (Exception e)
			{
				failmap.put("status_code", HttpStatus.SC_INTERNAL_SERVER_ERROR);
				return ResponseEntity.ok(failmap);
				
			}
		}
		else {
			failmap.put("message", "Booking is not done for the given booking number");
			failmap.put("status_code", HttpStatus.SC_INTERNAL_SERVER_ERROR);
			return ResponseEntity.ok(failmap);
		}
	
	
	}
	
	
}
