<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 

<html>
<head>
	<c:choose>
	    <c:when test="${paramName.paramName == 'env'}">
	        <title>eTAAP Dashboard - Manage Environment</title>
	    </c:when>
	    <c:when test="${paramName.paramName == 'suite'}">
	       <title>eTAAP Dashboard - Manage Test Suite</title>
	    </c:when>
	    <c:when test="${paramName.paramName == 'bed'}">
	        <title>eTAAP Dashboard - Manage Test Bed</title>
	    </c:when>
	    <c:when test="${paramName.paramName == 'sys'}">
	        <title>eTAAP Dashboard - Manage API</title>
	    </c:when>
	    <c:when test="${paramName.paramName == 'tc'}">
	        <title>eTAAP Dashboard - Manage Test Case</title>
	    </c:when>
	    <c:otherwise>
	        <title>eTAAP Dashboard - Manage Application</title>
	    </c:otherwise>
	</c:choose>
	<script type="text/javascript" src='<c:url value="/js/jquery_1.8.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/modernizr_custom.2.6.2.js" />'></script>
	<script type="text/javascript" src='<c:url value="/js/common.js" />'></script>
	<link type="text/css" rel="stylesheet" href='<c:url value="/css/design.css" />' />
	<link type="text/css" rel="stylesheet" href='<c:url value="/css/etaap.css" />' />
	<link type="text/css" rel="stylesheet" href='<c:url value="/fonts/open-sans/stylesheet.css" />' />
</head>
<body>
	<div class="main-content-container">
	<!-- main content container starts here -->
		<div class="dashboard-container">
			<!-- header starts here -->
			<header>
        		<%@ include file="../../jsp/header.jsp" %>
			</header>
			<!-- header ends here -->
            <div class="clear"></div>

	    	<!-- content container starts here -->
	    	<div class="content-container">
				<c:if test="${paramName.paramName != 'app' && paramName.paramName != 'tc'}">
					<div class="left-navigation">
						<ul>
							<!--li id="manage_app"><a href="manage?paramName=app&page="><span class="property-icon"></span>Property</a></li-->
							<li id="manage_env"><a href="manage?paramName=env&page=&orderBy=&orderType="><span class="environment-icon"></span>Environment</a></li>
							<li id="manage_suite"><a href="manage?paramName=suite&page=&orderBy=&orderType="><span class="testsuite-icon"></span>Test Suite</a></li>
							<li id="manage_bed"><a href="manage?paramName=bed&page=&orderBy=&orderType="><span class="testbed-icon"></span>Test Bed</a></li>
							<!--li id="manage_period"><a href="manage?paramName=period&page="><span class="period-icon"></span>Time Period</a></li-->
							<li id="manage_sys"><a href="manage?paramName=sys&page=&orderBy=&orderType="><span class="api-icon"></span>API</a></li>
							<!--li id="manage_tc"><a href="manage?paramName=tc&page=&orderBy=&orderType="><span class="api-icon"></span>Test Case</a></li-->
						</ul>
					</div>
				</c:if>

				<c:choose>
				    <c:when test="${paramName.paramName == 'env'}">
				        <%@ include file="../manage/manage_env.jsp" %>
				    </c:when>
				    <c:when test="${paramName.paramName == 'suite'}">
				       <%@ include file="../manage/manage_suite.jsp" %>
				    </c:when>
				    <c:when test="${paramName.paramName == 'bed'}">
				        <%@ include file="../manage/manage_bed.jsp" %>
				    </c:when>
				    <c:when test="${paramName.paramName == 'period'}">
				        <%@ include file="../manage/manage_period.jsp" %>
				    </c:when>
				    <c:when test="${paramName.paramName == 'sys'}">
				        <%@ include file="../manage/manage_sys.jsp" %>
				    </c:when>
				    <c:when test="${paramName.paramName == 'tc'}">
				        <%@ include file="../manage/manage_tc.jsp" %>
				    </c:when>
				    <c:otherwise>
				        <%@ include file="../manage/manage_app.jsp" %>
				    </c:otherwise>
				</c:choose>
				<div class="clear"></div>
			</div>
	    	<!-- content container ends here -->

	        <!-- footer starts here -->
			<footer>
				<%@ include file="../../jsp/footer.jsp" %>
	        </footer>
	        <!-- footer ends here -->
		</div>
	<!-- main content container ends here -->
	</div>
	<script>
		$(document).ready(function() {
			if ('${paramName.paramName}' == 'app') {
				$('li').removeClass("active");
				$('#manage_app').addClass("active");
			} else if ('${paramName.paramName}' == 'env') {
				$('li').removeClass("active");
				$('#manage_env').addClass("active");
			} else if ('${paramName.paramName}' == 'suite') {
				$('li').removeClass("active");
				$('#manage_suite').addClass("active");
			} else if ('${paramName.paramName}' == 'bed') {
				$('li').removeClass("active");
				$('#manage_bed').addClass("active");
			} else if ('${paramName.paramName}' == 'period') {
				$('li').removeClass("active");
				$('#manage_period').addClass("active");
			} else if ('${paramName.paramName}' == 'sys') {
				$('li').removeClass("active");
				$('#manage_sys').addClass("active");
			} else if ('${paramName.paramName}' == 'tc') {
				$('li').removeClass("active");
				$('#manage_tc').addClass("active");
			} else {
				$('li').removeClass("active");
				$('#manage_app').addClass("active");
			}

			$('.project-list-container').css('visibility', 'hidden');
			$('#ci').removeClass('active').addClass('deactive-link');
	    });

		function DropDown(el) {
			this.dd = el;
			this.initEvents();
		}
		DropDown.prototype = {
			initEvents : function() {
				var obj = this;
				obj.dd.on('click', function(event){
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
	</script>
</body>
</html>