package com.ramit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramit.models.Customer;
import com.ramit.repository.CustomerRepository;
import com.ramit.service.CustomerService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

	private MockMvc mockMvc;
	ObjectMapper objectMapper;

	@InjectMocks
	CustomerController customerControllerTest;

	@MockBean
	CustomerRepository customerRepo;

	@MockBean
	CustomerService customerService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(customerControllerTest).build();
		objectMapper = new ObjectMapper(); // Initialize ObjectMapper
	}

	@Test
	public void customerCountTest() throws Exception {
		when(customerService.getCustomerCount()).thenReturn(5);
		MvcResult mvcResult = mockMvc.perform(get("/admin/customer-count").accept(MediaType.APPLICATION_JSON))
				.andReturn();
		;

		assertEquals(mvcResult.getResponse().getContentAsString(), Integer.toString(5));
	}

	@Test
	public void upsertCustomerByExampleTest() throws Exception {
		Customer customer = Customer.builder().customerId(1).name("rama").email("srirama@gmail.com").build();
		when(customerService.upsertCustomerInDB(any(Customer.class))).thenReturn(customer);
		MvcResult mvcResult = mockMvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer))).andReturn();
		String responseAsString = mvcResult.getResponse().getContentAsString();
		String expectedAsString = objectMapper.writeValueAsString(customer);
		assertEquals(expectedAsString, responseAsString);
	}

	@Test
	public void updatePasswordForCustomerTest() throws Exception {
		Customer customer = Customer.builder().customerId(1).name("rama").email("srirama@gmail.com").build();
		doNothing().when(customerService).resetPassword(any(Customer.class));
		mockMvc.perform(post("/reset-password").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer))).andReturn();
	}

	@Test
	public void loginCustomerTest() throws Exception {
		Customer customer = Customer.builder().customerId(1).name("rama").email("srirama@gmail.com").build();
		when(customerService.login(any(Customer.class))).thenReturn(customer);
		MvcResult mvcResult = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer)))
				.andReturn();
		String responseAsString = mvcResult.getResponse().getContentAsString();
		String expectedAsString = objectMapper.writeValueAsString(customer);
		assertEquals(expectedAsString, responseAsString);

	}

	@Test
	public void registerCustomerTest() throws Exception {
		Customer customer = Customer.builder().customerId(1).name("rama").email("srirama@gmail.com").build();
		when(customerService.register(any(Customer.class))).thenReturn(customer);
		MvcResult mvcResult = mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer))).andReturn();
		String responseAsString = mvcResult.getResponse().getContentAsString();
		String expectedAsString = objectMapper.writeValueAsString(customer);
		assertEquals(expectedAsString, responseAsString);

	}

}
