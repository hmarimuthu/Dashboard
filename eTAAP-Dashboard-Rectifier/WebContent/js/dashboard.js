function fillDashboardCharts() {
	var jsonResult = jQuery.parseJSON(jsonObj);

	if (jsonResult == null) {
		alert("Database not found !!");
		return;
	}

	generateTcmDashBoardChart(jsonResult.data.Tcm, 'tcm_dashboard_chart');

	var obj1 = jsonResult.data.Defects.defectCases;
	var priority = obj1.Priority;
	var priorityDrilldown = obj1.PriorityDrilldown;
	var allPriorityCount = obj1.AllPriorityCount;
	var severity = obj1.Severity;
	var severityDrilldown = obj1.SeverityDrilldown;
	var allSeverityCount = obj1.AllSeverityCount;
	var key = obj1.Key;
	
	if(priority.length > 0) {
		var p = priority.toString().split(',').map(function(item) {
			return item;
		});

		var drilldownFlag = false;
		$('#priority_chart_container').highcharts({
			 chart: {
		            backgroundColor: null,
		            marginBottom: 50,
		            borderWidth: 0,
		            shadow: false,
		            width: 365,
					height: 258,
					type: 'pie',
					style: {
						fontFamily: 'open_sansregular,sans-serif,Arial'						
					},
					events: {
		                drilldown: function (e) {
		                	var ar = [];
		                	for (var i = 0; i < e.seriesOptions.data.length; i++) {
		                		e.seriesOptions.data[i].visible = true;
		                		var textElement = e.seriesOptions.data[i].name;
		                		ar.push(textElement);
			             	}
		                	var ar_plus = ar.toString().replace(/\,/g, ' + ');
		                	this.setTitle({text: ar_plus});
		                	drilldownFlag = true;
		                	$('#addPriorityText').text(e.point.y);
		                },
		                drillup: function (e) {
		                	drilldownFlag = false;
		                	$('#addPriorityText').text(allPriorityCount);
		                	this.setTitle({text: priority[0].priorityLabel});
		                },
		                load: function() {
		                	this.setTitle({text: priority[0].priorityLabel});
		                }
					}
		        },
		        lang: {
		            drillUpText: 'Back'
		        },
		        title: {
		        	text: '',
		            floating: true,
					y: 10,
		            style: {
			             fontSize: '14px',
					 	 fontWeight: 'bold',
					     color: '#6a6a6a'
			         }
		        },
		        tooltip: {
		            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
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
		            	//allowPointSelect: true,
		                cursor: 'pointer',
		                size:'80%',
		                dataLabels: {
							enabled: true,
		                    format: '{point.y}',
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                    },
		                    distance: -30,
		                    
		                },
		                point: {
		                    events: {
		                        legendItemClick: function (e) {
		                        	if (this.visible) {
		                        		if (drilldownFlag) {
				                        	$('#addPriorityText').text(parseInt($('#addPriorityText').text()) - this.y);
		                        		} else {
		                        			allPriorityCount = allPriorityCount - this.y;
				                        	$('#addPriorityText').text(allPriorityCount);
		                        		}
                                    } else {
                                    	if (drilldownFlag) {
				                        	$('#addPriorityText').text(parseInt($('#addPriorityText').text()) + this.y);
                                    	} else {
                                    		allPriorityCount = allPriorityCount + this.y;
 				                        	$('#addPriorityText').text(allPriorityCount);
                                    	}
                                    }
		                        }
		                    }
		                }
		            }
		        },
		        xAxis: {
		            categories: true
		        },
		        drilldown: {
		        	drillUpButton: {
		        		relativeTo: 'spacingBox',
		                position: {
		                	y: 40,
		                    x: -18
		                },
		                theme: {
		                    fill: '#e2e2e2',
		                    troke: '#c3c3c3',
		                    r: 3,
		                width: 50,
		                height: 15,
		                style: {
							fontSize: '11px',
							fontFamily:'"open_sansregular", "sans-serif","Arial"',
						},
		                paddingLeft: 20,
		                states: {
	                       hover: {
	                           fill: '#a9ce8e',
		                       stroke: '#c3c3c3'
		                          },
                           select: {
                               stroke: '#039',
                               fill: '#bada55'
                           }
		                }
		               }
		        	},
		        	series: priorityDrilldown
		        },
		        series: [{
		            name: 'Priority',
		            showInLegend: true,
		            data: priority
		        }]
		 });
		$('#addPriorityText').text(allPriorityCount);
	} else {
		$('#addPriority').hide();
	/*	$('#priority_chart_container').highcharts({
			chart: {
				backgroundColor: null,
				width: 365,
				height: 230
			},
			title: {
				text: 'No records available for Jira',
				align: 'left',
				x: 45,
				y: 120,
				floating: true
			},
			noData: {
				style: {
					fontWeight: 'bold',
					fontSize: '15px',
					color: '#303030'
				}
			}
		});*/
		//document.getElementById('#'+paramsId).innerHTML = '<div class="badge"></div>';
		 var nodata =
			    "<div class='badge'>"+
			    "</div>";
		 $('#priority_chart_container').html(nodata);
	}

	if(severity.length > 0) {
		var drilldownFlag = false;
		$('#severity_chart_container').highcharts({
			 chart: {
				 backgroundColor: null,
				 marginBottom: 50,
		            width: 365,
					height: 258,
					type: 'pie',
					style: {
						fontFamily: 'open_sansregular,sans-serif,Arial'						
					},
					events: {
		                drilldown: function (e) {
		                	for (var i = 0; i < e.seriesOptions.data.length; i++) {
		                		e.seriesOptions.data[i].visible = true;
		                	}
		                	drilldownFlag = true;
		                	$('#addSeverityText').text(e.point.y);
		                },
		                drillup: function (e) {
		                	drilldownFlag = false;
		                	$('#addSeverityText').text(allSeverityCount);
		                },
		                load: function() {
		                	
		                	if(key == "Severity"){
		                		this.setTitle({text: 'Defects by Severity'});
		                	} else {
		                		this.setTitle({text: 'Defects by Priority'});
		                	}
		                }
					}
		        },
		        lang: {
		            drillUpText: 'Back'
		        },
		        title: {
					floating: true,
					y: 9,
		            text: '',
		            style: {
			             fontSize: '14px',
					 	 fontWeight: 'bold',
					     color: '#6a6a6a'
			         }
		        },
		        tooltip: {
		       
		            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
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
		                //allowPointSelect: true,
		                cursor: 'pointer',
		                size:'80%',
		                dataLabels: {
		                    enabled: true,
		                    format: '{point.y}',
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                    },
		                    distance: -30
		                },
		                point: {
		                    events: {
		                        legendItemClick: function (e) {
		                        	if (this.visible) {
		                        		if (drilldownFlag) {
				                        	$('#addSeverityText').text(parseInt($('#addSeverityText').text()) - this.y);
		                        		} else {
		                        			allSeverityCount = allSeverityCount - this.y;
				                        	$('#addSeverityText').text(allSeverityCount);
		                        		}
                                    } else {
                                    	if (drilldownFlag) {
				                        	$('#addSeverityText').text(parseInt($('#addSeverityText').text()) + this.y);
                                    	} else {
                                    		allSeverityCount = allSeverityCount + this.y;
    			                        	$('#addSeverityText').text(allSeverityCount);
                                    	}
                                    }
		                        },
		                	load:function(e) {
			                	if(key == "Severity"){
			                		this.setName({text: 'Severity'});
			                	} else {
			                		this.setName({text: 'Priority'});
			                	}
			                }
		                    }
		                }
		            }
		        },
		        xAxis: {
		            categories: true
		        },
		        drilldown: {
		        	drillUpButton: {
		        		relativeTo: 'spacingBox',
		                position: {
		                	y: 40,
		                    x: -18
		                },
		                theme: {
		                    fill: '#e2e2e2',
		                    troke: '#c3c3c3',
		                    r: 3,
		                width: 50,
		                height: 15,
		                paddingLeft: 20,
		                style: {
							fontSize: '11px',
							fontFamily:'"open_sansregular", "sans-serif","Arial"',
						},
		                states: {
	                       hover: {
	                           fill: '#a9ce8e',
		                       stroke: '#c3c3c3'
		                          },
                           select: {
                               stroke: '#039',
                               fill: '#bada55'
                           }
		                }
		               }
		        	},
		        	series: severityDrilldown
		        },
		        series: [{
		            //name: 'Severity',
		        	name: ( function () {
		        		if(key == "Severity") {
		        			return 'Severity';
		        		} else {
		        			return 'Priority';
		        		}
		        	}()),
		            showInLegend: true,
		            data: severity
		        }]
		 });
		$('#addSeverityText').text(allSeverityCount);
	} else {
		$('#addSeverity').hide();
		/*$('#severity_chart_container').highcharts({
			chart: {
				backgroundColor: null,
				width: 365,
				height: 230
			},
			title: {
				text: 'No records available for Jira',
				align: 'left',
				x: 45,
				y: 120,
				floating: true
			},
			noData: {
				style: {
					fontWeight: 'bold',
					fontSize: '15px',
					color: '#303030'
				}
			}
		});*/
		//document.getElementById('severity_chart_container').innerHTML = '<div class="badge"></div>';
		 var nodata =
			    "<div class='badge'>"+
			    "</div>";
		 $('#severity_chart_container').html(nodata);
		
	}

	var obj2 = jsonResult.data.Jenkins;
	var appName = obj2.appNameCSV;
	//var buildCount = obj2.buildCountCSV;
	var failCountCSV = obj2.failCountCSV;
	var passCountCSV = obj2.passCountCSV;
	var skipCountCSV = obj2.skipCountCSV;

	var failCount = failCountCSV.toString().split(',').map(function(item) {
	    return parseInt(item, 10);
	});
	var passCount = passCountCSV.toString().split(',').map(function(item) {
	    return parseInt(item, 10);
	});
	var skipCount = skipCountCSV.toString().split(',').map(function(item) {
	    return parseInt(item, 10);
	});

	if (appName.length > 0) {
		$('#jenkins_chart_container').highcharts({
			chart: {
			backgroundColor: null,
			marginBottom: 50,
//			zoomType: 'xy',
            panning: false,
            style: {
				fontFamily:'"open_sansregular", "sans-serif","Arial"',
			},
            panKey: 'shift',
				type: 'column',
				width: 365,
				height: 260,
				style: {
	                margin: '0',
	                padding: '0',
	                fontFamily: 'open_sansregular,sans-serif,Arial'
	            }
			},
			colors: ['#a9ce8e', '#ff6666', '#ffcc66'],
			tooltip: {
				enabled: false
			},
			title: {
				text: 'Test Automation Results of Last Build',
				style: {
					fontSize: '14px',
					fontWeight: 'bold',
					color: '#6a6a6a',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				}
			},
			xAxis: {
				categories: appName,
			    labels: {
					overflow: 'justify',
					formatter: function() {
						return this.value.toString().substring(0, 6);					
					},
					autoRotation: false,
					style: {
						fontSize: '11px',
						fontFamily:'"open_sansregular", "sans-serif","Arial"',
						textAlign: 'left',
						width: '50px',
						whiteSpace: 'nowrap',
						y: -40,
						x: 4
					}
			    }
			},
			yAxis: {
	            min: 0,
	            title: {
	                text: 'Results',
	                style: {
						fontSize: '12px',
						fontFamily:'"open_sansregular", "sans-serif","Arial"',
					}
	            },
				labels: {
					style: {
						fontSize: '11px',
						fontFamily:'"open_sansregular", "sans-serif","Arial"',
					}
				},
	            stackLabels: {
	                enabled: true,
	                style: {
	                    fontWeight: 'bold',
						fontFamily:'"open_sansregular", "sans-serif","Arial"',
	                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
	                }
	            }
	        },
			tooltip: {
	            pointFormat: '{series.name}: <b>{point.y}</b><br/>Total: <b>{point.stackTotal}</b>'
	        },
			legend: {
				floating: true,
				y: 14,
			      align: 'center',
		        layout: 'horizontal',
		        itemStyle: {
		        	color: '#666',
		            fontWeight: 'normal',
		            fontSize: '11px',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
		        }
			 },
			 plotOptions: {
				 column: {
					 stacking: 'normal',
					 dataLabels: {
						 formatter: function() {
							 var val = this.y;
							 if (val < 6) {
								 return '';
							 }
							 return val;
						 },
						 color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
						 style: {
							 textShadow: '0 0 1px black'
						 }
					 }
				 }
			},
			series: [{
				name: 'Passed',
				data: passCount
			}, {
				name: 'Failed',
				data: failCount
			}, {
				name: 'Skipped',
				data: skipCount
			}]
		});
	} else {
		/*$('#jenkins_chart_container').highcharts({
			chart: {
				backgroundColor: null,
				width: 365,
				height: 258,
				style: {
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				},
			},
			title: {
				text: 'No records available for Jenkins',
				align: 'left',
				x: 28,
				y: 120,
				floating: true
			},
			noData: {
				style: {
					fontWeight: 'bold',
					fontSize: '15px',
					color: '#303030',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				}
			}
		});*/
		
		//document.getElementById('jenkins_chart_container').innerHTML = '<div class="badge"></div>';
		 var nodata =
			    "<div class='no-builds-badge'>"+
			    "</div>";
		 $('#jenkins_chart_container').html(nodata);
	}
	defectsStatistics(jsonResult);
	defectsLife(jsonResult);
	commitedCompletedUserStoriesChart(jsonResult);
}

