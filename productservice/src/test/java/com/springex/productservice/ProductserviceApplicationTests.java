package com.springex.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springex.productservice.dto.ProductRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductserviceApplicationTests {

	@Container
//	@ServiceConnection
	static MySQLContainer mysqlContainer = new MySQLContainer("mysql:8.2.0")
			.withDatabaseName("product_db");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry propertyRegistry) {
		propertyRegistry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
		propertyRegistry.add("spring.datasource.password", mysqlContainer::getPassword);
		propertyRegistry.add("spring.datasource.username", mysqlContainer::getUsername);
	}

	@Test
	void testCreateProduct_shouldCreateProduct() throws Exception {
		ProductRequestDto sampProduct = getSampleRequest();
		String jsonString = objectMapper.writeValueAsString(sampProduct);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString))
				.andExpect(status().isCreated());



	}

	private ProductRequestDto getSampleRequest(){
		return ProductRequestDto.builder()
				.name("Distilled Water 1L")
				.description("1 bottle distilled water")
				.price(BigDecimal.valueOf(1L))
				.build();
	}

}
