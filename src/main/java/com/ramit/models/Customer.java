package com.ramit.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer customerId;
	String name;
	String email;
	String password="DEFAULT";
	String role="user";
	String phoneNumber;
	boolean isVerified;
	@Transient
	Address address;
}
