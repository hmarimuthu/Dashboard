package com.etaap.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.EnvironmentDao;
import com.etaap.domain.Environment;

public class EnvironmentServiceImpl implements EnvironmentService {

	@Autowired
	EnvironmentDao environmentDao;

	public void insertData(Environment env) {
		environmentDao.insertData(env);
	}

	public List<Environment> getAllEnvironmentList(String orderBy, String orderType, int offset, int noOfRecords) {
		return environmentDao.getAllEnvironmentList(orderBy, orderType, offset, noOfRecords);
	}

	public int getTotalRowCount() {
		return environmentDao.getTotalRowCount();
	}

	public List<Environment> getEnvironmentList() {
		return environmentDao.getEnvironmentList();
	}

	public void updateData(Environment env) {
		environmentDao.updateData(env);
	}

	public void deleteData(int envId) {
		environmentDao.deleteData(envId);
	}

	public Environment getEnvironment(int envId) {
		return environmentDao.getEnvironment(envId);
	}

	public String isNameExistChkByAjaxCall(int id, String name) {
		return environmentDao.isNameExistChkByAjaxCall(id, name);
	}

	@Override
	public void deleteData(String suiteIds) {
		// TODO Auto-generated method stub
		environmentDao.deleteData(suiteIds);
	}

	@Override
	public List<Environment> getEnvironmentList(Map<String, Object> paramsMap) {
		// TODO Auto-generated method stub
		return environmentDao.getEnvironmentList(paramsMap);
	}
}
