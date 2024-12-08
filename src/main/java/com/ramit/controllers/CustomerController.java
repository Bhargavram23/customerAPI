package com.ramit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ramit.models.Customer;
import com.ramit.repository.CustomerRepository;
import com.ramit.service.CustomerService;
@CrossOrigin("http://localhost:4200")
@RestController
public class CustomerController {
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/admin/customer-count")
	public int customerCount() {
		return customerService.getCustomerCount();
	}
	
	@PostMapping("/customer")
	public Customer upsertCustomerByExample(@RequestBody Customer customer) {
		Customer customerInDB = customerService.upsertCustomerInDB(customer);

		return customerInDB;
	}

	@PostMapping("/reset-password")
	public void updatePasswordForCustomer(@RequestBody Customer customer) {
		customerService.resetPassword(customer);
	}
	
	@PostMapping("/login")
	public Customer loginCustomer(@RequestBody Customer customer)  {
		
		return customerService.login(customer);
	}
	
	@PostMapping("/register")
	public Customer registerCustomer(@RequestBody Customer customer) {
		return customerService.register(customer);
	}

}
