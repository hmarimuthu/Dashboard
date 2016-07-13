package com.etaap.beans;

import java.util.ArrayList;
import java.util.List;

public class BurndownSeries {
	private String name;
	private String color;
	private String pointWidth;
	private Marker marker;
	private int lineWidth;
	private boolean step = false;
	
	
	public class Marker {
		private int radius;
		private boolean enabled = true;
		private String symbol = "circle";
//		private String fillColor = "#FFFFFF";
		
		public int getRadius() {
			return radius;
		}

		public void setRadius(int radius) {
			this.radius = radius;
		}
		
	};
	
    public void setMarkerRadius(int radius) {
    	Marker marker = new Marker();
    	marker.setRadius(radius);
    	this.setMarker(marker);
    }
	
	private List<BurndownSeriesElement> data = new ArrayList<BurndownSeriesElement>();
	
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
	
	public List<BurndownSeriesElement> getData() {
		return data;
	}
	public void setData(List<BurndownSeriesElement> data) {
		this.data = data;
	}
	public Marker getMarker() {
		return marker;
	}
	public void setMarker(Marker marker) {
		this.marker = marker;
	}
	public int getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}
	public boolean isStep() {
		return step;
	}
	public void setStep(boolean step) {
		this.step = step;
	}
	
	
}
