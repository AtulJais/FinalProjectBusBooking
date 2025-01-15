package com.example.Admin.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Admin.Controller.AdminApplicationController;

import com.example.Admin.Entity.AdminModel;
import com.example.Admin.Repository.AdminRepository;


@Service
public class AdminService {
	
	Logger logger = LoggerFactory.getLogger(AdminService.class);
	@Autowired
	AdminRepository adminRepository;
	
	public  List<AdminModel> getAdminDetails() {
		
		  logger.info("Inside getAdminDetails method");
		 
		 
		 List<AdminModel> bm= adminRepository.findAll();
			return bm;
		
		
	
		// TODO Auto-generated method stub
		
	}

	public AdminModel addUserDetails(AdminModel AdminModel) {
		  logger.info("Inside addUserDetails  method of Admin Service");
		 AdminModel bm= adminRepository.save(AdminModel);
		return bm;
	}

	public AdminModel editUserDetails(AdminModel AdminModel) {
		  logger.info("Inside editUserDetails  method of Admin Service");
		 AdminModel bm= adminRepository.save(AdminModel);
		// TODO Auto-generated method stub
		return bm;
	}

	public Optional<AdminModel> getBookigdetailsById(int id) {
		 logger.info("Inside getBookigdetailsById  method of Admin Service");
		Optional<AdminModel> AdminModel = adminRepository.findById(id);
		return AdminModel;
		
	}

	public String deleteBookigdetailsById(int id) {
		try {
			logger.info("Inside deleteBookigdetailsById  method of Admin Service");
			adminRepository.deleteById(id);
		}
		catch(Exception e)
		{
		return "No data found for id or issue came";
		}
		return "Deleted Succesfully";
	}



}