function generateTcmDashBoardChart(quaterJsonString,chartId) {
	var automatedTestCountCSV = '';
	var manualTestCountCSV = '';
	var suiteTypeNameCSV = '';
	var delimiter = ',';
	if(quaterJsonString != null) {
		$.each(quaterJsonString, function(i, res) {
			if (res.testCaseType.toLowerCase() == 'automated') {
				automatedTestCountCSV += res.testCaseCount + delimiter;
			} else if (res.testCaseType.toLowerCase() == 'manual') {
				manualTestCountCSV += res.testCaseCount + delimiter;
			}
			if (i % 2 == 0) {
				suiteTypeNameCSV += res.appName + delimiter;
			}
		});
		automatedTestCountCSV = automatedTestCountCSV.substring(0, automatedTestCountCSV.length-1);
		manualTestCountCSV = manualTestCountCSV.substring(0, manualTestCountCSV.length-1);
		suiteTypeNameCSV = suiteTypeNameCSV.substring(0, suiteTypeNameCSV.length-1);
	}
	fillDashboardTcmChart(quaterJsonString, chartId, automatedTestCountCSV,manualTestCountCSV, suiteTypeNameCSV);
}

function fillDashboardTcmChart(response, chartId, automatedTestCountCSV, manualTestCountCSV,suiteTypeNameCSV) {
	document.getElementById(chartId).innerHTML = "";
	var automatedTestCount = automatedTestCountCSV.toString().split(',').map(function(item) {
		return parseInt(item, 10);
	});
	var manualTestCount = manualTestCountCSV.toString().split(',').map(function(item) {
		return parseInt(item, 10);
	});
	var xaxisCatg = suiteTypeNameCSV.toString().split(',').map(function(item) {
		return item;
	});
	if (response != null && response.length > 0) {
		$('#'+chartId).highcharts({
			chart: {
				backgroundColor: null,
				marginBottom: 50,
				//zoomType: 'xy',
           		panning: false,
            	panKey: 'shift',
				type: 'column',
				width: 365,
				height: 258, 
				style: {
					margin: '0',
					padding: '0'
				},
				style: {
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				},
			},
			colors: ['#008cc4','#00cccc'],
			tooltip: {
				enabled: false
			},
			title: {
				text: 'Test Case Statistics',
				 style: {
		             fontSize: '14px',
					 fontWeight: 'bold',
					 color: '#6a6a6a',
					 fontFamily:'"open_sansregular", "sans-serif","Arial"',
		         }
			},
			subtitle: {
				text: '',
				 style: {
		             fontSize: '11px',
					 fontFamily:'"open_sansregular", "sans-serif","Arial"',
		         }
			},
			xAxis: {
				categories: xaxisCatg,
				title: {
					text: '',
					 style: {
			             fontSize: '11px',
						 fontFamily:'"open_sansregular", "sans-serif","Arial"',
			         }
				},
				labels: {
					overflow: 'justify',
					formatter: function() {
						return this.value.toString().substring(0, 6);					
					},
					autoRotation: false,
					style: {
						fontSize: '11px',
						textAlign: 'left',
						fontFamily:'"open_sansregular", "sans-serif","Arial"',
					}
				}
			},
			yAxis: {
				min: 0,
				minTickInterval: 100,
				title: {
					text: 'Test Cases',
					style: {
			             fontSize: '11px',
			             fontFamily:'"open_sansregular", "sans-serif","Arial"',
			        }
				},
				stackLabels: {
					enabled: true,
				    style: {
				        fontWeight: 'bold',
				        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray',
						fontFamily:'"open_sansregular", "sans-serif","Arial"',
				    }
				}
			},
			tooltip: {
				pointFormat: '{series.name}: <b>{point.y:1f}</b>' 
			},
	        legend: {
				floating: true,
			    y: 16,
	            layout: 'horizontal',
	            itemStyle: {
	                color: '#666',
	                fontWeight: 'normal',
	                fontSize: '11px',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
	            }
	        },
	        plotOptions: {
	        	column: {
	        		stacking: 'normal',
			        dataLabels: {
						formatter: function() {
							var val = this.y;
			                if (val < 6) {
			                    return '';
			                }
			                return val;
						},
			            color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
			            style: {
			                textShadow: '0 0 3px black',
							fontFamily:'"open_sansregular", "sans-serif","Arial"',
			            }
			        } 
			    }
			},
			series: [{
				name: 'Manual',
				data: manualTestCount,
				pointWidth: 30
			}, {
				name: 'Automated',
				data: automatedTestCount,
				pointWidth: 30
			}]
		});
	} else {
/*		$('#'+chartId).highcharts({
			chart: {
				backgroundColor: null,
				width: 365,
				height: 230
			},
			title: {
				text: 'No records available for TCM',
				align: 'left',
				x: 40,
				y: 120,
				floating: true
			},
			noData: {
				style: {
					fontWeight: 'bold',
					fontSize: '15px',
					color: '#303030',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				}
			}
		});*/
		//document.getElementById('#'+chartId).innerHTML = '<div class="badge"></div>';
		 var nodata =
			    "<div class='no-test-cases-badge'>"+
			    "</div>";
		 $('#'+chartId).html(nodata);
	}
}

