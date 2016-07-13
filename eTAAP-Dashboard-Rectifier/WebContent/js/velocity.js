
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
	
	$('#velocity').attr('class','active');
	$("#velocity").siblings().attr('class','deactive-link');
	$('#velocity-chart-container').show();
	$('#displayName').html('VELOCITY');
	$('#period li').removeClass("selected-period");
	$('#period li').first().addClass("selected-period");
	$("#selected-period").text($(".selected-period").text());

	if ($(".selected-period").attr('startDt') != undefined)
		dateVelocityField($(".selected-period").attr('startDt'), $(".selected-period").attr('enddt'));

	if ($("#period").find('li').last().attr('startdt') != undefined)
		prevQuaterEndDate = $("#period").find('li').last().attr('startdt').split(' ')[0];

	
	generateChart(curQuaterJsonString,'velocity-chart-container');
	
	
});


function generateChart(quaterJsonString,chartId){
	if(quaterJsonString[0] !== undefined && quaterJsonString[0] !== null ){
		 currentQuaterTitle = '';
		 prevQuaterTitle = '';
		 currentQuaterTitle = $('#selected-period').html();
		 constructColumnChart("#"+chartId, '', "1092", "380", ['#a9ce8e', '#ff6666'], quaterJsonString[0].series, quaterJsonString[0].categories, "", "", "<p>No data available for this Quarter.<br/>To see the data - Please select different Quarter.");
	}
}

function getVelocityResultsAjaxCall(which,mapIdParams){
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
	
	$.ajax({url:uri,
		type: "POST",
		data:dataValue+"&mapId="+mapId,
		success:function(response)
			{
			var jsonResult = jQuery.parseJSON(response);
			constructColumnChart("#velocity-chart-container", '', "1092", "380", ['#a9ce8e', '#ff6666'], jsonResult[0].series, jsonResult[0].categories, "", "", "<p>No data available for this Quarter.<br/> To see the data - Please select different Quarter.");
			
		}});
	
	pushtoBrowserHistory(uri+"/?"+"from="+from+"&to="+to);
}

function retrievedVelocityResults(response)
{
	document.getElementById('velocity-chart-container').innerHTML = "";
	var jsonResult = jQuery.parseJSON(response);
	var velocity = jsonResult.data.Velocity;
	velocityChart(response, velocity.estimatedCSV, velocity.completedCSV, velocity.sprintNameCSV);
}


function dateVelocityField(startDt, endDt) {
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
