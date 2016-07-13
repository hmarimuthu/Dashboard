package com.etaap.utils;

import java.awt.Color;
import java.util.Random;

public class ColorGenerator {

	
	//public static
	
	public static String getColor() {
		String colorCodesVal = null;
		Random random = new Random();
		
		float r = random.nextFloat();
		float g = random.nextFloat();
		float b = random.nextFloat();
		
		Color randomColor = new Color(r, g, b);
		//randomColor.getHSBColor(r, g, b).
		colorCodesVal = Integer.toHexString(randomColor.getRGB());
		colorCodesVal = colorCodesVal.substring(2, colorCodesVal.length());
		System.out.println("Color Codes === " + colorCodesVal);
		return "#"+colorCodesVal;
	}
	
	
	
	public static void main(String args[]){
		
		Random random = new Random();
		
		float r = random.nextFloat();
		float g = random.nextFloat();
		float b = random.nextFloat();
		
		Color randomColor = new Color(r, g, b);
		//randomColor.getHSBColor(r, g, b).
		String val = Integer.toHexString(randomColor.getRGB());
		val = val.substring(2, val.length());
		
		
	}
	
}
