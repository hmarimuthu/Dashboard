if (document.layers) {
	document.captureEvents(Event.KEYDOWN);
}



function formatAMPM(date) {
  var hours = date.getHours();
  var minutes = date.getMinutes();
  var ampm = hours >= 12 ? 'pm' : 'am';
  hours = hours % 12;
  hours = hours ? hours : 12; // the hour '0' should be '12'
  minutes = minutes < 10 ? '0'+minutes : minutes;
  var strTime = hours + ':' + minutes + ' ' + ampm;
  return strTime;
}

function formatUTCAMPM(date) {
	  var hours = date.getUTCHours();
	  var minutes = date.getUTCMinutes();
	  var ampm = hours >= 12 ? 'pm' : 'am';
	  hours = hours % 12;
	  hours = hours ? hours : 12; // the hour '0' should be '12'
	  minutes = minutes < 10 ? '0'+minutes : minutes;
	  var strTime = hours + ':' + minutes + ' ' + ampm;
	  return strTime;
	}


var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
                  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

function getFormattedDate(date) {
//  return date.getDate()+"/"+monthNames[date.getMonth()]+"/"+date.getFullYear()+" "+formatAMPM(date);
  return date.getUTCDate()+"/"+monthNames[date.getUTCMonth()]+"/"+date.getUTCFullYear()+" "+formatUTCAMPM(date);
}


function getURIParameter(param, asArray) {

    return document.location.search.substring(1).split('&').reduce(function(p,c) {
        var parts = c.split('=', 2).map(function(param) { return decodeURIComponent(param); });
        if(parts.length == 0 || parts[0] != param) return (p instanceof Array) && !asArray ? null : p;
        return asArray ? p.concat(parts.concat(true)[1]) : parts.concat(true)[1];
    }, []);
}

function getURLParameters(paramName)
{
    var sURL = window.document.URL.toString();
    if (sURL.indexOf("?") > 0)
    {
        var arrParams = sURL.split("?");
        var arrURLParams = arrParams[1].split("&");
        var arrParamNames = new Array(arrURLParams.length);
        var arrParamValues = new Array(arrURLParams.length);

        var i = 0;
        for (i = 0; i<arrURLParams.length; i++)
        {
            var sParam =  arrURLParams[i].split("=");
            arrParamNames[i] = sParam[0];
            if (sParam[1] != "")
                arrParamValues[i] = unescape(sParam[1]);
            else
                arrParamValues[i] = "No Value";
        }

        for (i=0; i<arrURLParams.length; i++)
        {
            if (arrParamNames[i] == paramName)
            {
                return arrParamValues[i];
            }
        }
        return "Not Found";
    }
}

