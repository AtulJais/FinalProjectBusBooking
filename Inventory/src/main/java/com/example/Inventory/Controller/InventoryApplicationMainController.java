package com.example.Inventory.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpStatus;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


import com.example.Inventory.Entity.BusInventoryModel;
import com.example.Inventory.Service.InventoryService;
import com.example.Inventory.dto.CustomerOrder;
import com.example.Inventory.dto.InventoryEvent;
import com.example.Inventory.dto.PaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;



@RestController
@EnableAutoConfiguration
@RequestMapping("Inventory")
public class InventoryApplicationMainController {
	 
	Logger logger = LoggerFactory.getLogger(InventoryApplicationMainController.class);
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	private KafkaTemplate<String, InventoryEvent> kafkaTemplate;
	
	@Autowired
	private KafkaTemplate<String, PaymentEvent> kafkaPaymentTemplate;
	
	
	
	@Autowired
	private InventoryService inventoryService;
		
	
		@GetMapping(value = "greet" ,produces = "application/json;charset=UTF-8")
		public String greettest()
		{
			logger.info("Inside Welocome API for Inventory");
			return "Hello to Inventory World";
		}


	
	
	
	@GetMapping("fetchBusInventoryDetails")
	public ResponseEntity<?> getAdminDetails()
	{		 
		try
		{
		logger.info("Inside fetchBusRouteDetails API of Inventory");
		return ResponseEntity.ok(inventoryService.getInventoryDetails());
		}
		catch(Exception e)
		{
			logger.info("Issue in fetching bus route details of Inventory");
			return ResponseEntity.ok("Issue in fetching data");
		}

		
	
	}
	
	@PostMapping("addBusRouteDetails")
	public ResponseEntity<?> addAdminDetails(@RequestBody BusInventoryModel busInventoryModel)
	{		 
		try
		{
			
		logger.info("Inside add BusRouteDetails API of Inventory");	
		return ResponseEntity.ok(inventoryService.addUserDetails(busInventoryModel));
		}
		catch(Exception e)
		{
			logger.info("Issue in adding BusRouteDetails API of Inventory");	
			return ResponseEntity.ok("Issue in adding bus route data");
		}

		
	
	}
	
	@PutMapping("updateBusRouteDetails")
	public ResponseEntity<?> updateAdminDetails(@RequestBody BusInventoryModel busInventoryModel)
	{		 
		try
		{
		logger.info("Inside updateBusRouteDetails API of Inventory");
		return ResponseEntity.ok(inventoryService.editUserDetails(busInventoryModel));
		}
		catch(Exception e)
		{
			logger.info("Inside updateBusRouteDetails API of Inventory");
			return ResponseEntity.ok("Issue in Updating Route Data");
		}

		
	
	}
	
	@PutMapping("updateBusSeatDetails/{BusNos}/{finalSeat}")
	public  ResponseEntity<Map<String, Object>> updateBusSeatDetails(@PathVariable String BusNos,@PathVariable int finalSeat)
	{		
		Map <String, Object> map=new HashMap<>();
		try
		{
		logger.info("Inside updateBusSeatDetails API of Inventory");
		map = inventoryService.updateBusSeatDetails(BusNos,finalSeat);
		}
		catch(Exception e)
		{
			logger.info("Error occurs in updateBusSeatDetails API of Inventory");
			return ResponseEntity.ok(map);
		}
		   
		   return ResponseEntity.ok(map);

		
	
	}
		
