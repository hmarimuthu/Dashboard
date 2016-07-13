function DropDown(el) {
	this.dd = el;
	this.initEvents();
}

DropDown.prototype = {
	initEvents : function() {
		var obj = this;
		obj.dd.off('click').on('click', function(event) {
			var idRef = $(this).attr('id');
			$('#dd, #prjctDd, #settings').each(function() {
				if($(this).attr('id') != idRef) {
					$(this).removeClass('active');
				}
			});
			$(this).toggleClass('active');
			event.stopPropagation();
		});
	}
};



$(function() {
	var dd = new DropDown($('#dd, #prjctDd, #settings'));
	$(document).click(function() {
		$('.iteration-options-container').removeClass('active');
		$('.project-list').removeClass('active');
		$('#settings').removeClass('active');
	});
});


$(document).click(function() {
	$('#dd, #prjctDd, #settings').removeClass('active');
});

$('#other-settings').hover(function() {
	$('#pop-up').css("display", "block");
});

$('#other-settings').mouseout(function() {
	$('#pop-up').css("display", "none");
});

$('#other-settings').mousemove(function() {
	$('#pop-up').css("display", "block");
});

$('#pop-up').hover(function() {
	$("#pop-up").css("display", "none");
});

$('#pop-up').mouseleave(function() {
	$('#pop-up').css("display", "block");
});


$(document).ready(function() {
	
	$('#velocity').attr('class','active');
	$("#velocity").siblings().attr('class','deactive-link');
	$('#datewiseCommitedCompletedUserStories').show();
	$('#displayName').html('VELOCITY');
	$('#period li').removeClass("selected-period");
	$('#period li').first().addClass("selected-period");
//	$("#selected-period").text($(".selected-period").text());
	

	if ($(".selected-period").attr('startDt') != undefined)
		dateVelocityField($(".selected-period").attr('startDt'), $(".selected-period").attr('enddt'));

	if ($("#period").find('li').last().attr('startdt') != undefined)
		prevQuaterEndDate = $("#period").find('li').last().attr('startdt').split(' ')[0];


	retrievedIterationsChartResults(user_stories_json);
	waterTankProcessJsonString(user_stories_json);
	iterationChartAjaxCall(1);
	
});

function waterTankProcessJsonString(json){
	var _json=json;
	if(_json!=null && _json.length>0){
		if(_json[1]!=undefined){
			if(_json[1].title === "User Stories Count"){
				var chartContainerName="watertank-chart-container";
				var charInnerHtml="";
				var totalCount="";
				var statusCount="";
				statusCount = _json[1].status_Count;
				totalCount = json[1].total_Count;
				_waterTankChart(chartContainerName, charInnerHtml, totalCount, statusCount);
			}
		}
	}
	
	
}

function iterationChartAjaxCall(sprintId) {
	var locationHref = window.location.href;
	var locationHrefArr = locationHref.split("/");
	var appId = locationHrefArr[5];
	getCiResultsAjaxCall(appId, sprintId);
	 $("#selected-period").text($("#sprintId-"+sprintId).text());
}

function getCiResultsAjaxCall(appId,sprintId)
{       
 var uri = contextPath+"/app/"+appId+"/jira_dev_redesign/iterations/"+appId+".html";
 var dataValue = "sprintId="+sprintId;
 
 $.ajax({url:uri,
  type: "POST", 
  data:dataValue,
  success: function(result){
  
  parseToJsonAndDisplayChartResults(result);
 },error: function(request, error){
  console.log(arguments);
  //alert("Ajax Error "+ request+" "+error);
 }
 
 });
 pushtoBrowserHistory(uri+"?"+dataValue);
}


function retrievedIterationsChartResults(jsonRes)
{
	
	if(jsonRes[0] !== undefined && jsonRes[0] !== null ){
		
		document.getElementById('datewiseCommitedCompletedUserStories').innerHTML = "";
		document.getElementById('burndownPerSprintChartContainer').innerHTML = "";
		//constructSplineChart('#datewiseCommitedCompletedUserStories', 'User Stories', jsonRes[0].series, jsonRes[0].categories,'Date', 'Count', 'No data available for this Sprint. <br/> To see the data - please select different Sprint') 
		constructMultiLineChart('#datewiseCommitedCompletedUserStories', 'spline', 'User Stories',1092,400,jsonRes[0].series ,jsonRes[0].categories,'Date', 'Count', 'No data available for this Sprint. <br/> To see the data - please select different Sprint',legendsArray['multiline_iterations']);
		//chartHeight, seriesObj, categoriesVal, xAxisTitle, yAxisTitle, nodataMsg)
		burndownChart(jsonRes);
	}
}


