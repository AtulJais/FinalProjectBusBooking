package com.example.Booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class BookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingApplication.class, args);
	}
	   @Bean(name = "Inventory")
	   @LoadBalanced
	    public WebClient webClient(){
	        return WebClient.builder()
	        		.baseUrl("http://localhost:8090")
	        		.build();
	        
	    }
	   
	   @Bean(name = "Payment")
	   @LoadBalanced
	    public WebClient webClient2(){
	        return WebClient.builder()
	        		.baseUrl("http://localhost:8098")
	        		.build();
	        
	   }

}
