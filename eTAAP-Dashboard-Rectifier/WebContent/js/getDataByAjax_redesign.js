var responseType = "json";
var env;
var suite;
var bed;
var monthArr = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

function pushtoBrowserHistory(urlParams)
{
	/*var stateObj = { foo: window.location.href + "?Jiten"};
	
	
	history.pushState(window.location.href + "?Jiten", "page 2", window.location.href + "?Jiten");*/
	
	var stateObject = {stateObj : urlParams};
	window.onpopstate = function(event) { 
		window.location.reload(); 
		/*if(event.state != null)
			{
				window.location.reload();
			}*/
		};
	history.pushState(stateObject,"",urlParams);
	//history.go(-1);
	//alert(window.location.href);
}

function setPeriodLabel(startDate,endDate)
{
	var from = startDate;
	var to = endDate;
	
	from = from.substring(0,10);
	to = to.substring(0,10);
	
	var startDateArr = from.split("-");
	var endDateArr = to.split("-");
	$('#selected-time').html(getMonthName(startDateArr[1]) + " " + startDateArr[2] + " " + startDateArr[0] + " - " + getMonthName(endDateArr[1]) + " " + endDateArr[2] + " " + endDateArr[0]);
}

function getCiResultsAjaxCall(which, mapIdParams)
{       

	if(systemApi == 'Jira') {
		getDefectsAjaxCall(which, mapIdParams);
	} else if(systemApi == 'Jenkins') {
		var locationHref = window.location.href;
		var locationHrefArr = locationHref.split("/");
		var appId = locationHrefArr[5];
		var appName = locationHrefArr[6];
		var mapIdRetrieveSplit = locationHrefArr[7].split(".");
		var from;
		var to;
		var mapId = mapIdRetrieveSplit[0];
		//New start
		var env_suite_bed = mapId;
		var env_suite_bedAry = env_suite_bed.split('_');
		
		var envIdx = env_suite_bedAry[0];
		var suiteIdx = env_suite_bedAry[1];
		var bedIdx = env_suite_bedAry[2];
		//New end
		var responseType = mapIdRetrieveSplit[1].split("?")[0];
		if(which == 'env' || which == 'suite' || which == 'bed') {
			var selectedEnv = $( "li[class='filter-submenu selected-env']");
			envIdx = selectedEnv[0].getAttribute('value');
			var selectedSuite = $( "li[class='filter-submenu selected-suite']");
			suiteIdx = selectedSuite[0].getAttribute('value');
			var selectedBed = $( "li[class='filter-submenu selected-bed']");
			bedIdx = selectedBed[0].getAttribute('value');
			env_suite_bed = envIdx+"_"+suiteIdx+"_"+bedIdx;
			
			from = $('#periodStrtDt').attr('value').substr(0,10);
			to = $('#periodEndDt').attr('value').substr(0,10);
		} else if(which == 'period') {
			$('#periodStrtDt').val(mapIdParams.getAttribute('startdt'));
			$('#periodEndDt').val(mapIdParams.getAttribute('enddt'));
			
			from = mapIdParams.getAttribute('startdt').substr(0,10);
			to = mapIdParams.getAttribute('enddt').substr(0,10);
			$('#selected-period').html(mapIdParams.innerHTML);
			setPeriodLabel(mapIdParams.getAttribute('startdt'),mapIdParams.getAttribute('enddt'));
		}
		
		var uri = contextPath+"/app/"+appId+"/"+appName+"/"+env_suite_bed+"."+responseType;
		var dataValue = "from="+from+"&to="+to+"&envId="+envIdx+"&suiteId="+suiteIdx+"&bedId="+bedIdx;
		console.log(uri+"?"+dataValue);
		
		$.ajax({url:uri,
			type: "POST", 
			data:dataValue,
			success: function(result){
				loadCICharts(result);
	        },error: function(request, error){
	        	console.log(arguments);
	        }
		});			
		pushtoBrowserHistory(uri+"/?"+dataValue);
	} else {
		getTcmResultsAjaxCall(which,mapIdParams);
	}
}

