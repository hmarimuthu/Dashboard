package com.etaap.dao;

import java.util.List;
import java.util.Map;

import com.etaap.domain.SystemAPI;

public interface SystemAPIDao {
	public void insertData(SystemAPI systemAPI);

	public List<SystemAPI> getAllSystemAPIList(String orderBy, String orderType, int offset, int noOfRecords);

	public int getTotalRowCount();

	public List<SystemAPI> getSystemAPIList();

	public void updateData(SystemAPI systemAPI);

	public void deleteData(int sysId);

	public SystemAPI getSystemAPI(int sysId);

	public String isNameExistChkByAjaxCall(int id, String name);
	
	public List<Map<String,Object>> getSystemApiList();
	
	public void deleteData(String sysId);

	public List<SystemAPI> getSystemAPIList(int apiId);
}
