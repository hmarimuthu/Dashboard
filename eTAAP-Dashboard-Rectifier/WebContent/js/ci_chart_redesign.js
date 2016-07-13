$(function () {
	loadCICharts(jsonObj);
});

function loadCICharts(response) {
	
	var jsonResult = jQuery.parseJSON(response);
	if (jsonResult == null) {
		alert("Database not found !!");
		return;
	}
	var noDataMsg = "<div style='margin:180px 0px 0px 180px;color:#333333;font-size:18px;fill:#333333;width:716px;'>No data available for this Quarter. <br/> To see the data - please select a different Quarter</div>";
	var chartColors = ['#a9ce8e', '#ff6666', '#ffcc66'];
	for(var i = 0; i<jsonResult.length; i++) {
		var title = jsonResult[i].title;		
		if(title === "CI Results") {
			constructColumnChart("#ci-chart-container", '', 780, 380, chartColors, 
					jsonResult[i].series, jsonResult[i].categories, 'Build Date', 'Test Case Results', noDataMsg);
		}
		var noDataMsg1 = "No data available for this Quarter. <br/> To see the data - please select a different Quarter";
		
		var series = [{
	        	name: ( function () {
		        		return 'Severity';
		        	}()),
		            showInLegend: true,
		            data: jsonResult[i].chart_aggregate,
		            center: [170, 100]
		        }];
		if(title === "Severity"){
			constructPieChart("#severity-chart-container", "#addSeverityText", title, 396, 380, chartColors,
					totalCount, jsonResult[i].chart_aggregate, jsonResult[i].chart_drillDown, css_constructPieChart['pieChart_defects'].noDataAvail,series);
		}
		if(title === "Status"){
			var totalCount = jsonResult[i].total_Count;
			constructPieChart("#status-chart-container", "#addPriorityText", title, 396, 380,chartColors,
					totalCount, jsonResult[i].chart_aggregate, jsonResult[i].chart_drillDown,css_constructPieChart['pieChart_defects'].noDataAvail,series);
		}
	}
}