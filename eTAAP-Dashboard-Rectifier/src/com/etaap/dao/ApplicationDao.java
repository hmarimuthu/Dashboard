package com.etaap.dao;

import java.util.List;
import java.util.Map;

import com.etaap.domain.Application;

import java.util.HashMap;

public interface ApplicationDao {
	
	public List<Application> getDashBoardApplicationList();
	
	public void insertData(Application app);

	public List<Application> getAllApplicationList(String orderBy, String orderType, int offset, int noOfRecords);

	public int getTotalRowCount();

	public List<Application> getApplicationList();

	public void updateData(Application app);

	public void deleteData(int appId);

	public Application getApplication(int appId, int apiId, String deptType);

	public List<Application> getUrlAliasList(int appId, String apiName, String deptType);

	public List<Application> getApplicationMapDetails(int appId);
	
	public List<Application> getApplicationMapDetailsForJenkins(int appId);
	
	public String getMapIdByAppId(String appId,String systemApiName);

	public List<Map<String,Object>> getApplicationSystemMapService(String appId);
	
	public List<Map<String,Object>> getApplicationSystemMapService(String appId,String systemApiName);

	public List<Map<String, Object>> getApplicationSystemMapBasedOnMapId(String mapId) ;
	
	public List<Map<String, Object>> getApplicationSystemMap(String appId, String envId, String suiteId, String bedId);
	
	public List<Application> getApplicationListSpecificSystemApi(String systemName);

	public String isNameExistChkByAjaxCall(int id, String name);

	public void deleteData(String appIds);

	public List<Application> getApplicationListToCreateTestCase();


	public HashMap getDefaultActiveEnvIdSuiteIdBedIdByAppId(String appId,
			String systemName);
	
	public Map<String, String> getAPIList();
	
	public Map<String, String> getDepartmentList();
	
	public String disableRecordForApp(String mapids);
}
