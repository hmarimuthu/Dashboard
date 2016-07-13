package com.etaap.db.statements;

import java.util.ArrayList;

import org.springframework.jdbc.core.PreparedStatementCallback;

public abstract class ETAAPPrepareStatement implements ETAAPDBStatementsConstants {

	private ArrayList types;
	private ArrayList values;
	private String query;
	
	public ETAAPPrepareStatement(String query, ArrayList types, ArrayList values) {
		this.setQuery(query);
		this.setTypes(types);
		this.setValues(values);
	}

	public ArrayList getTypes() {
		return types;
	}

	public void setTypes(ArrayList types) {
		this.types = types;
	}

	public ArrayList getValues() {
		return values;
	}

	public void setValues(ArrayList values) {
		this.values = values;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	
}
