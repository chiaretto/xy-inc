package com.chiaretto.xyinc.api.services;

import java.util.List;
import java.util.Optional;

import com.chiaretto.xyinc.api.documents.MetaModel;

public interface MetaModelService {
	
	List<MetaModel> getAll(String model);
	
	Optional<MetaModel> getById(String model, String id);
	
	MetaModel insert(MetaModel metaModel);
	
	MetaModel update(MetaModel metaModel);
	
	void delete(String model, String id);
}
