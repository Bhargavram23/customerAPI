package com.ramit.models;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "my-fc", url = "http://localhost:6060")
public interface ShippingAddressClient {
	@PostMapping("/shipping")
	public Address upsertAddressByExample(@RequestBody Address address);
	@GetMapping("/msg")
	public String basicResponse() ;
}
