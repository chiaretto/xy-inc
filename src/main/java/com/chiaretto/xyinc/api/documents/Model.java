package com.chiaretto.xyinc.api.documents;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Model {
	
	@Id
	private String id;
	
	@NotEmpty(message = "Name is empty")
	@Indexed(unique = true)
	private String name;
	
	@NotEmpty(message = "Field is empty")
	private Set<Field> fields;
	
	public Model(String id, String name, Set<Field> fields) {
		this.id = id;
		this.name = name;
		this.fields = fields;
	}
	
	public Model(String name, Set<Field> fields) {
		this.name = name;
		this.fields = fields;
	}
	
	public Model(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Field> getFields() {
		return fields;
	}
	public void setFields(Set<Field> fields) {
		this.fields = fields;
	}
	
}
