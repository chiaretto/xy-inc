package com.chiaretto.xyinc.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.chiaretto.xyinc.api.documents.Field;
import com.chiaretto.xyinc.api.documents.Model;
import com.chiaretto.xyinc.api.services.ModelService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ModelControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private ModelService modelService;
	
	@LocalServerPort
	private int port;
	
	@Test
	@DisplayName("01 - List all models")
	public void test01getAll() {
		// Clear Model
		this.modelService.deleteByName("customer");
		// Creating model
		this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.getForEntity("/admin/model", String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	@DisplayName("02 - Get all models is forbiden for that user")
	public void test02getAllForbiden() {
		restTemplate = restTemplate.withBasicAuth("user", "user123");
		ResponseEntity<String> response = restTemplate.getForEntity("/admin/model", String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("03 - Get one model by id")
	public void test03getById() {
		// Clear Model
		this.modelService.deleteByName("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		
		// Consulting Model		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.getForEntity("/admin/model/{id}", String.class, model.getId());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	@DisplayName("04 - Get one model by id not found")
	public void test04getByIdNotFound() {
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.getForEntity("/admin/model/{id}", String.class, -1);
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	@DisplayName("05 - Create new model")
	public void test0500createNewModel() {
		this.modelService.deleteByName("customer");
		
		Model model = new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string"))));
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.postForEntity("/admin/model", model, String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
	}
	
	@Test
	@DisplayName("05.01 - Create new model invalid")
	public void test0501createNewModelInvalid() {
		Model model = new Model(null, null, new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string"))));
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.postForEntity("/admin/model", model, String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	@DisplayName("05.02 - Create new model duplicated")
	public void test0502createNewModelDuplicated() {
		// Clear Model
		this.modelService.deleteByName("customer");
		// Creating model
		this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		
		Model modelDuplicated = new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string"))));
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.postForEntity("/admin/model", modelDuplicated, String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CONFLICT.value());
	}
	
	@Test
	@DisplayName("06 - Edit model")
	public void test0600UpdatewModel() {
		// Clear Model
		this.modelService.deleteByName("customer");
		this.modelService.deleteByName("customernew");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		
		Model modelUpdate = new Model(null, "customernew", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string"))));
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		
		HttpEntity<Model> m = new HttpEntity<Model>(modelUpdate);
		ResponseEntity<String> response = restTemplate.exchange("/admin/model/{id}", HttpMethod.PUT, m, String.class, model.getId());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	@DisplayName("06.01 - Edit model Invalid")
	public void test0601UpdatewModelInvalid() {
		// Clear Model
		this.modelService.deleteByName("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		
		Model modelUpdate = new Model(null, null, new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string"))));
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		
		HttpEntity<Model> m = new HttpEntity<Model>(modelUpdate);
		ResponseEntity<String> response = restTemplate.exchange("/admin/model/{id}", HttpMethod.PUT, m, String.class, model.getId());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	@DisplayName("07 - Delete model")
	public void test0700DeletewModel() {
		// Clear Model
		this.modelService.deleteByName("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		restTemplate.delete("/admin/model/{id}", model.getId());
		ResponseEntity<String> response = restTemplate.getForEntity("/admin/model/{id}", String.class, model.getId());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	@DisplayName("07.01 - Delete model NotFound")
	public void test0701DeletewModelNotFound() {
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		HttpEntity<String> m = new HttpEntity<String>("");
		ResponseEntity<String> response = restTemplate.exchange("/admin/model/{id}", HttpMethod.DELETE, m, String.class, -1);
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	
}