$(document).ready(function(){
	
	if(getURLParameters('month_Name') != 'Not Found'){
		if(getURIParameter('month_Name') != ""){
			$('#selected_monthId').html(getURIParameter('month_Name'));
		}
	} 
	
	$().dropdown_select();
	$().avoid_specific_characters();
	$().maxlength();
	$('.setting-list li').off('click').on('click',function(){
		window.location = $(this).find('a').attr('href');
	});
	function DropDown(el) {
		this.dd = el;
		this.initEvents();
	}
	DropDown.prototype = {
		initEvents : function() {
			var obj = this;
			obj.dd.off('click').on('click', function(event){
				var idRef = $(this).attr('id');
				$('#dd, #prjctDd, #settings').each(function(){
					if($(this).attr('id') != idRef){
						$(this).removeClass('active');
					}
					$('.dropdown-menu').removeClass('active');
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
	$('.settings-container ul li').on('hover',function(){
		$(this).css('background-color','#99be7c');
		$(this).siblings().css('background-color','white');
	});
	$('#other-settings').hover(function() {
		$('#pop-up').css("display", "block");
	   }, function() {
		   $('#pop-up').css("display", "none");
	   }
	);
	$('#pop-up').hover(function() {
		$('#other-settings').css('background-color','#99be7c');
		$('#pop-up').css("display", "block");
	   }, function() {
		   $('#pop-up').css("display", "none");
	   }
	);

	$('#dd, #prjctDd').mouseleave(function() {
		$('#dd, #prjctDd').removeClass('active');
	});

	$('.settings-container').mouseleave(function() {
		$('#settings').removeClass('active');
	});
});
function isNameExist(type, id, name) {
	if (id == undefined) {
		id = 0;
	}

	var uri = contextPath + "/isNameExist?type=" + type;
	var param = "&id=" + id + "&name=" + name;
	var flag = "false";

    $.ajax({
    	async: false,
        type: "POST",
        url: uri,
        data: param,
        dataType: "text",
        success: function (result) {
        	flag = result;
        },
        "error": function (result) {
            var response = result.responseText;
            //alert('Error loading: ' + response);
        }
    });

    return flag;
}

function disableRecordForApp(mapids) {
	 
	 var uri = contextPath + "/disableRecordForApp?mapids=" + mapids;
	 //var param = "&id=" + id + "&name=" + name;
	 var flag = "false";
	 
	    $.ajax({
	     async: false,
	        type: "POST",
	        url: uri,
	        //data: param,
	        dataType: "text",
	        success: function (result) {
	         flag = result;
	        },
	        "error": function (result) {
	            var response = result.responseText;
	            //alert('Error loading: ' + response);
	        }
	    });
	 
	    return flag;
	}

function trimText(id, fixedLen) {
	$('[id^='+id+']').each(function(i) {
		var actualLen = $(this).text().length;
		if(actualLen > fixedLen) {
			$(this).attr('title', $(this).text());
			$(this).text($(this).text().substr(0, fixedLen)+'...');
		}
	});
}
// For sorting
function sortingHead(thatRef,orderType){
	if(orderType == 'desc'){
		if($(thatRef).html().trim() == 'Active'){
			$(thatRef).next().find('.down-arrow').click();
		}else{
			$(thatRef).next().find('.up-arrow').click();
		}
	}else{
		if($(thatRef).html().trim() == 'Active'){
			$(thatRef).next().find('.up-arrow').click();
		}else{
			$(thatRef).next().find('.down-arrow').click();
		}
	}
}

(function ($) {
	//Important point for this function is , in this the characters have to be in order , and if they are not in order it will throw error.
	//This function also requires every input to have a specialChar variable which will specify the characters to be ignored in order
    $.fn.avoid_specific_characters = function(){
	 	$("#sysName, #appName, #envName, #suiteName, #bedName").keypress(function() {
 			var ignore_code = $(this).attr("specialChar");
 			var pattr = "A-Za-z0-9"+"\\s"+ignore_code
 			var pattern_to_match =  "[^"+pattr+"]";
 			
  		if (key != 8 && String.fromCharCode(e.which).match(new RegExp(pattern_to_match))) {
    		e.preventDefault();
    		alert("Special characters are not allowed. Use 'A-Z', 'a-z', '0-9', '_', '-' and 'space'.");
   		}
  		
 	});
  }
})(jQuery);


(function ($) {
    $.fn.maxlength = function(){
        $("textarea[maxlength], input[maxlength]").keypress(function(event){ 
            var key = event.which;
            //all keys including return.
            if(key >= 33 || key == 13 || key == 32) {
                var maxLength = $(this).attr("maxlength");
                var length = this.value.length;
                if(length >= maxLength) {                     
                    event.preventDefault();
                }
            }

            $("textarea[maxlength], input[maxlength]").keyup(function (event) {
    		 var max = $(this).attr("maxlength");
   			 var len = $(this).val().length;
   			 var char = max - len;
    		$('#textleft').text(char + ' characters left');
  			});
        });
    }
})(jQuery);

(function ($) {
    $.fn.dropdown_select = function(){
        all_dropdown_links = ["jira_qa_redesign", "jenkins_qa_redesign", "TCM", "velocity", "iterations"];
		var a = document.URL.split("/");
		b = [{TCM:"TCM", jira_qa_redesign:"Defects", jenkins_qa_redesign:"CI Results", velocity:"Velocity", jira_dev_redesign:"Iterations"}];
		// $(this).parent().parent().removeClass("deactive-link");
		$(".main-nav .subnav li").each(function(){
			if ($(this).text() == b[0][a[6]]){
				$(".main-nav .subnav").find('li').removeClass();
				$(this).addClass("active-link");
				($(this).parent().parent().addClass("active"));
			}
		});
    }
})(jQuery);