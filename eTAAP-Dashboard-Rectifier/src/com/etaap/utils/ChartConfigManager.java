package com.etaap.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

public class ChartConfigManager {
	
	private JSONObject jsonObject = null;
//	private String jsonPath = "Charts.json"; - Not needed
		
	public ChartConfigManager(ServletContext servletContext){
		FileReader reader = null;
		try {			
			//This code will fetch the file from the current class folder
			String path=Thread.currentThread().getContextClassLoader().getResource("Charts.json").getPath();
			File jsonFile = new File(path);

			reader = new FileReader(jsonFile);
			JSONParser jsonParser = new JSONParser();
			jsonObject = (JSONObject) jsonParser.parse(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(reader != null) {
					reader.close(); 
				}
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	public JSONObject getChartInfo(String chartName){		
		JSONObject obj = (JSONObject)jsonObject.get(chartName);
		
		return obj;
	}
}