function defectsLife(result) {
	var categoriesVal = result.data.defectsLife.categories;
	var seriesObj = result.data.defectsLife.series;
	if(seriesObj.length > 0) {
		$('#defects_life_container').highcharts({
	        chart: {
				backgroundColor: null,
				marginBottom: 75,
	            type: 'bar',
				width: 365,
				height: 258,
	            style: {
	                margin: '0',
	                padding: '0',
	                fontFamily: 'open_sansregular,sans-serif,Arial'
	            },
	            style: {
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				},
				events: {
		            load: function() {
		            	if(categoriesVal[3] == "severity"){
		            		this.yAxis[0].setTitle({text: 'Severity Count'});
		            		this.yAxis[0].setTitle({textAlign: 'right', style: {fontSize: '11px'}});
		            	} else if(categoriesVal[3] == "priority"){
		            		this.yAxis[0].setTitle({text: 'Priority Count'});
		            		this.yAxis[0].setTitle({textAlign: 'right',fontSize: '11px'});
		            	}
		            }	
				}
	        },
	        title: {
	            text: 'Defects Life',
	            style: {
	            	fontSize: '14px',
	            	fontWeight: 'bold',
	            	color: '#6a6a6a',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
	            }
	        },
	        xAxis: {
	            categories: categoriesVal,
	            title: {
	                text: 'Greater than'
	            }
	        },
	        yAxis: {
	            min: 1, 
	            tickInterval: 1,
	            title: {
	                text: ''
	            }
	        },
	        /*legend: {
	            //reversed: true
	        	itemStyle: {
	                color: '#666',
	                fontWeight: 'normal',
	                fontSize: '11px'
	            }
	        },*/
	        legend: {
				floating: true,
				y: 14,
		        layout: 'horizontal',
		        align: 'center',
		        itemStyle: {
		        	color: '#666',
		            fontWeight: 'normal',
		            fontSize: '11px',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
		        }
			 },
	        plotOptions: {
	            series: {
	                stacking: 'normal'
	            }
	        },
	        tooltip: {
	            pointFormat: '{series.name}: <b>{point.y}</b><br/>'
	        },
	        series: seriesObj
	    });
	} else {
		//noDataAvailForChartDefaultDisplay('defects_life_container', 'No records available for Jira');
		 var nodata =
			    "<div class='badge'>"+
			    "</div>";
		 $('#defects_life_container').html(nodata);
	}
}
//Defects Statistics Chart
function defectsStatistics(result) {
	var categoriesVal = result.data.defectsStatistics.categories;
	var seriesObj = result.data.defectsStatistics.series;

	if(seriesObj.length>0) {
		$('#defects_statistics_container').highcharts({
			chart: {
				backgroundColor: null,
				marginBottom: 50,
				width: 365,
				height: 260,
				zoomType: 'xy',
            	panning: true,
            	panKey: 'shift',
				style: {
					margin: '0',
	                padding: '0',
	                fontFamily: 'open_sansregular,sans-serif,Arial'
	            }
			},
	        title: {
	            text: 'Defects Statistics',
	            style: {
		             fontSize: '14px',
					 fontWeight: 'bold',
					 color: '#6a6a6a',
					 fontFamily:'"open_sansregular", "sans-serif","Arial"',
		         }
	        },
	        xAxis: {
	            categories: categoriesVal
	        },
	        yAxis: {
	        	min: 0,
	        	title: {
	            	text: 'Defects' 
	            }
	        },
	        tooltip: {
	            pointFormat: '{series.name}: <b>{point.y}</b><br/>'
	        },
	        legend: {
				floating: true,
				y: 14,
	            layout: 'horizontal',
	            align: 'center',
	            borderWidth: 0,
				itemStyle: {
					color: '#666',
					fontWeight: 'normal',
					fontSize: '11px',
					align: 'center',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				}
	        },
	        series: seriesObj
	    });
	} else {
		//noDataAvailForChartDefaultDisplay('defects_statistics_container', 'No records available for Jira');
		 var nodata =
			    "<div class='badge'>"+
			    "</div>";
		 $('#defects_statistics_container').html(nodata);
	}
}
// No data chart
function noDataAvailForChartDefaultDisplay(paramsId,paramsMsg) {
/*	$('#'+paramsId).highcharts({
		chart: {
			backgroundColor: null,
			width: 365,
			height: 258,
			style: {
				fontFamily:'"open_sansregular", "sans-serif","Arial"',
			},
		},
		title: {
			text: paramsMsg,
			align: 'left',
			x: 45,
			y: 120,
			floating: true
		},
		noData: {
			style: {
				fontWeight: 'normal',
				fontSize: '15px',
				color: '#303030',
				fontFamily:'"open_sansregular", "sans-serif","Arial"',
			}
		}
	});*/
	//document.getElementById('#'+paramsId).innerHTML = '<div class="badge"></div>';
	 var nodata =
		    "<div class='no-user-stories-badge'>"+
		    "</div>";
	 $('#'+paramsId).html(nodata);
}

