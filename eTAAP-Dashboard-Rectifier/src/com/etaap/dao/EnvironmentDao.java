package com.etaap.dao;

import java.util.List;
import java.util.Map;

import com.etaap.domain.Environment;

public interface EnvironmentDao {
	public void insertData(Environment env);

	public List<Environment> getAllEnvironmentList(String orderBy, String orderType, int offset, int noOfRecords);

	public int getTotalRowCount();

	public List<Environment> getEnvironmentList();
	
	public List<Environment> getEnvironmentList(Map<String, Object> paramsMap);

	public void updateData(Environment env);

	public void deleteData(int envId);

	public Environment getEnvironment(int envId);

	public String isNameExistChkByAjaxCall(int id, String name);
	
	public void deleteData(String suiteIds);
}
