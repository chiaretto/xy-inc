package com.chiaretto.xyinc.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
import com.chiaretto.xyinc.api.documents.MetaModel;
import com.chiaretto.xyinc.api.documents.Model;
import com.chiaretto.xyinc.api.services.MetaModelService;
import com.chiaretto.xyinc.api.services.ModelService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class MetaModelControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private MetaModelService metaModelService;
	
	@Autowired
	private ModelService modelService;
	
	@LocalServerPort
	private int port;
	
	@Test
	@DisplayName("01 - List all data of model")
	public void test01getAllByModel() {
		// Clear Model
		this.modelService.deleteByName("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		// Creating Meta Model
		this.metaModelService.insert(new MetaModel(null, "customer", new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
		{
		    put("name","Fabiano Chiaretto");
		    put("age","33");
		    put("email","chiaretto@gmail.com");
		}}));
		
		restTemplate = restTemplate.withBasicAuth("user", "user123");
		ResponseEntity<String> response = restTemplate.getForEntity("/api/{model}", String.class, model.getName());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	@DisplayName("02 - Get one data by model by id")
	public void test0200getByModelById() {
		// Clear Model and MetaModel
		this.modelService.deleteByName("customer");
		this.metaModelService.deleteByModel("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		// Creating Meta Model
		MetaModel metaModel = this.metaModelService.insert(new MetaModel(null, "customer", new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
		{
		    put("name","Fabiano Chiaretto");
		    put("age","33");
		    put("email","chiaretto@gmail.com");
		}}));
		
		// Consulting Model		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.getForEntity("/api/{model}/{id}", String.class, model.getName(), metaModel.getId());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	@DisplayName("02 .01- Get one data by model by id - Not Found")
	public void test0201getByModelByIdNotFound() {
		// Clear Model and MetaModel
		this.modelService.deleteByName("customer");
		this.metaModelService.deleteByModel("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));

		// Consulting Model		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.getForEntity("/api/{model}/{id}", String.class, model.getName(), -1);
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	@DisplayName("05 - Create new meta model")
	public void test0500createNewMetaModel() {
		// Clear Model and MetaModel
		this.modelService.deleteByName("customer");
		this.metaModelService.deleteByModel("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		// Creating Meta Data Model
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("name","Fabiano Chiaretto");
		fields.put("age","33");
		fields.put("email","chiaretto@gmail.com");
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.postForEntity("/api/{model}/", fields, String.class, model.getName());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
	}
	
	@Test
	@DisplayName("05.01 - Create new meta model invalid")
	public void test0501createNewMetaModelInvalid() {
		// Clear Model and MetaModel
		this.modelService.deleteByName("customer");
		this.metaModelService.deleteByModel("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("weight", "float"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		// Creating Meta Data Model
		Map<String, String> metaModelFields = new HashMap<String, String>();
		metaModelFields.put("name","Fabiano Chiaretto");
		metaModelFields.put("weight","100.5");
		metaModelFields.put("age","not informed");
		metaModelFields.put("email","chiaretto@gmail.com");
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.postForEntity("/api/{model}/", metaModelFields, String.class, model.getName());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	@DisplayName("05.02 - Create new meta model with model no existent")
	public void test0501createNewMetaModelWithModelNoExistent() {
		// Clear Model and MetaModel
		this.modelService.deleteByName("customer");
		this.metaModelService.deleteByModel("customer");

		// Nonexistent model
		String nonexistentModel = "xptomodel";
		
		// Creating Meta Data Model
		Map<String, String> metaModelFields = new HashMap<String, String>();
		metaModelFields.put("name","Fabiano Chiaretto");
		metaModelFields.put("age","not informed");
		metaModelFields.put("email","chiaretto@gmail.com");
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.postForEntity("/api/{model}/", metaModelFields, String.class, nonexistentModel);
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	@DisplayName("06 - Edit meta model")
	public void test0600UpdatewModel() {
		// Clear Model and MetaModel
		this.modelService.deleteByName("customer");
		this.metaModelService.deleteByModel("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		// Creating Meta Data Model
		MetaModel metaModel= this.metaModelService.insert(new MetaModel(null, "customer", new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
		{
		    put("name","Fabiano Chiaretto");
		    put("age","33");
		    put("email","chiaretto@gmail.com");
		}}));
		
		// Creating Meta Data Model
		Map<String, String> metaModelFieldsUpdate = new HashMap<String, String>();
		metaModelFieldsUpdate.put("name","Fabiano Chiaretto Fernandes");
		metaModelFieldsUpdate.put("age","33");
		metaModelFieldsUpdate.put("email","chiaretto@gmail.com");
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		HttpEntity<Map<String, String>> m = new HttpEntity<Map<String, String>>(metaModelFieldsUpdate);
		ResponseEntity<String> response = restTemplate.exchange("/api/{model}/{id}", HttpMethod.PUT, m, String.class, model.getName(), metaModel.getId());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	@DisplayName("06.01 - Edit model Invalid")
	public void test0601UpdatewModelInvalid() {
		// Clear Model and MetaModel
		this.modelService.deleteByName("customer");
		this.metaModelService.deleteByModel("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		// Creating Meta Data Model
		MetaModel metaModel= this.metaModelService.insert(new MetaModel(null, "customer", new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
		{
		    put("name","Fabiano Chiaretto");
		    put("age","33");
		    put("email","chiaretto@gmail.com");
		}}));
		
		// Creating Meta Data Model
		Map<String, String> metaModelFieldsUpdate = new HashMap<String, String>();
		metaModelFieldsUpdate.put("name","Fabiano Chiaretto Fernandes");
		metaModelFieldsUpdate.put("age","NOT INFORMED");
		metaModelFieldsUpdate.put("email","chiaretto@gmail.com");
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		HttpEntity<Map<String, String>> m = new HttpEntity<Map<String, String>>(metaModelFieldsUpdate);
		ResponseEntity<String> response = restTemplate.exchange("/api/{model}/{id}", HttpMethod.PUT, m, String.class, model.getName(), metaModel.getId());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	@DisplayName("07 - Delete meta model")
	public void test0700DeletewModel() {
		// Clear Model
		this.modelService.deleteByName("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));
		// Creating Meta Data Model
		MetaModel metaModel= this.metaModelService.insert(new MetaModel(null, "customer", new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
		{
		    put("name","Fabiano Chiaretto");
		    put("age","33");
		    put("email","chiaretto@gmail.com");
		}}));
		
		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		ResponseEntity<String> response = restTemplate.getForEntity("/api/{model}/{id}", String.class, model.getName(), metaModel.getId());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
		
		restTemplate.delete("/api/{model}/{id}", model.getName(), metaModel.getId());
		
		response = restTemplate.getForEntity("/api/{model}/{id}", String.class, model.getName(), metaModel.getId());
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	@DisplayName("07.01 - Delete meta model NotFound")
	public void test0701DeletewModelNotFound() {
		// Clear Model
		this.modelService.deleteByName("customer");
		// Creating model
		Model model = this.modelService.insert(new Model(null, "customer", new HashSet<Field>(Arrays.asList(
				new Field("name", "string"),
				new Field("age", "integer"),
				new Field("email", "string")))));

		restTemplate = restTemplate.withBasicAuth("admin", "admin123");
		restTemplate.delete("/api/{model}/{id}", model.getName(), -1);
		
		ResponseEntity<String> response = restTemplate.getForEntity("/api/{model}/{id}", String.class, model.getName(), -1);
		assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	
}
