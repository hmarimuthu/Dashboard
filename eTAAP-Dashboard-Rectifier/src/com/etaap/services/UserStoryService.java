package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.etaap.domain.UserStory;

public interface UserStoryService {

	public List<UserStory> getDetails(HashMap<String, Object> params);

	public void insertData(Map<String, List<UserStory>> userStoryMap);
}
