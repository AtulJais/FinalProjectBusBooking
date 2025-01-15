package com.example.Inventory.Repository;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.Inventory.Entity.BusInventoryModel;

public interface InventoryRepository extends JpaRepository<BusInventoryModel , String> {
	
	@Transactional
	@Modifying
	@Query(value="update transaction set last_updated_date = CURDATE() ,AVAILABLE_SEATS = ?2  where BUS_NUMBER = ?1" ,nativeQuery = true)
	int updateBusSeatDetails(String id, int finalSeat);
	
	@Transactional
	@Modifying
	@Query(value="update transaction  set AVAILABLE_SEATS = ?2 , LAST_UPDATED_DATE= CURDATE() where BUS_NUMBER = ?1" ,nativeQuery = true)
	int updateSeat(String bookingNumber,int finalSeat);
	

}
