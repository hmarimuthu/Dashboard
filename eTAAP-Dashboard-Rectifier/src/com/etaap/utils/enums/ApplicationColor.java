package com.etaap.utils.enums;

import com.etaap.utils.ColorGenerator;

public enum ApplicationColor {
	INDEX("");

	private String index;

	private ApplicationColor(String id) {
		index = id;
	}

	public String getColorCode(String id) {
		index = id;
		return getColorCode();
	}
	
	public String getColumnColorCode(String id) {
		index = id;
		return getColumnColorCode();
	}

	public String getColorCode() {
		String colorCode = null;

		switch(index) {
			case "1":
				colorCode = "#f7a98e";
				break;
			case "2":
				colorCode = "#996699";
				break;
			case "3":
				colorCode = "#94c2ce";
				break;
			case "4":
				colorCode = "#a9ce8e";
				break;
			case "5":
				colorCode = "#4d979a";
				break;
			case "6":
				colorCode = "#fe7877";
				break;
			case "7":
				colorCode = "#e06089";
				break;
			case "8":
				colorCode = "#2ebae6";
				break;
			case "9":
				colorCode = "#ff6666";
				break;
			case "10":
				colorCode = "#a07011";
				break;
			case "11":
				colorCode = "#ffcc66";
				break;
			case "12":
				colorCode = "#e6785d";
				break;
			case "13":
				colorCode = "#f8de6e";
				break;
			case "14":
				colorCode = "#00cccc";
				break;
			case "15":
				colorCode = "#008cc4";
				break;
			default:
				colorCode = ColorGenerator.getColor();
		}

		return colorCode;
	}
	
	public String getColumnColorCode() {
		String colorCode = null;

		switch(index) {
			case "1":
				colorCode = "#FF8080";
				break;
			case "2":
				colorCode = "#008000";
				break;
			case "3":
				colorCode = "#f7a98e";
				break;
			case "4":
				colorCode = "#996699";
				break;
			case "5":
				colorCode = "#94c2ce";
				break;
			case "6":
				colorCode = "#a9ce8e";
				break;
			case "7":
				colorCode = "#4d979a";
				break;
			default:
				colorCode = ColorGenerator.getColor();
		}

		return colorCode;
	}
}
