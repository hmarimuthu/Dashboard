package com.etaap.beans;

public class Severity {
	String name;
	int y;
	String color;
	String drilldown;
	String id;
	Object data;
	boolean showInLegend;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return y;
	}
	public void setCount(int count) {
		this.y = count;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDrilldown() {
		return drilldown;
	}
	public void setDrilldown(String drilldown) {
		this.drilldown = drilldown;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public boolean isShowInLegend() {
		return showInLegend;
	}
	public void setShowInLegend(boolean showInLegend) {
		this.showInLegend = showInLegend;
	}
}
