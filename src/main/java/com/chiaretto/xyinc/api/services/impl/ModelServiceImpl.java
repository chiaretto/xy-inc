package com.chiaretto.xyinc.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.chiaretto.xyinc.api.documents.Model;
import com.chiaretto.xyinc.api.repositories.ModelRepository;
import com.chiaretto.xyinc.api.services.ModelService;

@Service
public class ModelServiceImpl implements ModelService {
	
	private ModelRepository modelRepository;

	public ModelServiceImpl(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}
	
	@Override
	public List<Model> getAll() {
		return this.modelRepository.findAll();
	}

	@Override
	public Optional<Model> getById(String id) {
		return this.modelRepository.findById(id);
	}
	
	@Override
	public Optional<Model> getByName(String name) {
		return this.modelRepository.findByName(name);
	}

	@Override
	public Model insert(Model model) {
		return this.modelRepository.save(model);
	}

	@Override
	public Model update(Model model) {
		return this.modelRepository.save(model);
	}

	@Override
	public void delete(String id) {
		this.modelRepository.deleteById(id);
	}
	
	@Override
	public void deleteByName(String name) {
		this.modelRepository.deleteByName(name);
	}
	
}
