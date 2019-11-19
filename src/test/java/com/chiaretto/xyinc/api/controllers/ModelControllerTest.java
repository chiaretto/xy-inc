package com.chiaretto.xyinc.api.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.chiaretto.xyinc.api.documents.Field;
import com.chiaretto.xyinc.api.documents.Model;
import com.chiaretto.xyinc.api.responses.Response;
import com.google.common.base.Optional;


@SpringBootTest
@AutoConfigureMockMvc
public class ModelControllerTest {
	
	private MockMvc mockMvc;
	
	@InjectMocks
	private ModelController modelController;
	
	@Mock
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
				.build();
	}
	
	@Test
	public void test01listAllUnauthorized() throws Exception {
		List<Model> mockModels = Arrays.asList(
				new Model("product", new HashSet<Field>(Arrays.asList(
						new Field("name", "string"),
						new Field("price", "float"),
						new Field("qty", "integer"),
						new Field("category", "string")))),
				new Model("costumer", new HashSet<Field>(Arrays.asList(
						new Field("name", "string"),
						new Field("age", "integer"),
						new Field("email", "string"))))
				);
		
		when(modelControllerMock.getAll()).thenReturn(ResponseEntity.ok(new Response<List<Model>>(mockModels)));
		
		this.mockMvc.perform(get("/admin/model").with(httpBasic("none", "none")))
		.andExpect(status().isUnauthorized())
		.andReturn();
	}
	
	
	@Test
	public void test02listAllForbiden() throws Exception {
		List<Model> mockModels = Arrays.asList(
				new Model("product", new HashSet<Field>(Arrays.asList(
						new Field("name", "string"),
						new Field("price", "float"),
						new Field("qty", "integer"),
						new Field("category", "string")))),
				new Model("costumer", new HashSet<Field>(Arrays.asList(
						new Field("name", "string"),
						new Field("age", "integer"),
						new Field("email", "string"))))
				);
		
		when(modelControllerMock.getAll()).thenReturn(ResponseEntity.ok(new Response<List<Model>>(mockModels)));
		
		this.mockMvc.perform(get("/admin/model").with(httpBasic("user", "user123")))
		.andExpect(status().isForbidden())
		.andReturn();
	}
	
	
	@Test
	public void test03listAll() throws Exception {
		List<Model> mockModels = Arrays.asList(
				new Model("product", new HashSet<Field>(Arrays.asList(
						new Field("name", "string"),
						new Field("price", "float"),
						new Field("qty", "integer"),
						new Field("category", "string")))),
				new Model("costumer", new HashSet<Field>(Arrays.asList(
						new Field("name", "string"),
						new Field("age", "integer"),
						new Field("email", "string"))))
				);
		
		when(modelControllerMock.getAll()).thenReturn(ResponseEntity.ok(new Response<List<Model>>(mockModels)));
		
		this.mockMvc.perform(get("/admin/model").with(httpBasic("admin", "admin123")))
		.andExpect(status().isOk())
		.andReturn();
	}
	

	@Test
	public void test04getById() throws Exception {
		Optional<Model> modelMock = Optional.of(new Model("1", "product", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("price", "float"),
				new Field("qty", "integer"),
				new Field("category", "string")))));
		
//		when(modelControllerMock.getById("1")).thenReturn(ResponseEntity.ok(new Response<Optional<Model>>(modelMock)));	
		
		this.mockMvc.perform(get("/admin/model").with(httpBasic("admin", "admin123")))
		.andExpect(status().isOk())
		.andReturn();
	}
	
	
	@Test
	public void test05post() throws Exception {
		Model modelMockPost = new Model("product", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("price", "float"),
				new Field("qty", "integer"),
				new Field("category", "string"))));
		

		Optional<Model> modelMock = Optional.of(new Model("1", "product", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("price", "float"),
				new Field("qty", "integer"),
				new Field("category", "string")))));
		
//		when(modelControllerMock.insert(modelMockPost, null)).thenReturn(ResponseEntity.ok(new Response<Optional<Model>>(modelMock)));	
		
		String content = "{\n" + 
				"	\"name\": \"product\",\n" + 
				"	\"fields\": [\n" + 
				"		{\n" + 
				"			\"name\":\"name\",\n" + 
				"			\"type\": \"string\"\n" + 
				"		},\n" + 
				"		{\n" + 
				"			\"name\":\"price\",\n" + 
				"			\"type\": \"float\"\n" + 
				"		},\n" + 
				"		{\n" + 
				"			\"name\":\"qty\",\n" + 
				"			\"type\": \"integer\"\n" + 
				"		},\n" + 
				"		{\n" + 
				"			\"name\":\"category\",\n" + 
				"			\"type\": \"string\"\n" + 
				"		}\n" + 
				"		]\n" + 
				"}";
		
		this.mockMvc.perform(post("/admin/model")
				.content("{\"name\": \"customer\"}")
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("admin", "admin123")))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
	}
	
	
	
}