function parseToJsonAndDisplayChartResults(jsonRes)
{
	document.getElementById('datewiseCommitedCompletedUserStories').innerHTML = "";
	document.getElementById('burndownPerSprintChartContainer').innerHTML = "";
	//document.getElementById('watertank-chart-container').innerHTML = "";
	console.log("jsonRes"+jsonRes);
	var jsonResult = jQuery.parseJSON(jsonRes);
	var  iterationData = jsonResult.data.iterationJson;
	constructSplineChart('#datewiseCommitedCompletedUserStories', 'User Stories', iterationData[0].series, iterationData[0].categories,'Date', 'Count', 'No data available for this Sprint. <br/> To see the data - please select different Sprint') 
	burndownChart(jsonResult);
	waterTankProcessJsonString(iterationData);
}

function burndownChart(result) {
	if (result.data != undefined) {
		var titleText = result.data.burndownChartJson.titleText;
		var subTitleText = result.data.burndownChartJson.subTitleText;
		var xAxisSprintStartDateTime = result.data.burndownChartJson.xAxisSprintStartDateTime;
		var xAxisMaxDate = result.data.burndownChartJson.xAxisMaxDate;
		var yAxisMaxStoryPoints = result.data.burndownChartJson.yAxisMaxStoryPoints;
		var seriesObj = result.data.burndownChartJson.series;

		if(seriesObj.length > 0) {
		    Highcharts.setOptions({
		        global: {
		        	useUTC: true
		        }
		    });
			
			$('#burndownPerSprintChartContainer').highcharts({
				chart: {
				zoomType: 'xy',
					style: {
		    			fontFamily: 'open_sansregular,sans-serif,Arial'
		    		}
				},
			    title: {
                  text: 'Burndown Chart',
			      x: -10, //center
		          style: {
		            	color: '#6a6a6a',
		            	fontSize: '14px',
		            	fontWeight: 'bold'	
		            }
			    },
			    scrollbar: {
			                barBackgroundColor: 'gray',
			                barBorderRadius: 7,
			                barBorderWidth: 0,
			                buttonBackgroundColor: 'gray',
			                buttonBorderWidth: 0,
			                buttonBorderRadius: 7,
			                trackBackgroundColor: 'none',
			                trackBorderWidth: 1,
			                trackBorderRadius: 8,
			                trackBorderColor: '#CCC'
			            },
			    colors: ['blue', 'red'],
			    plotOptions: {
			      line: {
			        lineWidth: 3
			      },
			      tooltip: {
			        hideDelay: 200
			      }
			    },
			    subtitle: {
			     // text: subTitleText,
			      x: -10
			    },
			    xAxis: {
					type: 'datetime',
					min: xAxisSprintStartDateTime, 
					max: xAxisMaxDate, 
/*		            startOnTick: true,
		            endOnTick: true,
		            tickmarkPlacement: 'on',					
*//*					dateTimeLabelFormats: {
						day: '%e %b'   
					},
*/
/*					tickInterval: 86400000,
					tickmarkPlacement: 'between',
*/
//					startOnTick: true,
//		            endOnTick: true,
		            tickmarkPlacement: 'on',
					labels: { 
			            formatter: function() {
							var dateToFrt1 = new Date(this.value);
 			          	    var n = dateToFrt1.getTimezoneOffset();
 			          	    var value1 = this.value + n; 
 			          	    dateToFrt1 = new Date(value1); 
							var formattedDate = dateToFrt1.getUTCDate()+"<br>"+monthNames[dateToFrt1.getUTCMonth()];
//							alert(formattedDate);
				            return formattedDate;
			            }
		            }
				},
			    yAxis: {
					title: {
						text: 'Story Points'
					},
					type: 'linear',
					min: 0,
					max: yAxisMaxStoryPoints,
					allowDecimals: false
			    },
			    
			    tooltip: {
			      crosshairs: false,
			      shared: false,
					formatter: function() {
			            var result = "";
						var dateToFrt = new Date(this.x);
//						var formattedDate = dateToFrt.toDateString();
						var formattedDate = getFormattedDate(dateToFrt);
						if(this.series.name == 'Ideal Burn') {
//							result = dateFormat +'<br>'+ this.y+'<br>'+this.point.addedRemoved;
							result = formattedDate +'<br>'+this.point.addedRemoved;
//							result = formattedDate+'<br>'+"Story Point: "+this.y;
						}
						else {
//							result = this.x+'<br>'+this.y+ '<br>' + this.point.keys+ '<br>'+ this.point.addedRemoved;
//							result = formattedDate+'<br>'+this.y+ '<br>' + this.point.keys+ '<br>'+ this.point.addedRemoved;
							result = this.point.keys+'<br>'+this.point.addedRemoved+'<br>'+formattedDate+'<br>';
						}
			            return result;
					}
			    },
			    legend: {
			     layout: 'horizontal',
			      align: 'center',
			      verticalAlign: 'bottom',
			      borderWidth: 0
			    },
			    series: seriesObj
			});				
		    
		}
		 else {
			 noDataAvailForChartDefaultDisplay('burndownPerSprintChartContainer', 'No data available for this Sprint. <br/> To see the data - please select different Sprint');
		}
	} else {
		noDataAvailForChartDefaultDisplay('burndownPerSprintChartContainer', 'No data available for this Sprint. <br/> To see the data - please select different Sprint');
	}
}