function changeApp(pathVariable,id)
{
	// needs to use jquery one
//	alert(contextPath + "/" + pathVariable + "/?" + "id=" + id + "&api="+systemApi);
	window.location.href = contextPath + "/" + pathVariable + "/?" + "id=" + id + "&api="+systemApi;
	
}

function fillCsvChat(type) {
	/*var data1 = test.data.CI;

	if(data1 === undefined) {
		pieChart();
	} else {
		ciChart(data1.buildNumberCSV,data1.passCountCSV,data1.failCountCSV,data1.skipCountCSV,data1.buildDateCSV);
	}*/
	if (type == "defects") {
		pieChart();
	} else if (type == "ci") {
		var data1 = "";
		if (test.data != undefined)
			data1 = test.data.CI;

		ciChart(data1.buildNumberCSV, data1.passCountCSV, data1.failCountCSV, data1.skipCountCSV, data1.buildDateCSV);
	}
}

function pieChart() {
	//console.log(test);

	var obj1 = "";
	var severity = "";
	var status = "";
	var title = "";
	if (test.data != undefined) {
		obj1 = test.data.Defects.defectCases;

		if (obj1.Severity != undefined) {
			severity = obj1.Severity;
			title = 'Severity';
		} else if (obj1.Priority != undefined) {
			severity = obj1.Priority;
			title = 'Priority';
		}

		status = obj1.Status;

		var severityArr = new Array();
		var keysObj = Object.keys(severity);
		for(var a=0;a < keysObj.length;a++){
			var singleArr = new Array();
			singleArr[0] = keysObj[a];
			singleArr[1] = severity[keysObj[a]];
			severityArr[a] = singleArr;
		}

		var statusArr = new Array();
		keysObj = Object.keys(status);
		for(var a=0;a < keysObj.length;a++){
			var singleArr = new Array();
			singleArr[0] = keysObj[a];
			singleArr[1] = status[keysObj[a]];
			//singleArr[2] = "#F7FE2E";
			statusArr[a] = singleArr;
		}
	}

	severityPieChart(severity, title);
	statusPieChart(status);

	/*Highcharts.getOptions().colors = Highcharts.map(Highcharts.getOptions().colors, function (color) {
        return {
            radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
            stops: [
                [0, color],
                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
            ]
        };
    });*/
	
	//var perShapeGradient = { cx: 0.5, cy: 0.3, r: 0.7 };
	
	if(severity.length > 0 && status.length > 0){
		$('#ci-chart-container').highcharts({
			 chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: 0,//null,
		            plotShadow: false,
		            width: 780,
					height: 380,
					style: {
						fontFamily:'"open_sansregular", "sans-serif","Arial"',
					},
		        },
		        /*colors : [
							{
								radialGradient: perShapeGradient,
								stops : [ [ 0, 'rgb(136, 219, 5)' ],
										[ 1, 'rgb(86, 138, 3)' ] ]
							},
							{
								//linearGradient : perShapeGradient,
								radialGradient: perShapeGradient,
								stops : [ [ 0, 'rgb(255, 120, 133)' ],
										[ 1, 'rgb(182, 49, 49)' ] ]
							}
							{
								//linearGradient : perShapeGradient,
								stops : [ [ 0, 'rgb(252, 247, 119)' ],
										[ 1, 'rgb(241, 218, 54)' ] ]
							} ],*/
		        title: {
		            text: 'Severity and Status'
		        },
		        tooltip: {
		            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                size: 200,
		                dataLabels: {
		                    enabled: true,
		                    format: '<b>{point.name}</b>: {point.y}',
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
		    					fontFamily:'"open_sansregular", "sans-serif","Arial"',
		                    }
		                }
		            }
		        },
		        series: [{
		            type: 'pie',
		            name: 'Severity',
		            center: [170, 100],
		            showInLegend: true,
		            data: severity/*,
		            color:{
						radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
						stops : [ [ 0, 'yellow' ],
								[ 1, 'red' ] ,
								[2,'green']]
						}*/
		            },{
		            	type: 'pie',
			            name: 'Status',
			            center: [550, 100],
			            showInLegend: true,
			            data: status/*,
			            color:{
							radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
							stops : [ [ 0, 'blue' ],
									[ 1, 'orange' ] ,
									[2,'red']]
							}*/
		        	}]
		 });
	} else {
		$('#ci-chart-container').highcharts({
			chart : {
				width : 780,
				height : 380,
				style: {
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				},
			},
			title : {
				text : 'No data available for this Quarter. <br/> To see the data - please select a different Quarter',
				align : 'left',
				x : 205,
				y : 180,
				floating : true
			},
			noData : {
				style : {
					fontWeight : 'bold',
					fontSize : '15px',
					color : '#303030',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				}
			}
		});
	}
}

