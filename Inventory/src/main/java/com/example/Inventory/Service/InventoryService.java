package com.example.Inventory.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.apache.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.example.Inventory.Controller.InventoryApplicationMainController;
import com.example.Inventory.Entity.BusInventoryModel;
import com.example.Inventory.Repository.InventoryRepository;



@Service
public class InventoryService {

	Logger logger = LoggerFactory.getLogger(InventoryService.class);
	
	@Autowired
	InventoryRepository inventoryrepo;
	
	public  List<BusInventoryModel> getInventoryDetails() {
		
		 logger.info("Inside getInventoryDetails method");
		
		 List<BusInventoryModel> bm= inventoryrepo.findAll();
		
		 
		return bm;
		// TODO Auto-generated method stub
		
	}

	public BusInventoryModel addUserDetails(BusInventoryModel AdminModel) {
		BusInventoryModel bm= inventoryrepo.save(AdminModel);
		return bm;
	}

	public BusInventoryModel editUserDetails(BusInventoryModel AdminModel) {
		  logger.info("Inside editUserDetails  method of Inventory Service");
		BusInventoryModel bm= inventoryrepo.save(AdminModel);
		// TODO Auto-generated method stub
		return bm;
	}

	public Optional<BusInventoryModel> getBookigdetailsById(String id) {
		logger.info("Inside getBookigdetailsById  method of Inventory Service");
		Optional<BusInventoryModel> AdminModel = inventoryrepo.findById(id);
		return AdminModel;
		
	}

	public String deleteBookigdetailsById(String id) {
		try {
			logger.info("Inside deleteBookigdetailsById  method of Inventory Service");
			inventoryrepo.deleteById(id);
		}
		catch(Exception e)
		{
			logger.info("Inside deleteBookigdetailsById  method of Inventory Service");
		return "No data found for id or issue came";
		}
		return "Deleted Succesfully";
	}



	@Transactional
	public Map<String, Object> updateBusSeatDetails(String id, int finalSeat) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			logger.info("Inside updateBusSeatDetails  method of Inventory Service");
		int i = inventoryrepo.updateBusSeatDetails(id,finalSeat);
		
		if (i > 0) {
			map.put("Available seat", finalSeat);
			map.put("message", "Record Updated Successfully");
			map.put("status_code", HttpStatus.SC_CREATED);

		} else {
			
			map.put("message", "Not Updated");
			map.put("status_code", HttpStatus.SC_BAD_GATEWAY);
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		map.put("Available seat", null);
		map.put("message", "Something went wrong!");
		map.put("status_code", HttpStatus.SC_BAD_REQUEST);
	}

	return map;
	}


	public int updateSeat(String bookingNumber,int finalSeat) {
		int bm= inventoryrepo.updateSeat(bookingNumber,finalSeat);
		return bm;
	}
}
