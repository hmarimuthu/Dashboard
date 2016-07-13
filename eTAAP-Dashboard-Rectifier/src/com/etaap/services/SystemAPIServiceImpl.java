package com.etaap.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.SystemAPIDao;
import com.etaap.domain.SystemAPI;

public class SystemAPIServiceImpl implements SystemAPIService {

	@Autowired
	SystemAPIDao systemApiDao;

	public void insertData(SystemAPI systemAPI) {
		systemApiDao.insertData(systemAPI);
	}

	public List<SystemAPI> getAllSystemAPIList(String orderBy, String orderType, int offset, int noOfRecords) {
		return systemApiDao.getAllSystemAPIList(orderBy, orderType, offset, noOfRecords);
	}

	public int getTotalRowCount() {
		return systemApiDao.getTotalRowCount();
	}

	public List<SystemAPI> getSystemAPIList() {
		return systemApiDao.getSystemAPIList();
	}

	public void updateData(SystemAPI systemAPI) {
		systemApiDao.updateData(systemAPI);
	}

	public void deleteData(int sysId) {
		systemApiDao.deleteData(sysId);
	}

	public SystemAPI getSystemAPI(int sysId) {
		return systemApiDao.getSystemAPI(sysId);
	}

	public String isNameExistChkByAjaxCall(int id, String name) {
		return systemApiDao.isNameExistChkByAjaxCall(id, name);
	}

	public List<Map<String, Object>> getSystemApiList() {
		// TODO Auto-generated method stub
		return systemApiDao.getSystemApiList();
	}

	@Override
	public void deleteData(String sysId) {
		// TODO Auto-generated method stub
		systemApiDao.deleteData(sysId);
	}

	public List<SystemAPI> getSystemAPIList(int apiId) {
		return systemApiDao.getSystemAPIList(apiId);
	}
}
