package com.etaap.beans;

import java.util.ArrayList;
import java.util.List;

public class Series {
	private String name;
	private String color;
	private String pointWidth;
	
	private List<Integer> data = new ArrayList<Integer>();
	
	public String getPointWidth() {
		return pointWidth;
	}
	public void setPointWidth(String pointWidth) {
		this.pointWidth = pointWidth;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Integer> getData() {
		return data;
	}
	public void setData(List<Integer> data) {
		this.data = data;
	}
	
}