var testsuitesObj;
var bedObj;
var data1;

function init() {
	/*test = jsonString;*/
	testsuitesObj;
	bedObj;
	console.log(test);
	if (test.data != undefined) {
		data1 = test.data.Environments;
		//alert("*****Data 1 "+data1);
		$.each(data1.environment, function(key, value) {
			//console.log(value["-name"]);
			console.log("systemApi = " + systemApi);
//			var literal = "<li  id='envId-" + value["name"] + "\' class='filter-submenu'  value=\'" + value[systemApi=='Jira' ? "mapId" : "name"] + "' onclick=\"getLeftMenuItems('env',\'"+value["name"]+"\',this);getCiResultsAjaxCall('env',null);\">" + value["name"] + "</li>";
			var literal = "<li  id='envId-" + value["name"] + "\' class='filter-submenu'  value=\'" + value[systemApi=='Jira' ? "mapId" : "envId"] + "' onclick=\"getLeftMenuItems('env',\'"+value["name"]+"\',this);getCiResultsAjaxCall('env',"+(systemApi=='Jira'?'this':value["envId"])+");\">" + value["name"] + "</li>";
			$("#ulEnv").after().append(literal);
			//alert($("#ulEnv").html());
		});

		if(data1.environment[0].testSuits.length > 0) {
			$.each(data1.environment[0].testSuits[0].testSuit, function(key, value) {
				//console.log(value["-name"]);
//				var literal = "<li  id='suiteId-" + value["name"] + "\' class='filter-submenu' value=\'" + value["name"] + "' onclick=\"getLeftMenuItems('suite','" + value["name"] + "',this);getCiResultsAjaxCall('suite',null);\">" + value["name"] + "</li>";
				var literal = "<li  id='suiteId-" + value["name"] + "\' class='filter-submenu' value=\'" + value["suiteId"] + "' onclick=\"getLeftMenuItems('suite','" + value["name"] + "',this);getCiResultsAjaxCall('suite',"+value["suiteId"]+");\">" + value["name"] + "</li>";
	       		$("#ulsuite").after().append(literal);
			});
		}

		testsuitesObj = data1.environment[0].testSuits.length > 0 ? data1.environment[0].testSuits[0].testSuit : null;
		bedObj = data1.environment[0].testSuits.length > 0 ? data1.environment[0].testSuits[0].testSuit[0].testBed : null;
		//console.log(bedObj);
		if(data1.environment[0].testSuits.length > 0) {
			$.each(data1.environment[0].testSuits[0].testSuit[0].testBed, function(key, value) {
			//console.log(value["-id"]);
//				var literal = "<li  id='bedId-" + value["id"] + "\' class='filter-submenu' value=\'" + value["id"] + "' onclick=\"getLeftMenuItems('bed','" + value["id"] + "',this);getCiResultsAjaxCall('bed',"+value["id"]+");\">" + value["text"] + "</li>";
				var literal = "<li  id='bedId-" + value["id"] + "\' class='filter-submenu' value=\'" + value["id"] + "' onclick=\"getLeftMenuItems('bed','" + value["id"] + "',this);getCiResultsAjaxCall('bed',"+value["id"]+");\">" + value["text"] + "</li>";
				$("#ulbed").after().append(literal);
			});
		}
	}

	$("#ulEnv li:first").addClass("filter-submenu selected-env");
    $("#ulsuite li:first").addClass("filter-submenu selected-suite");
    $("#ulbed li:first").addClass("filter-submenu selected-bed");
    setFirstTimeLabelValue();
}

