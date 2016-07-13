package com.etaap.utils.enums;

import com.etaap.utils.ColorGenerator;

public enum StatusColor {
	STATUS_NAME("");

	private String status;

	private StatusColor(String s) {
		status = s;
	}

	public String getColorCode(String s) {
		status = s;
		return getColorCode();
	}

	public String getColorCode() {
		String colorCode = null;

		switch(status) {
			case "Assigned":
				colorCode = "#ff6666";
				break;
			case "Closed":
				colorCode = "#a9ce8e";
				break;
			case "Deferred":
				colorCode = "#996699";
				break;
			case "In Progress":
				colorCode = "#f8de6e";
				break;
			case "New":
				colorCode = "#ff6666";
				break;
			case "Open":
				colorCode = "#ff6666";
				break;
			case "Resolved":
				colorCode = "#008cc4";
				break;
			case "Verify":
				colorCode = "#008cc4";
				break;
			default:
				colorCode = ColorGenerator.getColor();
		}

		return colorCode;
	}

}
