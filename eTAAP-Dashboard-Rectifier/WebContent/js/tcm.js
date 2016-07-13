function DropDown(el) {
	this.dd = el;
	this.initEvents();
}
/*DropDown.prototype = {
	initEvents : function() {
		var obj = this;
		obj.dd.on('click', function(event){
			$(this).toggleClass('active');
			event.stopPropagation();
		});	
	}
};*/
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

var prevQuaterEndDate = '';
var currentQuaterTitle = '';
var prevQuaterTitle = '';
$(document).ready(function() {
	$('#tcm').attr('class','active');
	$("#tcm").siblings().attr('class','deactive-link');
	$('#tcm-chart-container').show();
	$('#displayName').html('TCM');
	$('#period li').removeClass("selected-period");
	$('#period li').first().addClass("selected-period");
	$("#selected-period").text($(".selected-period").text());

	if ($(".selected-period").attr('startDt') != undefined)
		dateTcmField($(".selected-period").attr('startDt'), $(".selected-period").attr('enddt'));

	if ($("#period").find('li').last().attr('startdt') != undefined)
		prevQuaterEndDate = $("#period").find('li').last().attr('startdt').split(' ')[0];

	generateLeftChart(curQuaterJsonString,'ci-chart-container');
	generateRightChart(prevQuaterJsonString,'tcm-chart-container');
});

function generateLeftChart(quaterJsonString,chartId){
	currentQuaterTitle = '';
	prevQuaterTitle = '';
	currentQuaterTitle = $('#selected-period').html();
	prevQuaterTitle = getPrevQuaterTitle();
	var automatedTestCountCSV = '';
	var manualTestCountCSV = '';
	var suiteTypeNameCSV = '';
	var delimiter = ',';
	
	constructStackedColumnChart("#"+chartId, prevQuaterTitle, "480", "350", ['#a9ce8e', '#ff6666'], quaterJsonString[0].series, quaterJsonString[0].categories, "", "", "<p>No data available for this Quarter.<br/>To see the data - Please select different Quarter.");
}

function generateRightChart(quaterJsonString,chartId){
	currentQuaterTitle = '';
	prevQuaterTitle = '';
	currentQuaterTitle = $('#selected-period').html();
	prevQuaterTitle = getPrevQuaterTitle();
	//var automatedTestCountCSV= [];
	//var manualTestCountCSV = [];
	var categoriesValCSV = [];
	$("#period").find('li').each(function (){
		if( $(this).html() ==  currentQuaterTitle){
			var currentVal = $(this).attr('value');
			for(var i=currentVal; i < 4;i++){
				categoriesValCSV.push($('#periodId-'+i).html());
			}
			return false;
		}
	});
	
	constructMultiLineChart("#"+chartId, 'line', prevQuaterTitle, "480", "380", quaterJsonString[0].series, categoriesValCSV, "", "", "<p>No data available for this Quarter.<br/>To see the data - Please select different Quarter.",legendsArray['multiline_tcm']);
	
}

