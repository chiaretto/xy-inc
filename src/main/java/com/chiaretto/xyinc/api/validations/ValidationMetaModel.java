package com.chiaretto.xyinc.api.validations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.chiaretto.xyinc.api.documents.Field;
import com.chiaretto.xyinc.api.documents.Model;

public class ValidationMetaModel {

	private List<String> errors;
	
	private Map<String, String> sanitizedFields;

	public Boolean hasErrors(Optional<Model> modelDoc, Map<String, String> fieldsMetaModel) {
		this.errors = new ArrayList<String>();
		this.sanitizedFields = new HashMap<>();
		
		if (modelDoc.isPresent()) {
			Set<Field> fieldsModel = modelDoc.get().getFields();
			// Sanitize
			fieldsMetaModel.forEach((k, v) -> {
				fieldsModel.forEach(fieldModel -> {
					if (k.toString().equalsIgnoreCase(fieldModel.getName().toString())) {
						this.sanitizedFields.put(k, v);
					}
				});
			});
			// Validate
			this.sanitizedFields.forEach((k, v) -> {
				fieldsModel.forEach(fieldModel -> {
					if (k.toString().equalsIgnoreCase(fieldModel.getName().toString())) {
						if (!this.getType(v.toString()).equalsIgnoreCase(fieldModel.getType().toString())) {
							this.errors.add("The field " + k + " must be a " + fieldModel.getType().toString() + " but is " + this.getType(v.toString()));
						}
					}
				});
			});
			if (this.errors.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			this.errors.add("Model not found");
			return true;	
		}
	}
	
	public List<String> getErrors() {
		return this.errors;
	}
	
	public Map<String, String> getSanitizedFields() {
		return this.sanitizedFields;
	}
	
	/**
	 * Basic type identify
	 * @param value
	 * @return
	 */
	public String getType(String value) {
		try {
			Integer.parseInt(value);
			return "integer";
		} catch (Exception e) {}
		try {
			Float.parseFloat(value);
			return "float";
		} catch (Exception e) {}
		return "string";
	}
}
