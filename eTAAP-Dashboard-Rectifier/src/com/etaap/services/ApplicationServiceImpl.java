package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.ApplicationDao;
import com.etaap.domain.Application;

public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	ApplicationDao applicationDao;
	
	public List<Application> getDashBoardApplicationList(){
		return applicationDao.getDashBoardApplicationList();
	}
	
	public void insertData(Application app) {
		applicationDao.insertData(app);
	}

	public List<Application> getAllApplicationList(String orderBy, String orderType, int offset, int noOfRecords) {
		return applicationDao.getAllApplicationList(orderBy, orderType, offset, noOfRecords);
	}

	public int getTotalRowCount() {
		return applicationDao.getTotalRowCount();
	}

	public List<Application> getApplicationList() {
		return applicationDao.getApplicationList();
	}

	public void updateData(Application app) {
		applicationDao.updateData(app);
	}

	public void deleteData(int appId) {
		applicationDao.deleteData(appId);
	}

	public Application getApplication(int appId, int apiId, String deptType) {
		return applicationDao.getApplication(appId, apiId, deptType);
	}

	public List<Application> getUrlAliasList(int appId, String apiName, String deptType) {
		return applicationDao.getUrlAliasList(appId, apiName, deptType);
	}

	public List<Application> getApplicationMapDetails(int appId) {
		return applicationDao.getApplicationMapDetails(appId);
	}
	
	public List<Application> getApplicationMapDetailsForJenkins(int appId) {
		return applicationDao.getApplicationMapDetailsForJenkins(appId);
	}

	public List<Map<String, Object>> getApplicationSystemMapService(String appId) {
		 // TODO Auto-generated method stub
		 return applicationDao.getApplicationSystemMapService(appId);
	}

	public List<Application> getApplicationListSpecificSystemApi(String systemName) {
		// TODO Auto-generated method stub
		return applicationDao.getApplicationListSpecificSystemApi(systemName);
	}

	public String getMapIdByAppId(String appId, String string) {
		// TODO Auto-generated method stub
		return applicationDao.getMapIdByAppId(appId,string);
	}
	
	public List<Map<String, Object>> getApplicationSystemMap(String appId, String envId, String suiteId, String bedId) {
		// TODO Auto-generated method stub
		return applicationDao.getApplicationSystemMap(appId, envId, suiteId, bedId);
	}
	

	public List<Map<String, Object>> getApplicationSystemMapBasedOnMapId(String mapId) {
		// TODO Auto-generated method stub
		return applicationDao.getApplicationSystemMapBasedOnMapId(mapId);
	}

	public List<Map<String, Object>> getApplicationSystemMapService(String appId, String systemApiName) {
		// TODO Auto-generated method stub
		return applicationDao.getApplicationSystemMapService(appId,systemApiName);
	}

	public String isNameExistChkByAjaxCall(int id, String name) {
		return applicationDao.isNameExistChkByAjaxCall(id, name);
	}

	@Override
	public void deleteData(String appIds) {
		// TODO Auto-generated method stub
		applicationDao.deleteData(appIds);
	}

	public List<Application> getApplicationListToCreateTestCase() {
		return applicationDao.getApplicationListToCreateTestCase();
	}


	@Override
	public HashMap getDefaultActiveEnvIdSuiteIdBedIdByAppId(String appId, String systemName) {
		return applicationDao.getDefaultActiveEnvIdSuiteIdBedIdByAppId(appId, systemName);
	}

	public Map<String, String> getAPIList() {
		return applicationDao.getAPIList();
	}

	public Map<String, String> getDepartmentList() {
		return applicationDao.getDepartmentList();
	}
	
	public String disableRecordForApp(String mapids) {
		  return applicationDao.disableRecordForApp(mapids);
	}
}
