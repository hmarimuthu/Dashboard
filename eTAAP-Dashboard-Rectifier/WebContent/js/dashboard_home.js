$(function () {
	var jsonResult = jQuery.parseJSON(jsonObj);

	if (jsonResult == null) {
		alert("Database not found !!");
		return;
	}
	
	for(var i = 0; i<jsonResult.length; i++) {
		var title = jsonResult[i].title;
		var totalCount = jsonResult[i].total_Count;
		
		var series = [{
        	name: ( function () {
	        		return 'Severity';
	        	}()),
	            showInLegend: true,
	            data: jsonResult[i].chart_aggregate
	            //center: [170, 100]
	        }];
		
		if(title === "Defects by Priority") {
			var chartColors = ['#f7a98e', '#996699', '#94c2ce', '#a9ce8e', '#4d979a', '#fe7877', '#e06089', '#2ebae6', '#ff6666', '#a07011'];
			var noDataMsg = "<div class='badge'></div>";
			constructPieChart("#priority_chart_container", "#addPriorityText", title, 365, 258, 
					chartColors, totalCount, jsonResult[i].chart_aggregate, jsonResult[i].chart_drillDown, noDataMsg,series);
		}
		if(title === "Defects by Severity") {
			var chartColors = ['#f7a98e', '#996699', '#94c2ce', '#a9ce8e', '#4d979a', '#fe7877', '#e06089', '#2ebae6', '#ff6666', '#a07011'];
			var noDataMsg = "<div class='badge'></div>";
			constructPieChart("#severity_chart_container", "#addSeverityText", title, 365, 258, 
					chartColors, totalCount, jsonResult[i].chart_aggregate, jsonResult[i].chart_drillDown, noDataMsg,series);
		}
		if(title === "Defects Statistics") {
			var noDataMsg = "<div class='badge'></div>";
			constructMultiLineChart("#defects_statistics_container", 'line', title, 365, 260, 
					jsonResult[i].series, jsonResult[i].categories, '', 'Defects', noDataMsg,legendsArray['multiline_defectsStatistics']);
		}
		if(title === "Test Automation Results of last build") {
			var chartColors = ['#a9ce8e', '#ff6666', '#ffcc66'];
			var noDataMsg = "<div class='badge'></div>";
			constructStackedColumnChart("#jenkins_chart_container", title, 365, 260, 
					chartColors, jsonResult[i].series, jsonResult[i].categories, '', 'Results', noDataMsg);
		}
		if(title === "Test Case Statistics") {
			var chartColors = ['#008cc4','#00cccc'];
			var noDataMsg = "<div class='no-test-cases-badge'></div>";
			constructStackedColumnChart("#tcm_dashboard_chart", title, 365, 258, 
					chartColors, jsonResult[i].series, jsonResult[i].categories, '', 'Test Cases', noDataMsg);
		}
		if(title === "Defects Life") {
			var noDataMsg = "<div class='badge'></div>";
			var yAxisTitle = 'Severity Count';
			if(jsonResult[i].isSecondQueryResult === "true") {
				yAxisTitle = 'Priority Count';
			}
			constructStackedBarChart("#defects_life_container", title, 365, 258, 
					jsonResult[i].series, jsonResult[i].categories, 'Greater than', yAxisTitle, noDataMsg);
		}
		if(title === "Sprint Velocity") {
			var chartColors = ['#FF8080', '#008000'];
			var noDataMsg = "<div class='no-user-stories-badge'></div>";
			constructColumnChart("#committed_completed_userstories_chart_container", title, 1109, 258, 
					chartColors, jsonResult[i].series, jsonResult[i].categories, '', 'Story Points', noDataMsg);
		}
		
		/*
		 * Water Tank Example Code if(title === "User Stories") { var
		 * chartInnerHTML = '<section><article style="width:95%; background:
		 * #fff;"><div class="watertank-title-container"><p class="graph-name"><span
		 * class="graph-text-left">'+title+'</span><span
		 * class="status-icon-yellow"></span></p></div><div
		 * class="iframe-container"><div class="container-circle"><div
		 * class="inner-circle"><div class="us-details"><h3 class="user-story-number"><span
		 * id="user_stories">'+jsonResult[i].status_Count+'</span></h3><div
		 * class="total-us-count"><p class="us-count-text">OUT OF</p><div
		 * class="us-horz-separator"></div><h4 class="us-count" id="total_stories">'+totalCount+'</h4></div><div
		 * class="clear"></div></div><div class="clear"></div><div
		 * class="water-level-circle" style="background:
		 * -webkit-linear-gradient(top,white 100%,purple 10%);background:
		 * -moz-linear-gradient(top,white 100%,purple 10%);background:
		 * -o-linear-gradient(top,white 100%,purple 10%);background:
		 * -ms-linear-gradient(top,white 100%,purple 10%);"></div><p class="user-story-text">User
		 * Stories Completed</p></div></div></div></article><div
		 * class="clear"></div></section>';
		 * constructWaterTankChart("#watertank-chart-container", chartInnerHTML,
		 * totalCount, jsonResult[i].status_Count); }
		 */
	}
});