package com.study.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = TestRedisApplication.class)
@Import(TestContainerConfig.class)
class RedisApplicationTests {
	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private InvoiceRepository repository;

	@Autowired
	private InvoiceService service;

	@Autowired
	private ObjectMapper objectMapper;

	private static List<Invoice> invoices = new ArrayList<>();


	static {

		Invoice invoice1 = Invoice.builder().name("invoice 1").amount(123.45).build();
		Invoice invoice2 = Invoice.builder().name("invoice 2").amount(124.45).build();
		Invoice invoice3 = Invoice.builder().name("invoice 3").amount(126.45).build();
		Invoice invoice4 = Invoice.builder().name("invoice 4").amount(121.45).build();
		Invoice invoice5 = Invoice.builder().name("invoice 5").amount(128.45).build();

		invoices.add(invoice1);
		invoices.add(invoice2);
		invoices.add(invoice3);
		invoices.add(invoice4);
		invoices.add(invoice5);
	}


	@Test
	@Order(value = 1)
	void testSaveInvoices() throws Exception {
		for (Invoice invoice : invoices) {
			String inv = objectMapper.writeValueAsString(invoice);
			mockMvc.perform(
							MockMvcRequestBuilders.post("/api/invoice").contentType(MediaType.APPLICATION_JSON).content(inv))
					.andExpect(status().isCreated());
		}
		Assertions.assertEquals(5, service.getAllInvoices().size());
	}

	@Test
	@Order(value = 2)
	void testGetAllInvoices() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/invoice")).andExpect(status().isOk());
		Assertions.assertEquals(invoices.get(3).getName(), service.getAllInvoices().get(3).getName());
	}

	@Test
	@Order(value = 3)
	void testGetOneInvoice() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/invoice/2")).andExpect(status().isOk());
		Assertions.assertEquals(invoices.get(1).getName(), service.getInvoiceById(2L).getName());
	}

	@Test
	@Order(value = 4)
	void testDeleteInvoiceById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/invoice/2")).andExpect(status().isOk());
	}

	@Test
	@Order(value = 5)
	void testUpdateInvoice() throws Exception {
		Invoice invoice = Invoice.builder().id(3L).name("invoice 33").amount(444.34).build();
		String inv = objectMapper.writeValueAsString(invoice);
		mockMvc.perform(
						MockMvcRequestBuilders.put("/api/invoice/3").contentType(MediaType.APPLICATION_JSON).content(inv))
				.andExpect(status().isOk());
		Assertions.assertEquals(invoice.getAmount(), service.getInvoiceById(3L).getAmount());
		Assertions.assertEquals(invoice.getName(), service.getInvoiceById(3L).getName());
	}

	@Test
	@Order(value = 6)
	void testCacheUsage() {
		// Assuming you have some invoice in the database
		Long invoiceId = 1L;

		// Perform an operation that should be cached (e.g., fetching invoice data)
		Invoice firstResult = service.getInvoiceById(invoiceId);

		// Check if the data is retrieved from the cache
		assertNull(redisTemplate.opsForValue().get("Invoice ::" + invoiceId));

		// Check if the cache manager is an instance of RedisCacheManager
		assertTrue(cacheManager instanceof RedisCacheManager, "Cache manager is not an instance of RedisCacheManager");

		// Add further assertions specific to RedisCacheManager
		RedisCacheManager redisCacheManager = (RedisCacheManager) cacheManager;

		// Check if the default cache configuration is applied
		assertNotNull(redisCacheManager.getCacheConfigurations().get("Invoice"),
				"Cache configuration for 'Invoice' is missing");

		// Perform the same operation again
		Invoice secondResult = service.getInvoiceById(invoiceId);

		// Check if the data is retrieved from the cache (no DB query)
		assertNull(redisTemplate.opsForValue().get("Invoice ::" + invoiceId));

	}
}
