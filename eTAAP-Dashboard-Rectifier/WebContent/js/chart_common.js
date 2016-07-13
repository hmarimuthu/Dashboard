var legendsArray = new Array();
legendsArray['multiline_tcm']={
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
    };
legendsArray['multiline_iterations']={
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        borderWidth: 0
     };
legendsArray['multiline_defectsStatistics']=legendsArray['multiline_tcm'];

var css_constructPieChart= new Array();
css_constructPieChart['pieChart_defects']={noDataAvail:{
	chart : {
		width : 380,
		height: 380,
		style: {
			fontFamily:'"open_sansregular", "sans-serif","Arial"',
		},
	},
	title : {
		text : 'No data available for this Quarter. <br/> To see the data - please select a different Quarter',
		align : 'left',
		x : 60,
		y : 190,
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
}};

function constructSplineChart(chartContainer, title, seriesObj, categoriesVal, xAxisTitle, yAxisTitle, nodataMsg) {
	if(seriesObj &&  seriesObj.length > 0) {
$(function () {
	    $('#datewiseCommitedCompletedUserStories').highcharts({
	        chart: {
	            type: 'spline',
	            style: {
	    			fontFamily: 'open_sansregular,sans-serif,Arial'
	    		}
	        },
	        title: {
	        	text: 'User Stories',
	            x: -20,
	            style: {
	            	color: '#6a6a6a',
	            	fontSize: '14px',
	            	fontWeight: 'bold',
	            	fontFamily: 'open_sansregular,sans-serif,Arial'
	            }
	        },
	        subtitle: {
	            x: -20
	        },
					        xAxis : {
						title : {
							text : xAxisTitle
						},

						startOnTick : true,
						endOnTick : true,
						tickmarkPlacement : 'on',
						categories : categoriesVal,
						labels : {
							formatter : function() {
								var dateToFrt = new Date(this.value);
								var formattedDate = dateToFrt.getDate()
										+ "<br>"
										+ monthNames[dateToFrt.getMonth()];
								return formattedDate;
							}
						},
						style : {
							fontSize : '10px',
						}

					},
			        yAxis: {
			           min: 0,
		     	       allowDecimals: false,
			           title: {
			        	   text: yAxisTitle
			           },
	     	           style: {
			               fontSize: '10px',
			           }
			        },
				    tooltip: {
				      crosshairs: false,
				      shared: false,
					  formatter: function() {
							var dateToFrt = new Date(this.x);
							var formattedDate = dateToFrt.getDate()+" "+monthNames[dateToFrt.getMonth()];
							result = this.series.name+"-"+this.y+"<br>"+formattedDate;
							return result;
						}
			    },
			        legend: {
			            layout: 'vertical',
			            align: 'right',
			            verticalAlign: 'middle',
			            borderWidth: 0
			        
	/*		            align: 'right',
			            layout: 'vertical',
			            verticalAlign: 'top',
			            x: -10,
			            y: 10,
			            floating: true,
			            borderWidth: 0
	*/		        
			        },
	            labels: {
	                formatter: function () {
	                    return this.value + '°';
	                }
	            },

	        tooltip: {
	            crosshairs: true,
	            shared: true
	        },
	        plotOptions: {
	            spline: {
	                marker: {
	                    radius: 4,
	                    lineColor: '#666666',
	                    lineWidth: 1
	                }
	            }
	        },
	        series: [{
	            name: seriesObj[0].name,
	            data: seriesObj[0].data

	        }, {
	            name: seriesObj[1].name,
	            data: seriesObj[1].data

	        }, {
	            name: seriesObj[2].name,
	            data: seriesObj[2].data

	        }]
	    });
	});
	} else {

		$(chartContainer).highcharts( {
			chart : {
				//width : 780,
				height : 380
			},
			title : {
				text : nodataMsg,
				align : 'left',
				x : 180,
				y : 180,
				floating : true,
				style: {
					fontFamily: 'open_sansregular,sans-serif,Arial'
					
				}
			},
			noData : {
				style : {
					fontWeight : 'bold',
					fontSize : '15px',
					color : '#303030'
				}
			}
		});
	
		 //$(chartContainer).html(nodataMsg);
	}
}

function constructPieChart(chartContainer, totalCountText, title, 
		chartWidth, chartHeight, chartColors, totalCount, chart, chartDrilldown, nodataMsg,seriesParam) {
	
	if(chart != null && chart.length > 0) {
		var drilldownFlag = false;
		$(chartContainer).highcharts({
			 chart: {
				 backgroundColor: null,
				 marginBottom: 50,
		            width: chartWidth,
					height: chartHeight,
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
		                	$(totalCountText).text(e.point.y);
		                },
		                drillup: function (e) {
		                	drilldownFlag = false;
		                	$(totalCountText).text(totalCount);
		                },
		                load: function() {
		                	this.setTitle({text: title});
		                }
					}
		        },
		        lang: {
		            drillUpText: 'Back'
		        },
		        colors: chartColors,
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
		                cursor: 'pointer',
		                size:(typeof nodataMsg == 'object') ? 200 : '80%',
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
				                        	$(totalCountText).text(parseInt($(totalCountText).text()) - this.y);
		                        		} else {
		                        			totalCount = totalCount - this.y;
				                        	$(totalCountText).text(totalCount);
		                        		}
                                    } else {
                                    	if (drilldownFlag) {
				                        	$(totalCountText).text(parseInt($(totalCountText).text()) + this.y);
                                    	} else {
                                    		totalCount = totalCount + this.y;
    			                        	$(totalCountText).text(totalCount);
                                    	}
                                    }
		                        },
			                	load:function(e) {
			                		this.setName({text: 'Severity'});
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
		        	series: chartDrilldown
		        },
		        series:seriesParam,
		 });
		$(totalCountText).text(totalCount);
	} else 
		{
			if(typeof nodataMsg == 'object') {
				$(chartContainer).highcharts(nodataMsg);
			}else{
				$(chartContainer).html(nodataMsg);
			}
		}
}

