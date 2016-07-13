package com.etaap.db.statements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.jdbc.core.PreparedStatementCreator;

public class ETAAPPreparedStatementCreator extends ETAAPPrepareStatement implements PreparedStatementCreator {
	
	public ETAAPPreparedStatementCreator(String query, ArrayList types, ArrayList values) {
		super(query, types, values);
	}
	
	@Override
	public PreparedStatement createPreparedStatement(Connection connection)
			throws SQLException {
		PreparedStatement retPs = connection.prepareStatement(this.getQuery());

		if((this.getTypes() != null) && (this.getValues() != null)) { 
			for(int i = 0; i < this.getTypes().size(); i++) {
				int type = (int) this.getTypes().get(i);
				if(type == VALUE_TYPE_BIG_INT) {
					int value = (int) getValues().get(i);
					retPs.setInt(i+1, value);
				}
				if(type == VALUE_TYPE_STRING) {
					String value = (String) getValues().get(i);
					retPs.setString(i+1, value);
				}
			}
		}
		
		return retPs;
	}

}
