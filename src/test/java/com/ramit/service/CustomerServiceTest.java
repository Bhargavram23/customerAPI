package com.ramit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;

import com.ramit.models.Address;
import com.ramit.models.Customer;
import com.ramit.models.ShippingAddressClient;
import com.ramit.repository.CustomerRepository;
import com.ramit.utils.EmailUtils;

import jakarta.mail.MessagingException;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ExtendWith(value = { MockitoExtension.class })
public class CustomerServiceTest {

	@MockBean
	CustomerRepository customerRepo;
	@MockBean
	ShippingAddressClient shippingFC;
	@MockBean
	EmailUtils emailUtils;
	@InjectMocks
	CustomerService customerServiceTest;

	@BeforeEach
	void setUp() {

	}

	@Test
	public void resetPasswordTest() {
		// pre-setup
		Customer customer = Customer.builder().email("rama@gmail.com").customerId(1).phoneNumber("9381420451").build();
		when(customerRepo.findByEmail(any(String.class))).thenReturn(customer);
		when(customerRepo.save(any(Customer.class))).thenReturn(customer);
		// implement test
		customerServiceTest.resetPassword(customer);
		// verify test
	}

	@Test
	public void upsertExistingCustomerInDBTest() {
		// pre-setup
		List<Customer> Customerlist = new ArrayList<>();
		Customerlist
				.add(Customer.builder().customerId(1).email("rama@gmail.com")
						.address(Address.builder().addressId(1).line1("Ramalayam").line2("Near Main Road")
								.city("Hyderabad").state("Telangana").country("India").zipcode("500035").build())
						.build());

		when(customerRepo.findAll(any(Example.class))).thenReturn(Customerlist);

		Address testAddressInput = Address.builder().line1("Ramalayamu")
				.line2("Near Main Road RTX CrossRoad, Kothapeta").city("Hyderabad").state("Telangana").country("India")
				.zipcode("500035").build();

		Customer TestCustomerInput = Customer.builder().email("rama@gmail.com").address(testAddressInput).build();

		Address testAddressOutput = Address.builder().addressId(1).line1("Ramalayamu")
				.line2("Near Main Road RTX CrossRoad, Kothapeta").city("Hyderabad").state("Telangana").country("India")
				.zipcode("500035").build();
		Customer TestCustomerOutput = Customer.builder().customerId(1).email("rama@gmail.com").address(testAddressInput)
				.build();

		when(customerRepo.save(any(Customer.class))).thenReturn(TestCustomerOutput);
		when(shippingFC.upsertAddressByExample(any(Address.class))).thenReturn(testAddressOutput);

		// implement test

		Customer upsertCustomerInDBTestResult = customerServiceTest.upsertCustomerInDB(TestCustomerInput);

		// verify test

		assertEquals(1, upsertCustomerInDBTestResult.getCustomerId());
		assertEquals(1, upsertCustomerInDBTestResult.getAddress().getAddressId());

	}

	@Test
	public void upsertNonExistingCustomerInDBTest() {
		// pre-setup
		List<Customer> Customerlist = new ArrayList<>();

		when(customerRepo.findAll(any(Example.class))).thenReturn(Customerlist);

		Address testAddressInput = Address.builder().line1("Ramalayamu")
				.line2("Near Main Road RTX CrossRoad, Kothapeta").city("Hyderabad").state("Telangana").country("India")
				.zipcode("500035").build();

		Customer TestCustomerInput = Customer.builder().email("rama@gmail.com").address(testAddressInput).build();

		Address testAddressOutput = Address.builder().addressId(1).line1("Ramalayamu")
				.line2("Near Main Road RTX CrossRoad, Kothapeta").city("Hyderabad").state("Telangana").country("India")
				.zipcode("500035").build();
		Customer TestCustomerOutput = Customer.builder().customerId(1).email("rama@gmail.com").address(testAddressInput)
				.build();

		when(customerRepo.save(any(Customer.class))).thenReturn(TestCustomerOutput);
		when(shippingFC.upsertAddressByExample(any(Address.class))).thenReturn(testAddressOutput);

		// implement test

		Customer upsertCustomerInDBTestResult = customerServiceTest.upsertCustomerInDB(TestCustomerInput);

		// verify test

		assertEquals(1, upsertCustomerInDBTestResult.getCustomerId());
		assertEquals(1, upsertCustomerInDBTestResult.getAddress().getAddressId());

	}