function _waterTankChart(chartContainerName, charInnerHtml, totalCount, statusCount){
	var userStoriesCountCompleted = statusCount;
	var userStoriesCountAll = totalCount;

	 if(userStoriesCountAll != '0') {
		 
		// $("#total_stories").text(userStoriesCountAll);
		// $("#user_stories").text(userStoriesCountCompleted);
		 
		 $("#watertank-chart-container").html('');
		// var waterinnerHTML = '<section><article style="width:95%"><p class="graph-name"><span class="graph-text-left">User Stories</span><span class="status-icon-yellow"></span></p><div class="iframe-container"><div class="container-circle"><div class="inner-circle"><div class="us-details"><h3 class="user-story-number"><span id="user_stories">'+userStoriesCountCompleted+'</span></h3><div class="total-us-count"><p class="us-count-text">OUT OF</p><div class="us-horz-separator"></div><h4 id="total_stories" class="us-count">'+userStoriesCountAll+'</h4></div><div class="clear"></div></div><div class="clear"></div><div style="background: -moz-linear-gradient(center top , white 7%, rgb(0, 146, 231) 0%) repeat scroll 0% 0% transparent;" class="water-level-circle"></div><p class="user-story-text">User Stories Completed</p></div></div></div></article><div class="clear"></div></section>';
		 var waterinnerHTML = '<section><article style="width:95%; background: #fff;"><div class="watertank-title-container"><p class="graph-name"><span class="graph-text-left">User Stories</span><span class="status-icon-yellow"></span></p></div><div class="iframe-container"><div class="container-circle"><div class="inner-circle"><div class="us-details"><h3 class="user-story-number"><span id="user_stories">'+userStoriesCountCompleted+'</span></h3><div class="total-us-count"><p class="us-count-text">OUT OF</p><div class="us-horz-separator"></div><h4 class="us-count" id="total_stories">'+userStoriesCountAll+'</h4></div><div class="clear"></div></div><div class="clear"></div><div class="water-level-circle" style="background: -webkit-linear-gradient(top,white 100%,purple 10%);background: -moz-linear-gradient(top,white 100%,purple 10%);background: -o-linear-gradient(top,white 100%,purple 10%);background: -ms-linear-gradient(top,white 100%,purple 10%);"></div><p class="user-story-text">User Stories Completed</p></div></div></div></article><div class="clear"></div></section>';
		 $("#watertank-chart-container").html(waterinnerHTML);
		 $(".water-level-circle").html('');
		 
		    var completedPer = userStoriesCountCompleted / userStoriesCountAll * 100;
		  
			whiteBackgroundPer = 100 - completedPer;       
			 
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
				
					 
	 }
	 else {
			noDataAvailForChartDefaultDisplay('watertank-chart-container', 'No data available for this Sprint. <br/> To see the data - please select different Sprint');
	}
}

