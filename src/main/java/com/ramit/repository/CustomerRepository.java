package com.ramit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramit.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	Customer findByEmail(String email);
	List<Customer> findByRole(String role);
}
