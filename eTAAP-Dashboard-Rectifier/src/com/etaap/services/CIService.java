package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.etaap.domain.CI;

public interface CIService {

	public void insertData(List<CI> ciList);

	public List<CI> getDetails(HashMap<String, String> params);

	public boolean isRecordAvail(Map params);
}
