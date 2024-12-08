package com.ramit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.ramit.models.Address;
import com.ramit.models.Customer;
import com.ramit.models.ShippingAddressClient;
import com.ramit.repository.CustomerRepository;
import com.ramit.utils.EmailUtils;

import jakarta.mail.MessagingException;
@Service
public class CustomerService {
	@Autowired
	CustomerRepository customerRepo;
	@Autowired
	ShippingAddressClient shippingFC;
	@Autowired
	EmailUtils emailUtils;
	
	public void resetPassword(Customer customer) {
		Customer customerInDB = customerRepo.findByEmail(customer.getEmail());
		customerInDB.setPassword(customer.getPassword());
		customerInDB.setVerified(true);
		customerRepo.save(customerInDB);
	}
	public Customer upsertCustomerInDB(Customer customer) {
		Customer exampleCustomer = new Customer();
		exampleCustomer.setEmail(customer.getEmail());
		List<Customer> customerList = customerRepo.findAll(Example.of(exampleCustomer));
		Customer customerInDB;
		if(!customerList.isEmpty()) {
			customerInDB = customerList.get(0);
			customerInDB.setName(customer.getName());
			customerInDB.setPhoneNumber(customer.getPhoneNumber());
			customerRepo.save(customerInDB);
		}
		else {
			customerInDB = customerRepo.save(customer);
		}
		customerInDB.setAddress(customer.getAddress());
		customerInDB.getAddress().setCustomerId(customerInDB.getCustomerId());
		
		Address upsertAddressByExample = shippingFC.upsertAddressByExample(customerInDB.getAddress());
		customerInDB.setAddress(upsertAddressByExample);
		return customerInDB;
	}
 	public Customer login(Customer customer) {
		customer.setVerified(true);
		 List<Customer> customerList = customerRepo.findAll(Example.of(customer));
		 if(!customerList.isEmpty()) {
			 Customer customerInDB = customerList.get(0);
			 customerInDB.setPassword(null);
			 return customerInDB;}
		 
		 else {
			 if(customerRepo.findByEmail(customer.getEmail())!=null) {
				 customer.setPassword(null);
				 customer.setVerified(false);
				 return customer;
			 }
			 return null;}
		
	}
	public Customer register(Customer customer) {
		Customer customerInDB = customerRepo.save(customer);
		customerInDB.setPassword(null);
		try {
			emailUtils.sendEmail(customerInDB.getEmail());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customerInDB;
	}
	public int getCustomerCount() {
		return customerRepo.findByRole("user").size();
	
	}
	
}
