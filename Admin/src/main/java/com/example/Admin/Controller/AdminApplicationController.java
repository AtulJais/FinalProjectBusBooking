package com.example.Admin.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.Admin.Entity.AdminModel;
import com.example.Admin.Service.AdminService;


import java.util.*;



@RestController
@EnableAutoConfiguration
@RequestMapping("Admin")
public class AdminApplicationController {

	Logger logger = LoggerFactory.getLogger(AdminApplicationController.class);


	
	@Autowired
	AdminService adminService;
	

	
	
	@GetMapping(value = "greet" ,produces = "application/json;charset=UTF-8")
	public String greettest()
	{
		logger.info("Inside Welocome API for Admin");
		return "Hello to Admin World";
	}
	
	@GetMapping("fetchBusRouteDetails")
	public ResponseEntity<?> getAdminDetails()
	{		 
		try
		{
			
		logger.info("Inside fetchBusRouteDetails API of Admin");
		return ResponseEntity.ok(adminService.getAdminDetails());
		
		}
		catch(Exception e)
		{
			
			logger.info("Issue in fetching bus route details of admin");
			return ResponseEntity.ok("Issue in fetching data");
		}

		
	
	}
	
	@PostMapping("addBusRouteDetails")
	public ResponseEntity<?> addAdminDetails(@RequestBody AdminModel AdminModel)
	{		 
		try
		{
		logger.info("Inside addBusRouteDetails API of Admin");
		return ResponseEntity.ok(adminService.addUserDetails(AdminModel));
		}
		catch(Exception e)
		{
			return ResponseEntity.ok("Issue in adding bus route data");
		}	
	
	}
	
	@PutMapping("updateBusRouteDetails")
	public ResponseEntity<?> updateAdminDetails(@RequestBody AdminModel AdminModel)
	{		 
		try
		{
		logger.info("Inside updateBusRouteDetails API of Admin");
		return ResponseEntity.ok(adminService.editUserDetails(AdminModel));
		}
		catch(Exception e)
		{
			return ResponseEntity.ok("Issue in Updating Route Data detail");
		}
	
	}
	
	
	@GetMapping("/getBusRouteDetailsById/{id}")
	public ResponseEntity<?> getBookigdetailsById(@PathVariable int id) {
		try {
		logger.info("Inside getBusRouteDetailsById API of Admin");
		Optional<AdminModel> AdminModel=adminService.getBookigdetailsById(id);
		return ResponseEntity.ok(AdminModel);
		}
		catch(Exception e)
		{
			return ResponseEntity.ok("Issue in getBusRouteDetailsById  data");
		}
        

	}
	
	
	@DeleteMapping("/deleteBusRouteDetailsById/{id}")
	public ResponseEntity<?> deleteBookigdetailsById(@PathVariable int id) {
		try {
		logger.info("Inside deleteBusRouteDetailsById API of Admin");
		
		String a=adminService.deleteBookigdetailsById(id);
		return ResponseEntity.ok(a);
		}
		catch(Exception e)
		{
			return ResponseEntity.ok("Issue in getBusRouteDetailsById  data");
		}

	}		
	
}
