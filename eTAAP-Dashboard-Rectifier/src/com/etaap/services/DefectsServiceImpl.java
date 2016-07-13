package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.DefectsDao;
import com.etaap.domain.Defects;

public class DefectsServiceImpl implements DefectsService{

	@Autowired
	DefectsDao defectsDao;

	public List<Defects> getDetails(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		return defectsDao.getDetails(params);
	}

	public void insertData(List<Defects> defectList) {
		// TODO Auto-generated method stub
		defectsDao.insertData(defectList);
	}

	@Override
	public boolean isRecordAvail(Map params) {
		// TODO Auto-generated method stub
		return defectsDao.isRecordAvail(params);
	}
}