function setFirstTimeLabelValue()
{
	var elEnv = document.getElementById('ulEnv');
	if(elEnv!=null && elEnv.childNodes.length > 0 && elEnv.childNodes.length > 1)
		env = elEnv.childNodes[1].innerHTML;
	
	var ulsuite = document.getElementById('ulsuite');
	if(ulsuite!=null && ulsuite.childNodes.length > 0 && ulsuite.childNodes.length > 1)
		suite = ulsuite.childNodes[1].innerHTML;
	
	var ulbed = document.getElementById('ulbed');
	if(ulbed!=null && ulbed.childNodes.length > 0 && ulbed.childNodes.length > 1)
		bed = ulbed.childNodes[1].innerHTML;
	
	applyLabelMenusHtml();
}

function applyLabelMenusHtml()
{
	var selectedFilter = "";

	if (env != undefined || suite != undefined || bed != undefined) {
		selectedFilter = " &nbsp; "+env + (systemApi == "Jenkins" ? (" - " + suite + " - " + bed) : "");
	}

	document.getElementById('selected-filter').innerHTML = selectedFilter;
	//$('selected-filter').html(/*env + " - " + suite + " - " + bed*/"jiten ");
}

function updateLabelMenu(eventParam,inputObject)
{
	env = $( "li[class='filter-submenu selected-env']")[0].innerHTML;
	if(systemApi=='Jenkins')suite = $( "li[class='filter-submenu selected-suite']")[0].innerHTML;
	if(systemApi=='Jenkins')bed = $( "li[class='filter-submenu selected-bed']")[0].innerHTML;
	applyLabelMenusHtml();
}


function getDefectsAjaxCall(which,input){
	var mapId;
	var locationHref = window.location.href;
	var locationHrefArr = locationHref.split("/");
	var appId = locationHrefArr[5];
	var appName = locationHrefArr[6];
	var mapIdRetrieveSplit = locationHrefArr[7].split(".");
	var from;
	var to;
	var mapIdVal = mapIdRetrieveSplit[0];
	var responseType = mapIdRetrieveSplit[1].split("?")[0]; 
	
	if (which == "env") {
		var jiraVal = input.getAttribute('value');
		mapId = jiraVal;
		from = $('#periodStrtDt').attr('value').substr(0,10);
		to = $('#periodEndDt').attr('value').substr(0,10);
	}else if(which == "period") {
		var element = $( "li[class='filter-submenu selected-env']");
		mapId = element[0].getAttribute('value');
		
		$('#periodStrtDt').val(input.getAttribute('startdt'));
		$('#periodEndDt').val(input.getAttribute('enddt'));
		from = input.getAttribute('startdt').substr(0,10);
		to = input.getAttribute('enddt').substr(0,10);
		$('#selected-period').html(input.innerHTML);
		
		setPeriodLabel(input.getAttribute('startdt'),input.getAttribute('enddt'));
	}
	//var mapIdVal =  mapIdElement[0].getAttribute('value');
	
	
	var uri = contextPath+"/app/"+appId+"/"+appName+"/"+mapId+"."+responseType;
	var dataValue = "from="+from+"&to="+to;
	$.ajax({url:uri,
			type: "POST",
			data:dataValue+"&mapId="+mapId,
			success:function(result)
				{
				loadCICharts(result);
				
			}});
	pushtoBrowserHistory(uri+"/?"+dataValue);
}

function retrievedDefectsResults(response){
	
	document.getElementById('ci-chart-container').innerHTML = "";
	var jsonResult = jQuery.parseJSON(response);
	test = jsonResult;
	pieChart();
	//var jsonResult = jQuery.parseJSON(response);
	
	
}

