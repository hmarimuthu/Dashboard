<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
	<title>eTAAP Dashboard - TCM</title>
	<script type="text/javascript" src='<c:url value="/js/jquery_1.8.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/highcharts.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/modernizr_custom.2.6.2.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/common.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/chart_common.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/getDataByAjax.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/myAjax.js" />'></script>
	<script type="text/javascript" >
		var systemApi = 'TCM';
		var curQuaterJsonString = "";
		var prevQuaterJsonString = ""; 
	</script>
	<c:if test="${not empty curQuaterJsonString}">
	    <script type="text/javascript" >
	    curQuaterJsonString = ${curQuaterJsonString};
		</script>
	</c:if>
	<c:if test="${not empty prevQuaterJsonString}">
	    <script type="text/javascript" >
	    prevQuaterJsonString = ${prevQuaterJsonString};
		</script>
	</c:if>
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
			<script type="text/javascript">
				
			</script>
	    	<!-- content container starts here -->
	    	<section>
	        	<!-- right content container starts here -->
	        	<article style="width:95%">
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
	</body>
	<style type="text/css">
		#ci-chart-container{
			float: left;
			width: 530px !important;
		}
		#tcm-chart-container{
			float: right;
			width: 530px !important;
		}
	</style>
	<script type="text/javascript" src='<c:url value="/js/tcm.js" />'></script>
</html>