function check(a,b)
{
	alert("hello");}

function constructMultiLineChart(chartContainer, lineChartType, title, 
		chartWidth, chartHeight, seriesObj, categoriesVal, xAxisTitle, yAxisTitle, nodataMsg,legendObject) {
	
	if(seriesObj.length > 0) {
		$(chartContainer).highcharts({
			chart: {
				type: lineChartType,
				backgroundColor: null,
				marginBottom: 50,
				width: chartWidth,
				height: chartHeight,
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
	            text: title,
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
	            	text: yAxisTitle 
	            }
	        },
	        tooltip: {
	            pointFormat: '{series.name}: <b>{point.y}</b><br/>'
	        },
	        legend: legendObject,
	        series: seriesObj
	    });
	} else {
		 $(chartContainer).html(nodataMsg);
	}
}

function constructStackedColumnChart(chartContainer, title, 
		chartWidth, chartHeight, chartColors, seriesObj, categoriesVal, xAxisTitle, yAxisTitle, nodataMsg)
{
	if (seriesObj.length > 0) {
		$(chartContainer).highcharts({
			chart: {
			backgroundColor: null,
			marginBottom: 50,
            panning: false,
            style: {
				fontFamily:'"open_sansregular", "sans-serif","Arial"',
			},
            panKey: 'shift',
				type: 'column',
				width: chartWidth,
				height: chartHeight,
				style: {
	                margin: '0',
	                padding: '0',
	                fontFamily: 'open_sansregular,sans-serif,Arial'
	            }
			},
			colors: chartColors,
			title: {
				text: title,
				style: {
					fontSize: '14px',
					fontWeight: 'bold',
					color: '#6a6a6a',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				}
			},
			xAxis: {
				categories: categoriesVal,
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
	                text: yAxisTitle,
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
	            headerFormat: '<span style="font-size:10px"></span><table>',
	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                '<td style="padding:0"><b>{point.y}</b></td></tr>',
	            footerFormat: '</table>',
	            shared: true,
	            useHTML: true
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
			series: seriesObj
		});
	} else {
		 $(chartContainer).html(nodataMsg);
	}
}

function constructStackedBarChart(chartContainer, title, 
		chartWidth, chartHeight, seriesObj, categoriesVal, xAxisTitle, yAxisTitle, nodataMsg) {
	if(seriesObj.length > 0) {
		$(chartContainer).highcharts({
	        chart: {
				backgroundColor: null,
				marginBottom: 75,
	            type: 'bar',
				width: chartWidth,
				height: chartHeight,
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
		            	this.yAxis[0].setTitle({text: yAxisTitle});
	            		this.yAxis[0].setTitle({textAlign: 'right', style: {fontSize: '11px'}});
		            }	
				}
	        },
	        title: {
	            text: title,
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
	                text: xAxisTitle
	            }
	        },
	        yAxis: {
	            min: 0, 
	            tickInterval: 1,
	            title: {
	                text: yAxisTitle
	            }
	        },
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
		 $(chartContainer).html(nodataMsg);
	}
}