function commitedCompletedUserStoriesChart(result) {
	var categoriesVal = result.data.commitedCompletedUserStories.categories;
	//alert("1 "+categoriesVal);
	var seriesObj = result.data.commitedCompletedUserStories.series;
	//alert("2 "+seriesObj);
	if(seriesObj.length > 0) {
	    $('#committed_completed_userstories_chart_container').highcharts({
	        chart: {
	            type: 'column',
	            width: 1109,
				height: 258,
				style: {
					fontFamily: 'open_sansregular,sans-serif,Arial'				
				}
	        },
	        title: {
				text: 'Sprint Velocity',
				 style: {
		             fontSize: '14px',
					 fontWeight: 'bold',
					 color: '#6a6a6a'
		         }
	        },
	        
/*	        subtitle: {
	            text: 'Source: etouch.net'
	        },
	        */

	        xAxis: {
		    categories: categoriesVal,
		    
			overflow: 'justify',
			labels: {
			formatter: function() {
				//return this.value.toString().substring(0, 6);	
				return this.value.toString();
/*				var sprintName = this.value.toString().substring(0, 35);
				if(sprintName.length > 10){
	        		var str1 = sprintName.substring(0,10);
	        		var str2 = sprintName.substring(11);
	        		sprintName = str1+ '<br>'+ str2;
	        	}
	        	return sprintName;
*/			},
	        style: {
				color: '#606060',
				fontSize: '11px',
				lineHeight: '12px',
				left: '100px',
				top: '100px'
			}
			},
			autoRotation: false,

			/*labels: {
			    
				style: {
					color: '#606060',
					fontSize: '11px',
					lineHeight: '12px',
					left: '100px',
					top: '100px'
				}
			}*/
			},

	        yAxis: {
	            min: 0,
				tickInterval: 5,
				stackLabels: {
					enabled: true,
				    style: {
				        fontWeight: 'bold',
				        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
				    }
				},
	            title: {
	                text: 'Story Points'
	            }
	        },


	        tooltip: {
	           // headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	            headerFormat: '<span style="font-size:10px"></span><table>',
	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                '<td style="padding:0"><b>{point.y:.1f} points</b></td></tr>',
	            footerFormat: '</table>',
	            shared: true,
	            useHTML: true
	        },
	        /*legend: {
				floating: true,
			    y: 11,
	            layout: 'horizontal',
	            itemStyle: {
	                color: '#666',
	                fontWeight: 'normal',
	                fontSize: '11px'
	            }
	        },*/
	        legend: {
				floating: true,
				y: 14,
	            layout: 'horizontal',
	            align: 'center',
	            borderWidth: 0,
				itemStyle: {
					color: '#666',
					fontWeight: 'normal',
					fontSize: '11px',
					align: 'center',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				}
	        },
	        
	        plotOptions: {
	            column: {
	                pointPadding: 0.02,
	                borderWidth: 0
	            }
	            
/*	        	column: {
	        		stacking: 'normal',
			        dataLabels: {
						formatter: function() {
							var val = this.y;
			                if (val < 6) {
			                    return '';
			                }
			                return val;
						},
			            color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
			            style: {
			                textShadow: '0 0 3px black'
			            }
			        } 
			    }
			    */
	        },
	        series: seriesObj
	    });		
	} else {
		noDataAvailForChartDefaultDisplay('committed_completed_userstories_chart_container', 'No records available for Jira');
	}
}

