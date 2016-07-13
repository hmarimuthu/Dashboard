<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
	<title>eTAAP Dashboard - CI Resultsssss</title>
	<script type="text/javascript" src='<c:url value="/js/jquery_1.8.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/highcharts.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/modernizr_custom.2.6.2.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/common.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/getDataByAjax_redesign.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/ci_chart_redesign.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/myAjax.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/chart_common.js" />'></script>
	<script type="text/javascript" >
		var test = "";
		var systemApi = '${systemAPI}';
	</script>
	<c:if test="${not empty jsonString}">
	    <script type="text/javascript" >
	    	test = '${envJsonString}';
	    	var jsonObj = '${jsonString}';
		</script>
	</c:if>
	<link type="text/css" rel="stylesheet" href='<c:url value="/css/design.css" />' />
	<link type="text/css" rel="stylesheet" href='<c:url value="/fonts/open-sans/stylesheet.css" />' />
	<link type="text/css" rel="stylesheet" href='<c:url value="/css/etaap.css" />' />
</head>
<body onload="init();">
	<div class="main-content-container">
	<!-- main content container starts here -->
		<div class="dashboard-container">
		<form id="default_form" action="filter" method="post">
			<!-- header starts here -->
			<header>
        		<%@ include file="./header.jsp" %>
			</header>
			<!-- header ends here -->
            <div class="clear"></div>

	    	<!-- content container starts here -->
	    	<section>
	    		<!-- left content container starts here -->
	        	<aside>
					<%@ include file="./left_nav.jsp" %>
				</aside>
				<!-- left content container ends here -->

	        	<!-- right content container starts here -->
	        	<article>
					<%@ include file="./right_content.jsp" %>
	        	</article>
	        	<!-- right content container ends here -->
	            <div class="clear"></div>
	    	</section>
	    	<!-- content container ends here -->

	        <!-- footer starts here -->
			<footer>
				<%@ include file="./footer.jsp" %>
	        </footer>
	        <!-- footer ends here -->
		</form>
		</div>
	<!-- main content container ends here -->
	</div>
	<script type="text/javascript">
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

		function doFormSubmit(clickedType, clickedCell) {
			filterOption(clickedType, clickedCell);

			/*var appId = $('.selected-app').val();
		    var envId = $('.selected-env').val();
		    var suiteId = $('.selected-suite').val();
		    var bedId = $('.selected-bed').val();
		    var periodId = $('.selected-period').val();*/

		    /*alert(appId +":"+envId+":"+suiteId+":"+bedId+":"+periodId);*/

		    /*$.ajax({
				type : "Post",
			    url : "dropdown",
				cache : false,
			    data : "appId=" + appId + "&envId=" + envId + "&suiteId=" + suiteId + "&bedId=" + bedId + "&periodId=" + periodId,
				success : function() {
					ciChart(${csvForCIChart.buildNumberCSV}, ${csvForCIChart.passCountCSV}, ${csvForCIChart.failCountCSV}, ${csvForCIChart.skipCountCSV});
				},
				error : function(e) {
					alert('Fail error: ' + e);
				}
			});*/

			$('#appId').val($('.selected-app').val());
			$('#envId').val($('.selected-env').val());
			$('#suiteId').val($('.selected-suite').val());
			$('#bedId').val($('.selected-bed').val());
			$('#periodId').val($('.selected-period').val());
			$('#periodStrtDt').val($('.selected-period').attr('startDt'));
			$('#periodEndDt').val($('.selected-period').attr('endDt'));

			$("#default_form").submit();
		}

		function filterOption(clickedType, clickedCell) {
			$('#lastAppId').val($("#app li.selected-app").val());
			if (clickedType == "app") {
		    	$("#app li").removeClass("selected-app");
		    	$(clickedCell).addClass("selected-app");
		    }
		    if (clickedType == "env") {
		    	$("#env li").removeClass("selected-env");
		    	$(clickedCell).addClass("selected-env");
		    }
		    if (clickedType == "suite") {
		    	$("#suite li").removeClass("selected-suite");
		    	$(clickedCell).addClass("selected-suite");
		    }
		    if (clickedType == "bed") {
		    	$("#bed li").removeClass("selected-bed");
		    	$(clickedCell).addClass("selected-bed");
		    }
		    if (clickedType == "period") {
		    	$("#period li").removeClass("selected-period");
		    	$(clickedCell).addClass("selected-period");
		    }
		}

		$(document).ready(function() {
			if ('${fn:length(selectedParams)}' > 0) {
				$('#app li').removeClass("selected-app");
		    	$('#appId-'+'${selectedParams.appId}').addClass("selected-app");
		    	$("#selected-project").text($(".selected-app").text());

		    	$('#env li').removeClass("selected-env");
		    	$('#envId-'+'${selectedParams.envId}').addClass("selected-env");

		    	$('#suite li').removeClass("selected-suite");
		    	$('#suiteId-'+'${selectedParams.suiteId}').addClass("selected-suite");

		    	$('#bed li').removeClass("selected-bed");
		    	$('#bedId-'+'${selectedParams.bedId}').addClass("selected-bed");

		    	$('#period li').removeClass("selected-period");
		    	if (($('#periodId-'+'${selectedParams.periodId}').length)) {
		    		$('#periodId-'+'${selectedParams.periodId}').addClass("selected-period");
		    	} else {
		    		$('#period li').first().addClass("selected-period");
		    	}
		    	$("#selected-period").text($(".selected-period").text());

		    	if ($(".selected-period").attr('startDt') != undefined)
		    		dateField($(".selected-period").attr('startDt'), $(".selected-period").attr('endDt'));

		    	selectedFilter($('.selected-env').text(), $('.selected-suite').text(), $('.selected-bed').text());
			} else {
				$('#app li').first().addClass("selected-app");
				$('#env .filter-submenu').first().addClass("selected-env");
				$('#suite .filter-submenu').first().addClass("selected-suite");
				$('#bed .filter-submenu').first().addClass("selected-bed");
				$('#period li').first().addClass("selected-period");

				if ($(".selected-period").attr('startDt') != undefined)
					dateField($(".selected-period").attr('startDt'), $(".selected-period").attr('endDt'));

				selectedFilter($('.selected-env').text(), $('.selected-suite').text(), $('.selected-bed').text());
			}
        });

        function dateField(startDt, endDt) {
            var startDate = new Date(startDt.substring(0, startDt.indexOf(" ")));
            var endDate = new Date(endDt.substring(0, endDt.indexOf(" ")));

            var sd = startDate.toDateString();
            var ed = endDate.toDateString();

            $("#selected-time").text(sd.substring(sd.indexOf(" ")) + " - " + ed.substring(ed.indexOf(" ")));
		}

		function selectedFilter(env, suite, bed) {
			$("#selected-filter").text(env + " - " + suite + " - " + bed);
		}

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
	</script>
</body>
</html>
