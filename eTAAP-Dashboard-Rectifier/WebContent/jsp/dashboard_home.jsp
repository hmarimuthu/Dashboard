<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
	<head>
		<title>eTAAP Dashboard</title>
		<script type="text/javascript" src='<c:url value="/js/jquery_1.8.js" />'></script>
		<script type="text/javascript" src='<c:url value="/js/highcharts.js" />'></script>
		<script type="text/javascript" src='<c:url value="/js/grouped-categories.js"/>'></script>
		<script type="text/javascript" src='<c:url value="/js/modernizr_custom.2.6.2.js" />'></script>
		<script type="text/javascript" src='<c:url value="/js/dashboard_home.js" />'></script>
		<script type="text/javascript" src='<c:url value="/js/common.js" />'></script>
		<script type="text/javascript" src='<c:url value="/js/drilldown.js" />'></script>
		<script type="text/javascript" src='<c:url value="/js/chart_common.js" />'></script>
		
		
		<script type="text/javascript">
		function changeDashboardApp(pathVariable,thisRef)
		{	
			systemApi = thisRef.getAttribute('data-priority');
			if (systemApi == "none") {
				alert('No record available for current quarter.');
				return false; 
			}
			id= thisRef.value
			// needs to use jquery one 
			window.location.href = contextPath + "/" + pathVariable + "/?" + "id=" + id + "&api="+systemApi;
		}
		</script>
		<script type="text/javascript" >
			var systemApi = 'jenkins';
			var jsonObj = '${jsonString}';
		</script>
		<link type="text/css" rel="stylesheet" href='<c:url value="/css/design.css" />' />
		<link type="text/css" rel="stylesheet" href='<c:url value="/fonts/open-sans/stylesheet.css" />' />
		<link type="text/css" rel="stylesheet" href='<c:url value="/css/etaap.css" />' />
	</head>
	<body>
		<div class="main-content-container">
		<!-- main content container starts here -->
			<div class="dashboard-container">
			<form id="default_form" action="filter" method="post">
				<!-- header starts here -->
				<header>
					<%@ include file="./dashboard_header.jsp" %>
				</header>
				<!-- header ends here -->
	            <div class="clear"></div>

		    	<!-- content container starts here -->
		    	<section class="home-page-main-box">
		    		<div class="dashboard-bg home-page-box">
						<div class="db-box db-box-1" style="position: relative;">
							<div id="addPriority" class="count-cnt red-clr">
		      					<span id="addPriorityText"></span>
		      					Defects
		     				</div>
							<div id="priority_chart_container">
                            <div class="clear"></div></div>
						</div>
						<div class="db-box margin0" style="position: relative;">
							<div id="addSeverity" class="count-cnt red-clr">
		      					<span id="addSeverityText"></span>
		      					Defects
		     				</div>
							<div id="severity_chart_container">
                            <div class="clear"></div></div>
						</div>
						<div id="jenkins_chart_container" class="db-box"></div>
                        <div class="clear"></div>
						<div id="tcm_dashboard_chart" class="db-box db-box-1"></div>
						<div id="defects_statistics_container" class="db-box margin0"></div>
						<div id="defects_life_container" class="db-box"></div>
						<div id="committed_completed_userstories_chart_container" class="test-case-box"></div>
						<div class="clear"></div>
					</div>
						<div class="clear"></div>
		    	</section>
						<div class="clear"></div>
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
	</body>
	<script>
		//document.getElementById('ci').className='deactive-link';
	</script>
</html>