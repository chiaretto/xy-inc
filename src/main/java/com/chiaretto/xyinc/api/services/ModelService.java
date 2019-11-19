package com.chiaretto.xyinc.api.services;

import java.util.List;
import java.util.Optional;

import com.chiaretto.xyinc.api.documents.Model;

public interface ModelService {
	
	List<Model> getAll();
	
	Optional<Model> getById(String id);
	
	Optional<Model> getByName(String name);
	
	Model insert(Model model);
	
	Model update(Model model);
	
	void delete(String id);
}
