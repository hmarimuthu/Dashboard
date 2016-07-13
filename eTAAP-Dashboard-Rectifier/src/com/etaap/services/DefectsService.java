package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.etaap.domain.Defects;

public interface DefectsService {

	public List<Defects> getDetails(HashMap<String, Object> params);

	public void insertData(List<Defects> defectList);

	public boolean isRecordAvail(Map params);
}
