package com.chiaretto.xyinc.api.documents;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MetaModel {

	@Id
	private String id;
	
	@NotEmpty(message = "Model is required")
	private String model;
	
	@NotEmpty(message = "Fields is required")
	private Map<String, String> fields;

	public MetaModel(String id, String model, Map<String, String> fields) {
		this.id = id;
		this.model = model;
		this.fields = fields;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
	
}
