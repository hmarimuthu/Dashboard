package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.CIDao;
import com.etaap.domain.CI;

public class CIServiceImpl implements CIService {

	@Autowired
	CIDao ciDao;

	public void insertData(List<CI> ciList) {
		ciDao.insertData(ciList);
	}

	public List<CI> getDetails(HashMap<String, String> params) {
		return ciDao.getDetails(params);
	}

	@Override
	public boolean isRecordAvail(Map params) {
		// TODO Auto-generated method stub
		return ciDao.isRecordAvail(params);
	}
	
}