function waterTankChart(chartContainerName,userStoriesJsonString){
	console.log(userStoriesJsonString);
	if(userStoriesJsonString.data != null) {
		 userStoriesCountAll =  userStoriesJsonString.data.Sprint.userStoriesCountAll;
		 userStoriesCountCompleted = userStoriesJsonString.data.Sprint.userStoriesCountCompleted;
		
		 if(userStoriesCountAll != '0') {
			 
			// $("#total_stories").text(userStoriesCountAll);
			// $("#user_stories").text(userStoriesCountCompleted);
			 
			 $("#watertank-chart-container").html('');
			// var waterinnerHTML = '<section><article style="width:95%"><p class="graph-name"><span class="graph-text-left">User Stories</span><span class="status-icon-yellow"></span></p><div class="iframe-container"><div class="container-circle"><div class="inner-circle"><div class="us-details"><h3 class="user-story-number"><span id="user_stories">'+userStoriesCountCompleted+'</span></h3><div class="total-us-count"><p class="us-count-text">OUT OF</p><div class="us-horz-separator"></div><h4 id="total_stories" class="us-count">'+userStoriesCountAll+'</h4></div><div class="clear"></div></div><div class="clear"></div><div style="background: -moz-linear-gradient(center top , white 7%, rgb(0, 146, 231) 0%) repeat scroll 0% 0% transparent;" class="water-level-circle"></div><p class="user-story-text">User Stories Completed</p></div></div></div></article><div class="clear"></div></section>';
			 var waterinnerHTML = '<section><article style="width:95%; background: #fff;"><div class="watertank-title-container"><p class="graph-name"><span class="graph-text-left">User Stories</span><span class="status-icon-yellow"></span></p></div><div class="iframe-container"><div class="container-circle"><div class="inner-circle"><div class="us-details"><h3 class="user-story-number"><span id="user_stories">'+userStoriesCountCompleted+'</span></h3><div class="total-us-count"><p class="us-count-text">OUT OF</p><div class="us-horz-separator"></div><h4 class="us-count" id="total_stories">'+userStoriesCountAll+'</h4></div><div class="clear"></div></div><div class="clear"></div><div class="water-level-circle" style="background: -webkit-linear-gradient(top,white 100%,purple 10%);background: -moz-linear-gradient(top,white 100%,purple 10%);background: -o-linear-gradient(top,white 100%,purple 10%);background: -ms-linear-gradient(top,white 100%,purple 10%);"></div><p class="user-story-text">User Stories Completed</p></div></div></div></article><div class="clear"></div></section>';
			 $("#watertank-chart-container").html(waterinnerHTML);
			 $(".water-level-circle").html('');
			 
			    var completedPer = userStoriesCountCompleted / userStoriesCountAll * 100;
			  
				whiteBackgroundPer = 100 - completedPer;       
				 
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
					
						 
		 }
		 else {
				noDataAvailForChartDefaultDisplay('watertank-chart-container', 'No data available for this Sprint. <br/> To see the data - please select different Sprint');
		}	
	} else {
		noDataAvailForChartDefaultDisplay('watertank-chart-container', 'No data available for this Sprint. <br/> To see the data - please select different Sprint');
	}
}

function noDataAvailForChartDefaultDisplay2(name, msg) {
	$('#'+name).highcharts( {
		 chart : {
			//width : 780,
			 height : 380
		 },
		 title : {
			 text : msg,
			 align : 'left',
			 x : 180,
			 y : 180,
			 floating : true
		 },
		 noData : {
			 style : {
				 fontWeight : 'bold',
				 fontSize : '15px',
				 color : '#303030'
			 }
		 }
	 });
	/* var nodata =
		    "<div class='no-user-stories-badge'>"+
		    "</div>";
	 $('#'+name).html(nodata);*/
}
