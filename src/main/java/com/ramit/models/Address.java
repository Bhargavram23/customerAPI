package com.ramit.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer addressId;
	String line1;
	String line2;
	String city;
	String state;
	String country;
	String zipcode;
	Integer customerId;
}
