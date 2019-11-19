package com.chiaretto.xyinc.api.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chiaretto.xyinc.api.documents.Model;

public interface ModelRepository extends MongoRepository<Model, String> {

	Optional<Model> findByName(String name);
}
