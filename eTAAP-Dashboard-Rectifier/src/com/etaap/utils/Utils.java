package com.etaap.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.text.ParseException;

import com.etaap.domain.TimePeriod;

public class Utils {

	static private ArrayList month_list = new ArrayList();
	static {
		month_list.add("Jan");
		month_list.add("Feb");
		month_list.add("Mar");
		month_list.add("Apr");
		month_list.add("May");
		month_list.add("Jun");
		month_list.add("Jul");
		month_list.add("Aug");
		month_list.add("Sep");
		month_list.add("Oct");
		month_list.add("Nov");
		month_list.add("Dec");
	}
	
	public static List<TimePeriod> getTimePeriod(int monthId) {
		int quarter_start_month = monthId;
		ArrayList quarter1 = new ArrayList();
		ArrayList quarter2 = new ArrayList();
		ArrayList quarter3 = new ArrayList();
		ArrayList quarter4 = new ArrayList();

		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				quarter1.add(month_list.get(quarter_start_month - 1));
			}
			if (i == 1) {
				if (quarter_start_month < 12)
					quarter1.add(month_list.get(quarter_start_month));
				else
					quarter1.add(month_list.get(0));
			}
			if (i == 2) {
				if ((quarter_start_month + 1) < 12)
					quarter1.add(month_list.get(quarter_start_month + 1));
				else if (quarter_start_month + 1 == 12)
					quarter1.add(month_list.get(0));
				else
					quarter1.add(month_list.get(1));
			}
		}

		int temp = quarter_start_month + 3;
		if (temp == 13)
			temp = 1;
		else if (temp == 14)
			temp = 2;
		else if (temp == 15)
			temp = 3;

		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				quarter2.add(month_list.get(temp - 1));
			}
			if (i == 1) {
				if (temp < 12)
					quarter2.add(month_list.get(temp));
				else
					quarter2.add(month_list.get(0));
			}
			if (i == 2) {
				if ((temp + 1) < 12)
					quarter2.add(month_list.get(temp + 1));
				else if (temp + 1 == 12)
					quarter2.add(month_list.get(0));
				else
					quarter2.add(month_list.get(1));
			}
		}

		temp = quarter_start_month + 6;
		if (temp == 13)
			temp = 1;
		else if (temp == 14)
			temp = 2;
		else if (temp == 15)
			temp = 3;
		else if (temp == 16)
			temp = 4;
		else if (temp == 17)
			temp = 5;
		else if (temp == 18)
			temp = 6;

		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				quarter3.add(month_list.get(temp - 1));
			}
			if (i == 1) {
				if (temp < 12)
					quarter3.add(month_list.get(temp));
				else
					quarter3.add(month_list.get(0));
			}
			if (i == 2) {
				if ((temp + 1) < 12)
					quarter3.add(month_list.get(temp + 1));
				else if (temp + 1 == 12)
					quarter3.add(month_list.get(0));
				else
					quarter3.add(month_list.get(1));
			}
		}

		temp = quarter_start_month + 9;
		if (temp == 13)
			temp = 1;
		else if (temp == 14)
			temp = 2;
		else if (temp == 15)
			temp = 3;
		else if (temp == 16)
			temp = 4;
		else if (temp == 17)
			temp = 5;
		else if (temp == 18)
			temp = 6;
		else if (temp == 19)
			temp = 7;
		else if (temp == 20)
			temp = 8;
		else if (temp == 21)
			temp = 9;

		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				quarter4.add(month_list.get(temp - 1));
			}
			if (i == 1) {
				if (temp < 12)
					quarter4.add(month_list.get(temp));
				else
					quarter4.add(month_list.get(0));
			}
			if (i == 2) {
				if ((temp + 1) < 12)
					quarter4.add(month_list.get(temp + 1));
				else if (temp + 1 == 12)
					quarter4.add(month_list.get(0));
				else
					quarter4.add(month_list.get(1));
			}
		}

		/*System.out.println(quarter1);
		System.out.println(quarter2);
		System.out.println(quarter3);
		System.out.println(quarter4);*/

		Calendar calendar = Calendar.getInstance();
		int current_month = calendar.get(Calendar.MONTH);
		//String next_year = (calendar.get(Calendar.YEAR) + 1) + "";
		String next_year = calendar.get(Calendar.YEAR) + "";
		next_year = next_year.substring(2, next_year.length());
		//String current_year = calendar.get(Calendar.YEAR) + "";
		String current_year = (calendar.get(Calendar.YEAR) - 1) + "";
		current_year = current_year.substring(2, current_year.length());

		ArrayList quarter_list = new ArrayList();
		if (quarter1.contains(month_list.get(current_month))) {
			quarter_list.add("Q1 - FY" + next_year);
			quarter_list.add("Q4 - FY" + current_year);
			quarter_list.add("Q3 - FY" + current_year);
			quarter_list.add("Q2 - FY" + current_year);
		} else if (quarter2.contains(month_list.get(current_month))) {
			quarter_list.add("Q2 - FY" + next_year);
			quarter_list.add("Q1 - FY" + next_year);
			quarter_list.add("Q4 - FY" + current_year);
			quarter_list.add("Q3 - FY" + current_year);
		} else if (quarter3.contains(month_list.get(current_month))) {
			quarter_list.add("Q3 - FY" + next_year);
			quarter_list.add("Q2 - FY" + next_year);
			quarter_list.add("Q1 - FY" + next_year);
			quarter_list.add("Q4 - FY" + current_year);
		} else {
			quarter_list.add("Q4 - FY" + next_year);
			quarter_list.add("Q3 - FY" + next_year);
			quarter_list.add("Q2 - FY" + next_year);
			quarter_list.add("Q1 - FY" + next_year);
		}

		String strt_dt = "";
		String end_dt = "";
		HashMap<Integer, List> hm = new HashMap<Integer, List>();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, current_month);

		for (int k = 0; k < 12; k++) {
			ArrayList al = new ArrayList();
			if (c.get(Calendar.MONTH) == 11) {
				strt_dt = c.get(Calendar.YEAR) + "-12-0" + c.getActualMinimum(Calendar.DAY_OF_MONTH) + " 00:00:00";
				end_dt = c.get(Calendar.YEAR) + "-12-" + c.getActualMaximum(Calendar.DAY_OF_MONTH) + " 23:59:59";
				al.add(strt_dt);
				al.add(end_dt);
				hm.put(12, al);
			} else if (c.get(Calendar.MONTH) == 10) {
				strt_dt = c.get(Calendar.YEAR) + "-11-0" + c.getActualMinimum(Calendar.DAY_OF_MONTH) + " 00:00:00";
				end_dt = c.get(Calendar.YEAR) + "-11-" + c.getActualMaximum(Calendar.DAY_OF_MONTH) + " 23:59:59";
				al.add(strt_dt);
				al.add(end_dt);
				hm.put(11, al);
			} else if (c.get(Calendar.MONTH) == 9) {
				strt_dt = c.get(Calendar.YEAR) + "-10" + "-0" + c.getActualMinimum(Calendar.DAY_OF_MONTH) + " 00:00:00";
				end_dt = c.get(Calendar.YEAR) + "-10" + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH) + " 23:59:59";
				al.add(strt_dt);
				al.add(end_dt);
				hm.put(10, al);
			} else {
				strt_dt = c.get(Calendar.YEAR) + "-0" + (c.get(Calendar.MONTH) + 1) + "-0" + c.getActualMinimum(Calendar.DAY_OF_MONTH) + " 00:00:00";
				end_dt = c.get(Calendar.YEAR) + "-0" + (c.get(Calendar.MONTH) + 1) + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH) + " 23:59:59";
				al.add(strt_dt);
				al.add(end_dt);
				hm.put(c.get(Calendar.MONTH) + 1, al);
			}
			c.add(Calendar.MONTH, -1);
		}

		//System.out.println("hm ::::::::::::::::::::::::: " +   hm);
		List<TimePeriod> timePeriodList = new ArrayList<TimePeriod>();
		for (int i = 0; i < 4; i++) {
			String quarter_name = quarter_list.get(i).toString();
			String month_name = "";
			if (quarter_name.startsWith("Q1"))
				month_name = quarter1.get(0).toString();
			else if (quarter_name.startsWith("Q2"))
				month_name = quarter2.get(0).toString();
			else if (quarter_name.startsWith("Q3"))
				month_name = quarter3.get(0).toString();
			else
				month_name = quarter4.get(0).toString();

			TimePeriod tp = new TimePeriod();
			ArrayList a1 = null;
			ArrayList a2 = null;

			if (month_name == "Jan") {
				a1 = (ArrayList) hm.get(1);
				a2 = (ArrayList) hm.get(3);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a2_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "Feb") {
				a1 = (ArrayList) hm.get(2);
				a2 = (ArrayList) hm.get(4);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a2_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "Mar") {
				a1 = (ArrayList) hm.get(3);
				a2 = (ArrayList) hm.get(5);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a2_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "Apr") {
				a1 = (ArrayList) hm.get(4);
				a2 = (ArrayList) hm.get(6);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a2_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "May") {
				a1 = (ArrayList) hm.get(5);
				a2 = (ArrayList) hm.get(7);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a2_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "Jun") {
				a1 = (ArrayList) hm.get(6);
				a2 = (ArrayList) hm.get(8);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a2_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "Jul") {
				a1 = (ArrayList) hm.get(7);
				a2 = (ArrayList) hm.get(9);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a2_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "Aug") {
				a1 = (ArrayList) hm.get(8);
				a2 = (ArrayList) hm.get(10);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a2_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "Sep") {
				a1 = (ArrayList) hm.get(9);
				a2 = (ArrayList) hm.get(11);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a2_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "Oct") {
				a1 = (ArrayList) hm.get(10);
				a2 = (ArrayList) hm.get(12);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a2_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "Nov") {
				a1 = (ArrayList) hm.get(11);
				a2 = (ArrayList) hm.get(1);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a1_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			} else if (month_name == "Dec") {
				a1 = (ArrayList) hm.get(12);
				a2 = (ArrayList) hm.get(2);
				int a1_year = Integer.parseInt(a1.get(1).toString().substring(0, 4));
				int a2_year = Integer.parseInt(a2.get(1).toString().substring(0, 4));
				int year = 0;
				if (a1_year != a2_year)
					year = a1_year + 1;
				else
					year = a1_year;

				tp.setPeriodId(i);
				tp.setPeriodName(quarter_name);
				tp.setStartDt(String.valueOf(a1.get(0)));
				tp.setEndDt(year + String.valueOf(a2.get(1)).substring(4));
			}
			timePeriodList.add(tp);
		}

		return timePeriodList;
	}
	
	public static String getFirstDayOfPrevTwoMonths() {
	//	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -2);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(cal.getTime());
	}

	public static String _getDate(boolean isCurrent, String timePeriodString, String dateFormat) {
		String date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(/* "yyyy-MM-dd" */dateFormat);
			Calendar cal2 = Calendar.getInstance();
			date = format.format(cal2.getTime());
		} catch (Exception e) {
			e.getMessage();
		}
		return date;
	}

	public static String getDate(boolean isCurrent, String timePeriodString, String dateFormat) {
		String date = null;
		try {
			if (dateFormat != null && !dateFormat.equalsIgnoreCase("")) {
				SimpleDateFormat format = new SimpleDateFormat(/* "yyyy-MM-dd" */dateFormat);
				Calendar cal2 = Calendar.getInstance();
				if (isCurrent) {
					cal2.set(Calendar.DAY_OF_MONTH, 1);
					date = format.format(cal2.getTime());
				}
				if (!isCurrent && timePeriodString != null) {
					if(timePeriodString.equalsIgnoreCase("PREVIOUS_DAY")){
						cal2.add(Calendar.DATE, -1);
						date = format.format(cal2.getTime());
					}
					if (timePeriodString.equalsIgnoreCase("PRE_ONE_WEEK")) {
						cal2.add(Calendar.DATE, -7);
						date = format.format(cal2.getTime());
						System.out.println("date" +date);
					}
					if (timePeriodString.equalsIgnoreCase("PRE_WEEK_DAY")) {
						cal2.add(Calendar.DATE, -8);
						date = format.format(cal2.getTime());
						System.out.println("date" +date);
					}
					if (timePeriodString.equalsIgnoreCase("PRE_ONE_MONTH")) {
						cal2.add(Calendar.DATE, -37);
						date = format.format(cal2.getTime());
						System.out.println("date" +date);
					}
					
					if (timePeriodString.equalsIgnoreCase("WEEK")) {
						cal2.add(Calendar.DAY_OF_WEEK_IN_MONTH, 0);
						cal2.set(Calendar.DAY_OF_WEEK, 1);
						date = format.format(cal2.getTime());
					}
					if (timePeriodString.equalsIgnoreCase("MONTH")) {
						cal2.add(Calendar.DAY_OF_MONTH, -1);
						cal2.set(Calendar.DAY_OF_MONTH, 1);
						date = format.format(cal2.getTime());
					}
					if (timePeriodString.equalsIgnoreCase("MONTH_DAYOFMONTH")) {
						cal2.add(Calendar.MONTH, -2);
						cal2.set(Calendar.DAY_OF_MONTH, 1);
						// cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
						date = format.format(cal2.getTime());
					}
					if (timePeriodString.equalsIgnoreCase("MONTH_DAYOFMONTH_ADD_1_AS_VALUE")) {
						cal2.add(Calendar.MONTH, -1);
						cal2.set(Calendar.DAY_OF_MONTH, 1);
						date = format.format(cal2.getTime());
					}
					if (timePeriodString.equalsIgnoreCase("MONTH_DAYOFMONTH_ADD_2_AS_VALUE")) {
						cal2.add(Calendar.MONTH, -2);
						cal2.set(Calendar.DAY_OF_MONTH, 1);
						date = format.format(cal2.getTime());
					}
					if (timePeriodString.equalsIgnoreCase("MONTH_DAYOFMONTH_LAST_DAY")) {
						cal2.add(Calendar.MONTH, -1);
						cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
						// cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
						date = format.format(cal2.getTime());
					}
					if (timePeriodString.equalsIgnoreCase("MONTH_DAYOFMONTH_LAST_DAY_MONTH_MINUS2")) {
						cal2.add(Calendar.MONTH, -1);
						// cal2.set(Calendar.DAY_OF_MONTH, 1);
						cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
						date = format.format(cal2.getTime());
					}
					if (timePeriodString.equalsIgnoreCase("MONTH_DAYOFMONTH_LAST_DAY_Month2Minus")) {
						cal2.add(Calendar.MONTH, -2);
						cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
						// cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
						date = format.format(cal2.getTime());
					}
					if (timePeriodString.equalsIgnoreCase("MONTH_DAYOFMONTH_LAST_DAY_CURRENT_Month")) {
						cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
						date = format.format(cal2.getTime());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getMonthName(int val) {
		String monthName = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, val);
			java.util.Date d = new java.util.Date(cal.getTimeInMillis());
			monthName = new SimpleDateFormat("MMM").format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return monthName;
	}

	public static Map<String, String> getDepartmentList() {
		Map<String, String> departmentList = new LinkedHashMap<String, String>();
		departmentList.put("1", "Dev");
		departmentList.put("2", "QA");
		departmentList.put("3", "Operations");

		return (new TreeMap<String, String>(departmentList));
	}
	
	public static String getDate(String dateStr, String inputFormat, String outputFormat) {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
//		SimpleDateFormat formatter1 = new SimpleDateFormat("dd MMM yy");
//		String dateInString = "2013-06-07";
	
		String retDate = null;
		
		SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
		SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
		
		try {
	 
			java.util.Date date = (java.util.Date) inputFormatter.parse(dateStr);
	//		System.out.println(date);
			retDate = outputFormatter.format(date);
		}
		catch(ParseException e) {
			retDate = dateStr;
		}
	 
		return retDate;
	}
	
	public static void sortDates(List<java.util.Date> dateList) {
		Collections.sort(dateList);
	}
	
	public static List<String> sortDatesInStringFormat(List<String> dateInStringFormatList) throws ParseException {
		List<java.util.Date> datesToSortList = new ArrayList<java.util.Date>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		for(String dateStr : dateInStringFormatList) {
			java.util.Date dateToSort = formatter.parse(dateStr);
			datesToSortList.add(dateToSort);
		}
		Collections.sort(datesToSortList);
		
		List<String> retList = new ArrayList<String>();
		for(java.util.Date dateObj : datesToSortList) {
			String dateSortedStr = formatter.format(dateObj);
			System.out.println("NBNBNBNBNBN "+dateSortedStr);
			retList.add(dateSortedStr);
		}
		return retList;
		
		
	}
	
	public static String getDate(java.util.Date date) throws ParseException {
		SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String retVal = inputFormatter.format(date);
		return retVal;
	}
	
	public static int getNumberOfWorkingDays(long startDatetime, long endDatetime) {
		java.util.Date startDate = new java.util.Date(startDatetime);
		java.util.Date endDate = new java.util.Date(endDatetime);
		
	    Calendar startCal = Calendar.getInstance();
	    
	    startCal.setTime(startDate);        

	    Calendar endCal = Calendar.getInstance();
	    endCal.setTime(endDate);

	    int workDays = 0;

	    //Return 0 if start and end are the same
	    if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
	        return 0;
	    }

	    if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
	        startCal.setTime(endDate);
	        endCal.setTime(startDate);
	    }

	    do {
	        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
	            ++workDays;
	        }
            //include start date
	        startCal.add(Calendar.DAY_OF_MONTH, 1);
	    } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

	    return workDays;
	}
	
	public static boolean isSaturdaySunday(long dateTime) {
		boolean saturdaySunday = false;
		java.util.Date dateTimeObj = new java.util.Date(dateTime);
		
	    Calendar startCal = Calendar.getInstance();
	    startCal.setTime(dateTimeObj);        
		
	    if((startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
	    	saturdaySunday = true;
	    }
	    return saturdaySunday;
	}
	
	/**@param minDateStr Format yyyy-MM-dd
	 * @param maxDateStr Format yyyy-MM-dd
	 * @return List<String> Each element is formatted String as - yyyy-MM-dd
	 * */
	public static List<String> getDates(String minDateStr, String maxDateStr) {
		List<String> retVal = new ArrayList<String>();
		
		System.out.println("*********Min Date "+minDateStr);
		System.out.println("*********Max Date "+maxDateStr);
		
	    String dateArr1[] = minDateStr.split("-");
	    String year1 = dateArr1[0];
	    String month1 = dateArr1[1];
	    String date1 = dateArr1[2];
	    
	    int yearInt1 = Integer.parseInt(year1);
	    int monthInt1 = Integer.parseInt(month1);
	    monthInt1--;
	    int dateInt1 = Integer.parseInt(date1);
	    
	    Calendar startCal = Calendar.getInstance();
	    startCal.set(yearInt1, monthInt1, dateInt1);
	    
	    
	    String dateArr2[] = maxDateStr.split("-");
	    String year2 = dateArr2[0];
	    String month2 = dateArr2[1];
	    String date2 = dateArr2[2];
	    
	    int yearInt2 = Integer.parseInt(year2);
	    int monthInt2 = Integer.parseInt(month2);
	    monthInt2--;
	    int dateInt2 = Integer.parseInt(date2);
	    
	    Calendar endCal = Calendar.getInstance();
	    endCal.set(yearInt2, monthInt2, dateInt2);
	    
	    do {
        	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        	String formatted = format1.format(startCal.getTime());
        	System.out.println("************Cal Date "+formatted);
        	retVal.add(formatted);
	        startCal.add(Calendar.DATE, 1);
	    } while ( (startCal.getTime().before(endCal.getTime())) || (startCal.getTime().equals(endCal.getTime())) );
	    
	    return retVal;
	}
	
	
	public static List<Long> getDays(long startDatetime, long endDatetime) {
		List<Long> retVal = new ArrayList<Long>();
		java.util.Date startDate = new java.util.Date(startDatetime);
		java.util.Date endDate = new java.util.Date(endDatetime);
		
	    Calendar startCal = Calendar.getInstance();
	    startCal.setTime(startDate);        

	    Calendar endCal = Calendar.getInstance();
	    endCal.setTime(endDate);

//	    int workDays = 0;

	    //Return 0 if start and end are the same
	    if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
	        return retVal;
	    }

	    if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
	        startCal.setTime(endDate);
	        endCal.setTime(startDate);
	    }

	    do {
//	        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
	        	retVal.add(startCal.getTimeInMillis());
//	            ++workDays;
//	        }
		    //include start date
	        startCal.add(Calendar.DAY_OF_MONTH, 1);
	    } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

/*	    do {
		       //excluding start date
		        startCal.add(Calendar.DAY_OF_MONTH, 1);
		        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
		        	retVal.add(startCal.getTimeInMillis());
//		            ++workDays;
		        }
		    } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date
		    */
	    
	    return retVal;
	}
	
	
	public static List<Long> getWorkingDays(long startDatetime, long endDatetime) {
		List<Long> retVal = new ArrayList<Long>();
		java.util.Date startDate = new java.util.Date(startDatetime);
		java.util.Date endDate = new java.util.Date(endDatetime);
		
	    Calendar startCal = Calendar.getInstance();
	    startCal.setTime(startDate);        

	    Calendar endCal = Calendar.getInstance();
	    endCal.setTime(endDate);

//	    int workDays = 0;

	    //Return 0 if start and end are the same
	    if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
	        return retVal;
	    }

	    if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
	        startCal.setTime(endDate);
	        endCal.setTime(startDate);
	    }

	    do {
	        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
	        	retVal.add(startCal.getTimeInMillis());
//	            ++workDays;
	        }
		    //include start date
	        startCal.add(Calendar.DAY_OF_MONTH, 1);
	    } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

