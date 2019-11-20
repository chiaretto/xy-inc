package com.chiaretto.xyinc.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chiaretto.xyinc.api.documents.MetaModel;

public interface MetaModelRepository extends MongoRepository<MetaModel, String> {

	List<MetaModel> findByModel(String model);
	
	Optional<MetaModel> findByModelAndId(String model, String id);
	
//	MetaModel insert(MetaModel metaModel);
//	
//	MetaModel update(MetaModel metaModel);
//	
	void deleteByModelAndId(String model, String id);
	
	void deleteByModel(String model);
}
