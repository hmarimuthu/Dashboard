package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.UserStoryDao;
import com.etaap.domain.UserStory;

public class UserStoryServiceImpl implements UserStoryService {

	@Autowired
	UserStoryDao userStoryDao;

	public List<UserStory> getDetails(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		return userStoryDao.getDetails(params);
	}

	public void insertData(Map<String, List<UserStory>> userStoryMap) {
		// TODO Auto-generated method stub
		userStoryDao.insertData(userStoryMap);
	}
}