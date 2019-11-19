package com.chiaretto.xyinc.api.documents;

import javax.validation.constraints.NotEmpty;

public class Field {

	@NotEmpty
	private String name;
	
	@NotEmpty
	private String type;
	
	public Field(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