/*	    do {
		       //excluding start date
		        startCal.add(Calendar.DAY_OF_MONTH, 1);
		        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
		        	retVal.add(startCal.getTimeInMillis());
//		            ++workDays;
		        }
		    } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date
		    */
	    
	    return retVal;
	}
	
	public static java.util.Date getDate(String dateStr, String inputFormat) throws ParseException {
/*		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateInString = "2015-08-05 02:18:08";
		*/
		
		SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
		java.util.Date date = formatter.parse(dateStr);
		
//		System.out.println(date);
//		System.out.println(formatter.format(date));
		return date;
	}
	
/*	public static void main(String args[]) {
		getDate(null, null);
	}
*/	
	
	public static Integer[] getRefinedDataArr(Integer[] dataArr) {
		Integer[] dataArrVal = dataArr;
		Integer[] dataArrValToReturn = null;
		int monthCount = 0;
		if (dataArrVal != null && dataArrVal.length > 0) {
			dataArrValToReturn = new Integer[dataArr.length];
			for (Integer intval : dataArrVal) {
				Integer value;
				if (intval == null || intval.equals("null")) {
					value = 0;
				} else {
					value = intval;
				}

				dataArrValToReturn[monthCount] = value;
				monthCount++;
			}
		}
		
		return dataArrValToReturn;
	}
	
	public static boolean isNull(String inputString) {
		if(inputString != null && !inputString.equals("") && !inputString.equals("null")) {
			return false;
		} else {
			return true;
		}
	}
}
