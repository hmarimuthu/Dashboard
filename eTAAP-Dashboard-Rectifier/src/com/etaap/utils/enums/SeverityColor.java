package com.etaap.utils.enums;

import com.etaap.utils.ColorGenerator;

public enum SeverityColor {
	SEVERITY_NAME("");

	private String severity;

	private SeverityColor(String s) {
		severity = s;
	}

	public String getColorCode(String s) {
		severity = s;
		return getColorCode();
	}

	public String getColorCode() {
		String colorCode = null;

		switch(severity) {
			case "S1-Critical":
				colorCode = "#e6785d";
				break;
			case "S2-Major":
				colorCode = "#f8de6e";
				break;
			case "S3-Medium":
				colorCode = "#00cccc";
				break;
			case "S4-Minor":
				colorCode = "#008cc4";
				break;
			default:
				colorCode = ColorGenerator.getColor();
		}

		return colorCode;
	}
}
