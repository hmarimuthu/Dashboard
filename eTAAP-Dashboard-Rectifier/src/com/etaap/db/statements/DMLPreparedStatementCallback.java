package com.etaap.db.statements;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

public class DMLPreparedStatementCallback  extends ETAAPPrepareStatement implements PreparedStatementCallback {
	
	public DMLPreparedStatementCallback(String query, ArrayList types, ArrayList values) {
		super(query, types, values);
		System.out.println("********&*&*&********* "+query);
		System.out.println("********&*&*&*********Types Size="+getTypes().size()+" Types="+getTypes());
		System.out.println("********&*&*&*********Values Size="+getValues().size()+" Values="+getValues());
	}

	@Override
	public Boolean doInPreparedStatement(java.sql.PreparedStatement ps) throws SQLException, DataAccessException {
		boolean retVal = false;
		System.out.println("Size in doPrepare="+this.getTypes().size());
		for(int i = 0; i < this.getTypes().size(); i++) {
			int type = (int) this.getTypes().get(i);
			if(type == VALUE_TYPE_BIG_INT) {
				int value = (int)this.getValues().get(i);
				ps.setInt((i+1), value);
				System.out.println("*******&*&*&*******setInt "+(i+1)+" "+value);
			}
			else if(type == VALUE_TYPE_STRING) {
				String value = (String)this.getValues().get(i);
				ps.setString((i+1), value);
				System.out.println("*******&*&*&*******setString "+(i+1)+" "+value);
			}
		}
        int numberOfRowsUpdated = ps.executeUpdate(this.getQuery(), Statement.RETURN_GENERATED_KEYS);
        if(numberOfRowsUpdated > 0) {
        	retVal = true;
        }
        return retVal;
	}

}
