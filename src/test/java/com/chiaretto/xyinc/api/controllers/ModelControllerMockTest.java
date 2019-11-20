package com.chiaretto.xyinc.api.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.chiaretto.xyinc.api.documents.Field;
import com.chiaretto.xyinc.api.documents.Model;
import com.chiaretto.xyinc.api.responses.Response;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ModelControllerMockTest {
	
	private MockMvc mockMvc;
	
	@InjectMocks
	private ModelController modelController;

	@Autowired
	@MockBean
	private ModelController modelControllerMock;
	
	@Autowired
	public WebApplicationContext context;
	
	@Autowired
    FilterChainProxy springSecurityFilterChain;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(this.context)
				.addFilters(springSecurityFilterChain)
				.defaultRequest(get("/**").with(httpBasic("admin", "admin123")))
				.build();
	}
	
	@Test
	@DisplayName("List all models - Unauthorized")
	public void test01getAllUnauthorized() throws Exception {
		when(modelControllerMock.getAll()).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build());
		
		this.mockMvc.perform(get("/admin/model").with(httpBasic("none", "none")))
		.andExpect(status().isUnauthorized())
		.andReturn();
	}
	
	@Test
	@DisplayName("List all models - Forbiden")
	public void test02listAllForbidden() throws Exception {
		when(modelControllerMock.getAll()).thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN.value()).build());
		
		this.mockMvc.perform(get("/admin/model").with(httpBasic("user", "user123")))
		.andExpect(status().isForbidden())
		.andReturn();
	}
	
	@Test
	@DisplayName("List all models")
	public void test03listAll() throws Exception {
		List<Model> mockModels = Arrays.asList(
				new Model(null, "product", new HashSet<Field>(Arrays.asList(
						new Field("name", "string"),
						new Field("price", "float"),
						new Field("qty", "integer"),
						new Field("category", "string")))),
				new Model(null, "costumer", new HashSet<Field>(Arrays.asList(
						new Field("name", "string"),
						new Field("age", "integer"),
						new Field("email", "string"))))
				);
		
		when(modelControllerMock.getAll()).thenReturn(ResponseEntity.ok(new Response<List<Model>>(mockModels)));
		
		this.mockMvc.perform(get("/admin/model"))
		.andExpect(status().isOk())
		.andReturn();
	}
	

	@Test
	@DisplayName("Get By Id")
	public void test04getById() throws Exception {
		Optional<Model> modelMock = Optional.of(new Model("1", "product", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("price", "float"),
				new Field("qty", "integer"),
				new Field("category", "string")))));
		
		when(modelControllerMock.getById("1")).thenReturn(ResponseEntity.ok(new Response<Optional<Model>>(modelMock)));	
		
		this.mockMvc.perform(get("/admin/model/1"))
		.andExpect(status().isOk())
		.andReturn();
	}	
	
}
