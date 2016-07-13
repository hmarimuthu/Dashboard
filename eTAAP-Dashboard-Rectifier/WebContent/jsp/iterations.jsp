<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
	<title>eTAAP Dashboard - Iterationsss</title>
	<script type="text/javascript" src='<c:url value="/js/jquery_1.8.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/highcharts.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/modernizr_custom.2.6.2.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/common.js" />'></script>
	
	<script type="text/javascript" src='<c:url value="/js/chart_common.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/getDataByAjax.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/myAjax.js" />'></script>
	<script type="text/javascript" >
		var systemApi = 'iterations';
		var jsonString = "";
		var user_stories_json="";
		var abc = "${jiten}";
		
	</script>
 	<c:if test="${not empty jsonString}">
	    <script type="text/javascript" >
	    //userStoriesJsonString = ${userStoriesJsonString};
	    jsonString = ${jsonString};
	    console.log(jsonString);
		</script>
	</c:if>
	
	<c:if test="${not empty user_stories}">
	    <script type="text/javascript" >
	    //userStoriesJsonString = ${userStoriesJsonString};
	    user_stories_json = ${user_stories};
	    console.log("User_stories_json"+user_stories_json);
		</script>
	</c:if>
	
	<link type="text/css" rel="stylesheet" href='<c:url value="/css/main.css" />' />
	<link type="text/css" rel="stylesheet" href='<c:url value="/css/design.css" />' />
	<link type="text/css" rel="stylesheet" href='<c:url value="/fonts/open-sans/stylesheet.css" />' />
	<link type="text/css" rel="stylesheet" href='<c:url value="/css/etaap.css" />' />

</head>
<body onload="">
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
	        	<!-- right content container starts here -->
	        	<article style="width:95%">
				<h3 class="chart-heading">Iterations</h3>
				
				<!-- time filter starts here -->
				<div class="time-filter-container">
				
					<div id="selected-time" class="peroid-container">
						<p></p>
					</div>
					<div id="dd" class="iteration-container">
					<%System.out.println("NMNMNMNMNM Sprint Id in Iterations.jsp "+request.getAttribute("selectedSprint")); %>
					<h3 id="selected-period">${selectedSprint}</h3>
						<ul class="iteration-options-container">
							<li class="iteration-heading">
								<ul id="period" class="iteration-submenu">
									<c:forEach var="sprint" items="${sprintList}">
									
										<li id="sprintId-${sprint.sprintId}" value="${sprint.sprintId}" onclick="iterationChartAjaxCall(${sprint.sprintId});">${sprint.sprintName}</li>
									</c:forEach>
								</ul>
							</li>
						</ul>
					</div>
					
					
				<!-- time filter ends here -->    	
				</div>
				<div class="clear"></div>
				
				<!-- charts-container starts here -->
			<div class="clear"></div>	
				<div id="datewiseCommitedCompletedUserStories"  class="velocity-chart-container" style="display: block;"></div>
				<div>
					<div id="burndownPerSprintChartContainer"  class="burn-down-chart" style="display: block;"></div>
					<div id="watertank-chart-container"  class="watertank-chart-container" style="display: block;">
				  	<section>
			        	<!-- right content container starts here -->
			        	<article style="width:95%">
							<p class="graph-name"><span class="graph-text-left">User Stories</span><span class="status-icon-yellow"></span></p>
							<div class="iframe-container">
							  <div class="container-circle">
							    <div class="inner-circle">
							      <div class="us-details">
							      <h3 class="user-story-number"><span id="user_stories"></span></h3>
							      <div class="total-us-count">
							          <p class="us-count-text">OUT OF</p>
							          <div class="us-horz-separator"></div>
							          <h4 class="us-count" id="total_stories"></h4>
							      </div>
							      <div class="clear"></div>
							    </div>
							    <div class="clear"></div>
							      <div class="water-level-circle" style="background: -webkit-linear-gradient(top,white 100%,purple 10%);background: -moz-linear-gradient(top,white 100%,purple 10%);background: -o-linear-gradient(top,white 100%,purple 10%);background: -ms-linear-gradient(top,white 100%,purple 10%);">
							      </div>
							        <p class="user-story-text">User Stories Completed</p>
							    </div>
							  </div>
							</div>
			        	</article>
			        	<!-- right content container ends here -->
			            <div class="clear"></div>
			    	</section>				
				</div>
				<div class="clear"></div>					
				</div>
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
		</div>
	<!-- main content container ends here -->	
	</body>
 	<style type="text/css">
	
	</style>
<script type="text/javascript" src='<c:url value="/js/iterationsChart.js" />'></script>
</html>