function constructColumnChart(chartContainer, title, chartWidth, chartHeight, 
		chartColorsObj, seriesObj, categoriesVal, xAxisTitle, yAxisTitle, nodataMsg) {
	if(seriesObj.length > 0) {
	    $(chartContainer).highcharts({
	        chart: {
	            type: 'column',
	            width: chartWidth,
				height: chartHeight,
				zoomType: 'x',
				style: {
					fontFamily: 'open_sansregular,sans-serif,Arial'				
				}
	        },
	        title: {
				text: title,
				 style: {
		             fontSize: '14px',
					 fontWeight: 'bold',
					 color: '#6a6a6a'
		         }
	        },
	        xAxis: {
	        	categories: categoriesVal,
				title : {
					text : xAxisTitle
				},
	        	overflow: 'justify',
	        	labels: {
	        		formatter: function() {
	        			return this.value.toString();
	        		},
	        		style: {
	        			color: '#606060',
	        			fontSize: '11px',
	        			lineHeight: '12px',
	        			left: '100px',
	        			top: '100px'
	        		}
	        	},
	        	autoRotation: false,
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
	                text: yAxisTitle
	            }
	        },
			colors : chartColorsObj,
	        tooltip: {
	            headerFormat: '<span style="font-size:10px"></span><table>',
	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                '<td style="padding:0"><b>{point.y}</b></td></tr>',
	            footerFormat: '</table>',
	            shared: true,
	            useHTML: true
	        },
	        plotOptions: {
	            column: {
	                pointPadding: 0.02,
	                borderWidth: 0
	            }
	        },
	        series: seriesObj
	    });		
	} else {$(chartContainer).html(nodataMsg);}
}

function constructWaterTankChart(chartContainer, chartInnerHTML, totalCount, statusCount) {
	console.log('Total Count: ' + totalCount + ' Status Count: ' + statusCount);
	 if(totalCount != '0') {
		 console.log('Inside IF...');
		 $(chartContainer).html('');
		 $(chartContainer).html(chartInnerHTML);
		 $(".water-level-circle").html('');
		 var completedPer = statusCount / totalCount * 100;
		 var whiteBackgroundPer = 100 - completedPer;
		 iG = $(".water-level-circle").attr("style");
		 inter = Number(iG.split("(")[1].split(" ")[1].split(",")[0].split("%")[0]);
		
		 setInterval(function() {
			 if(inter > whiteBackgroundPer) {
				 inter = inter - 3;
				 backStrWebkit = "-webkit-linear-gradient(top,white " + inter + "%,#61ba1d 0%)";
				 backStrMoz = "-moz-linear-gradient(top,white " + inter + "%,#61ba1d 0%)";
				 backStrOpera = "-o-linear-gradient(top,white " + inter + "%,#61ba1d 0%)";
				 backStrMs = "-ms-linear-gradient(top,white " + inter + "%,#61ba1d 0%)";
				 $(".water-level-circle").css({"background": backStrWebkit});
				 $(".water-level-circle").css({"background": backStrMoz});
				 $(".water-level-circle").css({"background": backStrOpera});
				 $(".water-level-circle").css({"background": backStrMs});
			 }
			 return
		 },200);
	 } else {
		 var nodata =
			    "<div class='no-user-stories-badge'>"+
			    "</div>";
		 $(chartContainer).html(nodata);
	}
}
