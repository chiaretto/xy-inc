package com.chiaretto.xyinc.api.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.chiaretto.xyinc.api.documents.Model;
import com.chiaretto.xyinc.api.responses.Response;
import com.chiaretto.xyinc.api.services.ModelService;
import org.springframework.dao.DuplicateKeyException;

import io.swagger.annotations.ApiOperation;

@Secured("ROLE_ADMIN")
@RestController
@RequestMapping(path = "/admin/model")
public class ModelController {

	private final ModelService modelService;
	
	ModelController(ModelService modelService) {
		this.modelService = modelService;
	}
	
	@ApiOperation(value = "List all models")
	@GetMapping
	public ResponseEntity<Response<List<Model>>> getAll() {
		return ResponseEntity.ok(new Response<List<Model>>(this.modelService.getAll()));
	}
	
	@ApiOperation(value = "Get a model by id")
	@GetMapping(path = "/{id}")
	public ResponseEntity<Response<Optional<Model>>> getById(@PathVariable String id) {
		Optional<Model> model = this.modelService.getById(id);
		if (model.isPresent()) {
			return ResponseEntity.ok(new Response<Optional<Model>>(model));	
		} 
		return ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value = "Create a new model")
	@PostMapping
	public ResponseEntity<Response<Model>> insert(@Valid @RequestBody Model model, BindingResult result) throws URISyntaxException {
		if (result.hasErrors()) {
			List<String> erros = new ArrayList<String>();
			result.getAllErrors().forEach(erro -> erros.add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(new Response<Model>(erros));
		}
		try {
			Model newModel = this.modelService.insert(model);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newModel.getId()).toUri();
			return ResponseEntity.created(location).body(new Response<Model>(newModel));
		} catch (DuplicateKeyException e) {
			List<String> erros = new ArrayList<String>();
			erros.add(e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new Response<Model>(erros));
		}
	}
	
	@ApiOperation(value = "Edit a model by id")
	@PutMapping(path = "/{id}")
	public ResponseEntity<Response<Model>> update(@PathVariable(name = "id") String id, @Valid @RequestBody Model model, BindingResult result) {
		if (result.hasErrors()) {
			List<String> erros = new ArrayList<String>();
			result.getAllErrors().forEach(erro -> erros.add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(new Response<Model>(erros));
		}
		model.setId(id);
		return ResponseEntity.ok(new Response<Model>(this.modelService.update(model)));
	}
	
	@ApiOperation(value = "Delete a model")
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Response<Integer>> delete(@PathVariable String id) {
		Optional<Model> model = this.modelService.getById(id);
		if (model.isPresent()) {
			this.modelService.delete(id);
			return ResponseEntity.noContent().build();	
		} 
		return ResponseEntity.notFound().build();			
	}
}
