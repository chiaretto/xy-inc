package com.chiaretto.xyinc.api.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.chiaretto.xyinc.api.documents.MetaModel;
import com.chiaretto.xyinc.api.documents.Model;
import com.chiaretto.xyinc.api.responses.Response;
import com.chiaretto.xyinc.api.services.MetaModelService;
import com.chiaretto.xyinc.api.services.ModelService;
import com.chiaretto.xyinc.api.validations.ValidationMetaModel;

import io.swagger.annotations.ApiOperation;

@Secured("ROLE_USER")
@RestController
@RequestMapping(path = "/api/{model}")
public class MetaModelController {

	private final MetaModelService metaModelService;
	private final ModelService modelService;
	
	MetaModelController(MetaModelService metaModelService,
			ModelService modelService) {
		this.metaModelService = metaModelService;
		this.modelService = modelService;
	}
	
	@ApiOperation(value = "List all data of model")
	@GetMapping
	public ResponseEntity<Response<List<Map<String, String>>>> getAll(@PathVariable("model") String model) {
		List<MetaModel> listMetaModels = this.metaModelService.getAll(model);
		List<Map<String, String>> metaModels = new ArrayList<Map<String, String>>();
		listMetaModels.forEach(metaModel -> {
			Map<String, String> metaData = metaModel.getFields();
			metaData.put("id", metaModel.getId());
			metaModels.add(metaData);
			
		});
		return ResponseEntity.ok(new Response<List<Map<String, String>>>(metaModels));
	}
	
	@ApiOperation(value = "Get a data of model by id")
	@GetMapping(path = "/{id}")
	public ResponseEntity<Response<Map<String, String>>> getById(@PathVariable("model") String model, @PathVariable String id) {
		Optional<MetaModel> metaModel = this.metaModelService.getById(model, id);
		if (metaModel.isPresent()) {
			Map<String, String> metaData = metaModel.get().getFields();
			metaData.put("id", metaModel.get().getId());
			return ResponseEntity.ok(new Response<Map<String, String>>(metaData));	
		} 
		return ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value = "Create a new data for model")
	@PostMapping
	public ResponseEntity<Response<MetaModel>> insert(@PathVariable("model") String model, @RequestBody Map<String, String> fields, MetaModel metaModel, ValidationMetaModel validationMetaModel) throws URISyntaxException {
		metaModel.setFields(fields);
		Optional<Model> modelDoc = this.modelService.getByName(model);
		if (validationMetaModel.hasErrors(modelDoc, fields)) {
			return ResponseEntity.badRequest().body(new Response<MetaModel>(validationMetaModel.getErrors()));
		}
		metaModel.setFields(validationMetaModel.getSanitizedFields());
		MetaModel newMetaModel = this.metaModelService.insert(metaModel);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newMetaModel.getId()).toUri();
		return ResponseEntity.created(location).body(new Response<MetaModel>(newMetaModel));
	}
	
	@ApiOperation(value = "Edit data for model by id")
	@PutMapping(path = "/{id}")
	public ResponseEntity<Response<MetaModel>> update(@PathVariable("model") String model, @PathVariable("id") String id, @RequestBody Map<String, String> fields, MetaModel metaModel, ValidationMetaModel validationMetaModel) {
		metaModel.setFields(fields);
		metaModel.setId(id);
		Optional<Model> modelDoc = this.modelService.getByName(model);
		if (validationMetaModel.hasErrors(modelDoc, fields)) {
			return ResponseEntity.badRequest().body(new Response<MetaModel>(validationMetaModel.getErrors()));
		}
		metaModel.setFields(validationMetaModel.getSanitizedFields());
		return ResponseEntity.ok(new Response<MetaModel>(this.metaModelService.update(metaModel)));
	}
	
	@ApiOperation(value = "Delete data of model")
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Response<Integer>> delete(@PathVariable("model") String model, @PathVariable("id") String id) {
		Optional<MetaModel> metaModel = this.metaModelService.getById(model, id);
		if (metaModel.isPresent()) {
			this.metaModelService.delete(model, id);
			return ResponseEntity.noContent().build();	
		} 
		return ResponseEntity.notFound().build();			
	}
}