	@Test
	public void loginVerfiedCustomerTest() {
		// pre-setup
		List<Customer> Customerlist = new ArrayList<>();
		Customer customerTestInput = Customer.builder().email("rama@gmail.com").password("sita").build();
		Customerlist
				.add(Customer.builder().customerId(1).email("rama@gmail.com")
						.password("sita")
						.address(Address.builder().addressId(1).line1("Ramalayam").line2("Near Main Road")
								.city("Hyderabad").state("Telangana").country("India").zipcode("500035").build())
						.isVerified(true)
						.build());
		when(customerRepo.findAll(any(Example.class))).thenReturn(Customerlist);
		// implement test
		Customer customerTestResult = customerServiceTest.login(customerTestInput);
		// verify test
		assertEquals(1, customerTestResult.getCustomerId());
		assertEquals(true, customerTestResult.isVerified());
		assertEquals(customerTestResult.getPassword(), null);
	}

	@Test
	public void loginUnVerfiedCustomerTest() {
		// pre-setup
		List<Customer> Customerlist = new ArrayList<>();
		Customer customerTestInput = Customer.builder().email("rama@gmail.com").password("sita").isVerified(false).build();
		
		when(customerRepo.findAll(any(Example.class))).thenReturn(Customerlist);
		when(customerRepo.findByEmail(any(String.class))).thenReturn(customerTestInput);
		// implement test
		Customer customerTestResult = customerServiceTest.login(customerTestInput);
		// verify test
		
		assertEquals(false, customerTestResult.isVerified());
		assertEquals(customerTestResult.getPassword(), null);
	}

	@Test
	public void loginUnregisteredCustomerTest() {
		List<Customer> Customerlist = new ArrayList<>();
		Customer customerTestInput = Customer.builder().email("rama@gmail.com").password("sita").isVerified(false).build();
		
		when(customerRepo.findAll(any(Example.class))).thenReturn(Customerlist);
		when(customerRepo.findByEmail(any(String.class))).thenReturn(null);
		// implement test
		Customer customerTestResult = customerServiceTest.login(customerTestInput);
		// verify test
		
		assertEquals(null, customerTestResult);
		
	}

	@Test
	public void registerTestNoException() throws MessagingException {
		Customer customerTestInput = Customer.builder().email("rama@gmail.com").password("sita").isVerified(false).build();
		Customer customerTestOutput = Customer.builder().customerId(1).email("rama@gmail.com").password("sita").isVerified(false).build();
		when(customerRepo.save(any(Customer.class))).thenReturn(customerTestOutput);
		
		doNothing().when(emailUtils).sendEmail(any(String.class));;
		
		// implement test
		Customer customerServiceResult = customerServiceTest.register(customerTestInput);
		
		// verify test
		assertEquals(null,customerServiceResult.getPassword());
		assertEquals(1, customerServiceResult.getCustomerId());
	}
	
	@Test
	public void registerTestException() throws MessagingException {
		Customer customerTestInput = Customer.builder().email("rama@gmail.com").password("sita").isVerified(false).build();
		Customer customerTestOutput = Customer.builder().customerId(1).email("fakeEmail@gmail.com").password("sita").isVerified(false).build();
		when(customerRepo.save(any(Customer.class))).thenReturn(customerTestOutput);
		
		doThrow(new MessagingException(" email not found")).when(emailUtils).sendEmail("fakeEmail@gmail.com");
		
		customerServiceTest.register(customerTestInput);
		
		  assertThrows(Exception.class, () -> {
	            emailUtils.sendEmail("fakeEmail@gmail.com");
	        });
		
	}
	

	@Test
	public void getCustomerCountTestNoException() {
		when(customerRepo.findByRole("user")).thenReturn(new ArrayList<>());
		int customerCountResult = customerServiceTest.getCustomerCount();
		
		assertEquals(0, customerCountResult);
	}
	
	

}
