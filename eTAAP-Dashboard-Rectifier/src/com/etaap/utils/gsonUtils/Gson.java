package com.etaap.utils.gsonUtils;

import com.etaap.scheduler.beans.SchedulerService;


public class Gson {

	public static String getGsonString(Object c) throws Exception
	{
		String gsonString = null;
		if(c!=null)
		{
			com.google.gson.Gson gson = new com.google.gson.Gson();
			gsonString = gson.toJson(c,c.getClass());
		}
		return  gsonString;
	}

	public static Object getGsonObject(String jsonString, Class classObject) {
		Object object = null;
		if (jsonString != null && classObject != null) {
			com.google.gson.Gson gson = new com.google.gson.Gson();
			object = gson.fromJson(jsonString, classObject);
		}
		return object;
	}
	
}
