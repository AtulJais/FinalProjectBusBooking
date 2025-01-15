package com.example.Admin.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Admin.Entity.AdminModel;



@Repository
public interface AdminRepository  extends JpaRepository<AdminModel, Integer>{



	

}
	