function getLeftMenuItems(event, liText, input) {
	 var data1 = test.data.Environments;
	    
	 if(systemApi == 'Jira'){
//	  getDefectsAjaxCall(event,input);
	  $('#ulEnv li').removeClass("filter-submenu selected-env").addClass("filter-submenu");
	  $(input).removeClass("filter-submenu").addClass("filter-submenu selected-env");
	 }
	 else if(systemApi == 'Jenkins'){
	  if (event == "env") {
	         var environmentData = $.grep(data1.environment, function(element, index) {
	             return element["name"] == liText;
	         });
	         //console.log(environmentData);
	         $.each(environmentData, function(key, value) {
	             //console.log('TestSuites:  ' + key + '--------------' + value.testSuits + '------------------' + value.testSuits[key].testSuit);
	             testsuitesObj = value.testSuits[key].testSuit;
	         });
	         $("#ulsuite").empty();
	         $.each(testsuitesObj, function(key, value) {
	             //console.log('TestSuites:  ' + key + '--------------' + value["-name"]);
	             var literal = "<li  id='suiteId-" + value["name"] + "\' class='filter-submenu' value=\'" + value["suiteId"] + "\' onclick=\"getLeftMenuItems('suite','" + value["name"] + "',this);getCiResultsAjaxCall('suite',"+value["id"]+");\">" + value["name"] + "</li>";
	             $("#ulsuite").after().append(literal);
	         });
	         //console.log(testsuitesObj[0].testBed);
	         $("#ulbed").empty();
	         $.each(testsuitesObj[0].testBed, function(key, value) {
	             var literal = "<li  id='bedId-" + value["id"] + "\' class='filter-submenu' value=\'" + value["id"] + "' onclick=\"getLeftMenuItems('bed','" + value["id"] + "',this);getCiResultsAjaxCall('bed',"+value["id"]+");\">" + value["text"] + "</li>";
	             $("#ulbed").after().append(literal);
	         });
	         
	         $('#ulEnv li').removeClass("filter-submenu selected-env").addClass("filter-submenu");
	         $('#ulsuite li').removeClass("filter-submenu selected-suite").addClass("filter-submenu");
	         $('#ulbed li').removeClass("filter-submenu selected-bed").addClass("filter-submenu");
	         $(input).removeClass("filter-submenu").addClass("filter-submenu selected-env");
	         $("#ulsuite li:first").removeClass("filter-submenu").addClass("filter-submenu selected-suite");
	         $("#ulbed li:first").removeClass("filter-submenu").addClass("filter-submenu selected-bed");
	     }

	     if (event == "suite") {

	         //console.log(testsuitesObj);
	         var bedData = $.grep(testsuitesObj, function(element, index) {
	             return element["name"] == liText;
	         });
	         
	         $.each(bedData, function(key, value) {
	             bedObj = value.testBed;
	         });
	         
	         $("#ulbed").empty();
	         $.each(bedObj, function(key, value) {
	             var literal = "<li  id='bedId-" + value["id"] + "' class='filter-submenu' value='" + value["id"] + "' onclick=getLeftMenuItems('bed','" + value["id"] + "',this);getCiResultsAjaxCall('bed',"+value["id"]+");>" + value["text"] + "</li>";
	             $("#ulbed").after().append(literal);
	         });
	         
	         $('#ulsuite li').removeClass("filter-submenu selected-suite").addClass("filter-submenu");
	         $('#ulbed li').removeClass("filter-submenu selected-bed").addClass("filter-submenu");
	         $(input).removeClass("filter-submenu").addClass("filter-submenu selected-suite");
	         $("#ulbed li:first").removeClass("filter-submenu").addClass("filter-submenu selected-bed");

	     }
	     if (event == "bed") {
	      $('#ulbed li').removeClass("filter-submenu selected-bed").addClass("filter-submenu");
	         $(input).removeClass("filter-submenu").addClass("filter-submenu selected-bed");
	     }
	 }

	 updateLabelMenu(event,input); 
}


