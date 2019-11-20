package com.chiaretto.xyinc.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.chiaretto.xyinc.api.documents.MetaModel;
import com.chiaretto.xyinc.api.repositories.MetaModelRepository;
import com.chiaretto.xyinc.api.services.MetaModelService;

@Service
public class MetaModelServiceImpl implements MetaModelService {
	
	private MetaModelRepository metaModelRepository;

	public MetaModelServiceImpl(MetaModelRepository metaModelRepository) {
		this.metaModelRepository = metaModelRepository;
	}
	
	@Override
	public List<MetaModel> getAll(String model) {
		return this.metaModelRepository.findByModel(model);
	}

	@Override
	public Optional<MetaModel> getById(String model, String id) {
		return this.metaModelRepository.findByModelAndId(model, id);
	}

	@Override
	public MetaModel insert(MetaModel metaModel) {
		return this.metaModelRepository.save(metaModel);
	}

	@Override
	public MetaModel update(MetaModel metaModel) {
		return this.metaModelRepository.save(metaModel);
	}

	@Override
	public void delete(String model, String id) {
		this.metaModelRepository.deleteByModelAndId(model, id);
	}
	
	@Override
	public void deleteByModel(String model) {
		this.metaModelRepository.deleteByModel(model);
	}
}