	@GetMapping("/getBusRouteDetailsById/{id}")
	public ResponseEntity<?> getBookigdetailsById(@PathVariable String id) {
		try {
		logger.info("Inside getBusRouteDetailsById API of Inventory");
		Optional<BusInventoryModel> AdminModel=inventoryService.getBookigdetailsById(id);
		return ResponseEntity.ok(AdminModel);
		}
		catch(Exception e)
		{
			return ResponseEntity.ok("Issue in getBusRouteDetailsById  data");
		}
	
        

	}
	
	
	@DeleteMapping("/deleteBusRouteDetailsById/{id}")
	public ResponseEntity<?> deleteBookigdetailsById(@PathVariable String id) {
		try {
		logger.info("Inside deleteBusRouteDetailsById API of Admin");
		String a=inventoryService.deleteBookigdetailsById(id);
		
		
        return ResponseEntity.ok(a);
		}
		catch(Exception e)
		{
			return ResponseEntity.ok("Issue in deleteBusRouteDetailsById  data");
		}
	}
	
	
	@KafkaListener(topics = "new-payments", groupId = "stock-group")
	public ResponseEntity<?> updateStock(String paymentEvent) throws JsonMappingException, JsonProcessingException {
		System.out.println("Inside update inventory for order "+paymentEvent);
		
		
		PaymentEvent p = new ObjectMapper().readValue(paymentEvent, PaymentEvent.class);
		int remainingSeat = 0;
		CustomerOrder order = p.getOrder();
		int payid = p.getPid();
		BusInventoryModel busInventoryModel = new BusInventoryModel();
		InventoryEvent inventoryEvent = new InventoryEvent();
		try {
			int NoOfSeat = order.getNos_of_seats();
		    String busNumber = 	order.getBus_number();
		    logger.info("Checking inventory again with updated seat ");
		    Optional<BusInventoryModel>  inventory= inventoryService.getBookigdetailsById(busNumber );
		    
		    BusInventoryModel inventoryUpdated =     inventory.get();
		    String availableSeat =  inventoryUpdated.getAvailable_seats();
		    
		   
		   if( Integer.parseInt(availableSeat) > NoOfSeat)
		   {
			   logger.info("Checking inventory again with updated seat ");
			   remainingSeat= Integer.parseInt(availableSeat) - NoOfSeat;
			    busInventoryModel.setAvailable_seats(String.valueOf(remainingSeat));
				busInventoryModel.setBus_number(order.getBus_number());
				busInventoryModel.setLast_updated_Date(java.time.LocalDate.now());
				logger.info("updating in inventory table");
				inventoryService.addUserDetails(busInventoryModel);
		   }
			
			logger.info("updated in inventory table");
			
			order.setAvailableSeat(String.valueOf(remainingSeat));
			inventoryEvent.setOrder(p.getOrder());
			inventoryEvent.setType("Inventory_Updated");
			
			logger.info("Message send to Order/bboking ms");
			kafkaTemplate.send("new-booking", inventoryEvent);
			
			return ResponseEntity.ok("Seat saved and Message send to Order/booking ms");
			
		} catch (Exception e) {
			PaymentEvent pe = new PaymentEvent();
			pe.setOrder(order);
			pe.setPid(payid);
			pe.setType("PAYMENT_REVERSED");
			kafkaPaymentTemplate.send("reversed-payments", pe);
		
			return ResponseEntity.ok("Exception in saving order and error message send to payment ");
				
		}
	
	
}
	
	@PutMapping("updateSeat/{busNumber}/{seat}")
	public boolean updateSeat(@PathVariable String busNumber,@PathVariable int seat)
	{		 
		try
		{
		logger.info("Inside updatePaymentStatus");
		Optional<BusInventoryModel> busInventoryModel=inventoryService.getBookigdetailsById(busNumber);
		BusInventoryModel bm = busInventoryModel.get();
		String availableSeat = bm.getAvailable_seats();
		int finalSeat = Integer.parseInt(availableSeat)+seat;
		int i = inventoryService.updateSeat(busNumber,finalSeat);
		if(i>=1)
		{
		return true;
		}
		else
		{
			return false;
		}
		}
		catch(Exception e)
		{
			logger.info("Inside updatePaymentStatus exception occured "+e);
			return false;
		}

		
	
	}

	
	
}