function getMonthName(no)
{
	return monthArr[parseFloat(no) - 1];
}
function severityPieChart(severity, title) {
	if(severity.length > 0){
		$('#severity-chart-container').highcharts({
			 chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: 0,//null,
		            plotShadow: false,
		            width: 396,
					height: 380,
					style: {
						fontFamily:'"open_sansregular", "sans-serif","Arial"',
					},
		        },
		        title: {
		            text: title,
		            style: {
		            	fontSize: '14px',
		            	color: '#6a6a6a',
		            	fontWeight: 'bold'
		            }
		        },
		        tooltip: {
		            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
	            	 style: {
	            		 fontFamily:'"open_sansregular", "sans-serif","Arial"',
			             fontSize: '11px',
					 	 fontWeight: 'normal',
					     color: '#6a6a6a'
			         }
		        },
		        legend: {
					floating: true,
					y: 9,
		            layout: 'horizontal',
		            itemStyle: {
		                color: '#666',
		                fontWeight: 'normal',
		                fontSize: '11px'
		            }
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                size: '60%',
		                dataLabels: {
		                	distance: 10,
		                    enabled: true,
		                    format: '<b>{point.name}</b>: {point.y}',
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
		    					fontFamily:'"open_sansregular", "sans-serif","Arial"',
		    					fontWeight: 'normal',
		 		                fontSize: '11px'
		                     }
		                }
		            }
		        },
		        series: [{
		            type: 'pie',
		            name: title,
		            center: [165, 100],
		            showInLegend: true,
		            data: severity
		            }]
		 });
	}else{
		noDataAvailForChartDefaultDisplay('severity-chart-container','No data available for this Quarter. <br/>To see the data - please select a different Quarter');
	}
}
function noDataAvailForChartDefaultDisplay(paramsId,paramsMsg){
	$('#'+paramsId).highcharts({
		chart : {
			width : 380,
			height : 380,
			style: {
				fontFamily:'open_sansregular, sans-serif, Arial',
			},
		},
		title : {
			text : paramsMsg,
			align : 'left',
			x : 50,
			y : 180,
			floating : true
		},
		noData : {
			style : {
				fontWeight : 'bold',
				fontSize : '15px',
				color : '#303030',
				fontFamily:'open_sansregular, sans-serif, Arial',
			}
		}
	});
}

function statusPieChart(status){
	if(status.length > 0){
		$('#status-chart-container').highcharts({
			 chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: 0,//null,
		            plotShadow: false,
		            width: 396,
					height: 380,
					style: {
						fontFamily:'"open_sansregular", "sans-serif","Arial"',
					},
		        },
		        title: {
		            text: 'Status',
		            style: {
		            	fontFamily:'"open_sansregular", "sans-serif","Arial"',
		            	fontSize: '14px',
		            	color: '#6a6a6a',
		            	fontWeight: 'bold'
		            }
		        },
		        legend: {
					floating: true,
					y: 9,
		            layout: 'horizontal',
		            itemStyle: {
		            	fontFamily:'"open_sansregular", "sans-serif","Arial"',
		                color: '#666',
		                fontWeight: 'normal',
		                fontSize: '11px'
		            }
		        },
		        tooltip: {
		            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
		            	 style: {
		            		 fontFamily:'"open_sansregular", "sans-serif","Arial"',
				             fontSize: '11px',
						 	 fontWeight: 'normal',
						     color: '#6a6a6a'
				         }
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                size: '60%',
		                dataLabels: {
		                	distance: 10,
		                    enabled: true,
		                    format: '<b>{point.name}</b>: {point.y}',
		                    /*style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
		    					fontFamily:'"open_sansregular", "sans-serif","Arial"',
		                    }*/
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
		    					fontFamily:'"open_sansregular", "sans-serif","Arial"',
		    					fontWeight: 'normal',
		 		                fontSize: '11px'
		                     }
		                }
		            }
		        },
		        series: [{
		            	type: 'pie',
			            name: 'Status',
			            center: [165, 100],
			            showInLegend: true,
			            data: status
		        	}]
		 });
	}else{
		noDataAvailForChartDefaultDisplay('status-chart-container','No data available for this Quarter. <br/>To see the data - please select a different Quarter');
	}
}
