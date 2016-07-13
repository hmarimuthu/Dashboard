package com.etaap.utils.enums;

import com.etaap.utils.ColorGenerator;

public enum PriorityColor {
	PRIORITY_NAME("");

	private String priority;

	private PriorityColor(String p) {
		priority = p;
	}

	public String getColorCode(String p) {
		priority = p;
		return getColorCode();
	}

	public String getColorCode() {
		String colorCode = null;

		switch(priority) {
			case "P1: High":
				colorCode = "#e6785d";
				break;
			case "P2: Medium":
				colorCode = "#f8de6e";
				break;
			case "P3: Low":
				colorCode = "#00cccc";
				break;
			default:
				colorCode = ColorGenerator.getColor();
		}

		return colorCode;
	}
}