function fillTcmRightChart(response, chartId, automatedTestCountCSV, manualTestCountCSV,categoriesValCSV) {
	
	document.getElementById(chartId).innerHTML = "";
	var seriesObj = [{
        name: 'Automated',
        data: automatedTestCountCSV
    }, {
        name: 'Manual',
        data: manualTestCountCSV
    }];
	
	if (response != null && response.length > 0 && automatedTestCountCSV.length > 0) {
		$('#'+chartId).highcharts({
			chart: {
				width : 480,
				height : 380,
				style: {
	                margin: '0',
	                padding: '0',
	                fontFamily: 'open_sansregular,sans-serif,Arial'
	            }
			},
			title: {
				  text: prevQuaterTitle,
				  style: {
					  fontSize: '14px',
					  color:'#6a6a6a',
					  fontWeight: 'bold'
				  }
			},
	        xAxis: {
	            categories: categoriesValCSV
	        },
	        yAxis:{
	        	min:0
	        },
	        tooltip:{
			borderRadius	: 2,
		    borderWidth		: 1,
		    borderColor		: '#999',
			shadow			: false,
		    shared			: true,
		    useHTML			: true,
			yDecimals		: 0,
            valueDecimals	: 0,
            formatter: function() {
                var points = '<span style="padding: 15px 0 5px 10px; display: block; font-size: 12px; line-height:13px;"><span></span>'+this.x+'</span><table>';
                var totalCount = 0;
                $.each(this.points,function(i,point){
                	totalCount += point.y;
                	points+='<tr><td style="padding:0 0 0 10px; display: block;font-size: 12px; margin:0; line-height:13px;">'+point.series.name+':</td><td style="margin:0;padding:0; line-height:13px;">'+point.y+'</td></tr>';
                });
                points+='<tr><td style="padding:0 0 0 10px; display: block;font-size: 12px; margin:0; line-height:13px;"><b>Total: </b></td>'
        		+'<td style="text-align:right;font-size: 12px; margin:0; line-height:13px;"><b>'+totalCount+'</b></td></tr>'
            	+'</table>';
                return points;
            }
           },
	        legend: {
	            layout: 'vertical',
	            floating: true,
	            align: 'right',
	            verticalAlign: 'top',
	            borderWidth: 0,
	            x:0,
	            y:-5
	        },
	        series: seriesObj
		});
	} else {
		$('#'+chartId).highcharts({
			chart : {
				width : 480,
				height : 380
			},
			title : {
				text : 'No data available for this Quarter. <br/> To see the data - please select a different Quarter',
				align : 'left',
				x : 100,
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
	}
}

function fillTcmLeftChart(response, chartId, automatedTestCountCSV, manualTestCountCSV,suiteTypeNameCSV) {
	console.log(automatedTestCountCSV);
	document.getElementById(chartId).innerHTML = "";
	if(chartId == 'tcm-chart-container'){
		suiteTypeNameCSV = "";
	}
	var automatedTestCount = automatedTestCountCSV.toString().split(',').map(function(item) {
		return parseInt(item, 10);
	});
	/*automatedTestCount = automatedTestCount.map(function (item) {
	    return (item == 0) ? null : item;
	});*/
	var manualTestCount = manualTestCountCSV.toString().split(',').map(function(item) {
		return parseInt(item, 10);
	});
	/*manualTestCount = manualTestCount.map(function (item) {
	    return (item == 0) ? null : item;
	});*/
	var xaxisCatg = suiteTypeNameCSV.toString().split(',').map(function(item) {
		return item;
	});
	/*var perShapeGradient = {
		x1 : 0,
		y1 : 0,
		x2 : 1,
		y2 : 1
	};*/
	if (response != null && response.length > 0) {
		$('#'+chartId)
				.highcharts({
							chart : {
								type : 'column',
								width : 480,
								height : 350,
//								zoomType : 'x',
								style: {
									fontFamily: 'open_sansregular,sans-serif,Arial'
								}
									
							},
							/*colors : [
									{
										linearGradient : perShapeGradient,
										stops : [ [ 0, 'rgb(136, 219, 5)' ],
												[ 1, 'rgb(86, 138, 3)' ] ]
									},
									{
										linearGradient : perShapeGradient,
										stops : [ [ 0, 'rgb(255, 120, 133)' ],
												[ 1, 'rgb(182, 49, 49)' ] ]
									} ],*/
							colors : ['#a9ce8e', '#ff6666'],
							tooltip : {
								enabled : false
							},
							title: {
								  style: {
									  fontSize: '14px',
									  color: '#6a6a6a',
									  fontWeight: 'bold'
								  },
								  text: (function() {
								    if (chartId == 'ci-chart-container') {
								      return currentQuaterTitle;
								    } else {
								      return prevQuaterTitle;
								    }
								  })()
							},
							subtitle : {
								text : ''
							},
							xAxis : {
								categories : xaxisCatg,
								title : {
									text : (function() {
								    if (chartId == 'ci-chart-container') {
									      return 'Suite Types';
									    } else {
									      return 'Total Count';
									    }
									})()
								}
							},
							yAxis : {
								min : 0,
								minTickInterval: 100,
								title : {
									text : 'Test Case Count'
								}
							},tooltip:{
								borderRadius	: 2,
							    borderWidth		: 1,
							    borderColor		: '#999',
								shadow			: false,
							    shared			: true,
							    useHTML			: true,
								yDecimals		: 0,
					            valueDecimals	: 0,
					            formatter: function() {
					                var points = '<span style="padding: 15px 0 5px 10px; display: block; font-size: 12px; line-height:13px;">Suite Type: <span></span>'+this.x+'</span><table>';
					                $.each(this.points,function(i,point){
					                	points+='<tr><td style="padding:0 0 0 10px; display: block;font-size: 12px; margin:0; line-height:13px;">'+point.series.name+':</td><td style="margin:0;padding:0; line-height:13px;">'+point.y+'</td></tr>';
					                });
					                points+='<tr><td style="padding:0 0 0 10px; display: block;font-size: 12px; margin:0; line-height:13px;"><b>Total: </b></td>'
					        		+'<td style="text-align:right;font-size: 12px; margin:0; line-height:13px;"><b>'+this.points[0].total+'</b></td></tr>'
					            	+'</table>';
					                return points;
					            }
							},plotOptions: {
							    column: {
							        stacking: 'normal',
							        dataLabels: {
							            //enabled: true,
							            color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
							            style: {
							                textShadow: '0 0 3px black'
							            }
							        } 
							    }
							},
							series : [ {
								name : 'Automated',
								data : automatedTestCount
							}, {
								name : 'Manual',
								data : manualTestCount
							} ]
						});
	} else {
		$('#'+chartId).highcharts({
			chart : {
				width : 480,
				height : 380
			},
			title : {
				text : 'No data available for this Quarter. <br/> To see the data - please select a different Quarter',
				align : 'left',
				x : 100,
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
	}
}
function getTcmResultsAjaxCall(which,mapIdParams){
	
	var locationHref = window.location.href;
	var locationHrefArr = locationHref.split("/");
	var appId = locationHrefArr[5];
	var appName = locationHrefArr[6];
	var mapIdRetrieveSplit = locationHrefArr[7].split(".");
	var from;
	var to;
	var mapId = mapIdRetrieveSplit[0];
	var responseType = mapIdRetrieveSplit[1].split("?")[0];
	$('#periodStrtDt').val(mapIdParams.getAttribute('startdt'));
	$('#periodEndDt').val(mapIdParams.getAttribute('enddt'));
	from = mapIdParams.getAttribute('startdt').substr(0,10);
	to = mapIdParams.getAttribute('enddt').substr(0,10);
	$('#selected-period').html(mapIdParams.innerHTML);
	
	setPeriodLabel(mapIdParams.getAttribute('startdt'),mapIdParams.getAttribute('enddt'));
	
	var uri = contextPath+"/app/"+appId+"/"+appName+"/"+mapId+"."+responseType;
	var dataValue = "from="+from+"&to="+to+"&prevQuaterEndDate="+prevQuaterEndDate;
	$.ajax({
		url : uri,
		type : "POST",
		data:dataValue+"&mapId="+mapId,
		success : function(result) {
			var delimiter = "^^^";
			var resultArr = result.split(delimiter);
			generateLeftChart(jQuery.parseJSON(resultArr[0])  == null ? '' : jQuery.parseJSON(resultArr[0]),'ci-chart-container');
			generateRightChart(jQuery.parseJSON(resultArr[1])  == null ? '' : jQuery.parseJSON(resultArr[1]),'tcm-chart-container');
		}
	});
	pushtoBrowserHistory(uri+"/?"+"from="+from+"&to="+to);
}

function dateTcmField(startDt, endDt) {
    var startDate = new Date(startDt.substring(0, startDt.indexOf(" ")));
    var endDate = new Date(endDt.substring(0, endDt.indexOf(" ")));

    var sd = startDate.toDateString();
    var ed = endDate.toDateString();

    $("#selected-time").text(sd.substring(sd.indexOf(" ")) + " - " + ed.substring(ed.indexOf(" ")));
}

function getPrevQuaterTitle(){
	var prevQuaterStr = '';
	$("#period").find('li').each(function (){
		if( $(this).html() ==  currentQuaterTitle){
			var currentVal = $(this).attr('value');
			for(var i=currentVal; i < 4;i++){
				prevQuaterStr += $('#periodId-'+i).html();
				if(i != 3){
					prevQuaterStr += ' + ';
				}
			}
			return false;
		}
	});
	var prevQuaterArr = prevQuaterStr.split('+');
	if(prevQuaterArr[prevQuaterArr.length-1] != prevQuaterArr[0]){
		prevQuaterStr = prevQuaterArr[prevQuaterArr.length-1]+' to '+prevQuaterArr[0];
	}
	return prevQuaterStr; 
}
