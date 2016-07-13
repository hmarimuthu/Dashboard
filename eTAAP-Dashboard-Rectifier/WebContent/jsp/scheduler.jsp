<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
	<meta charset="utf-8">
	<title>Manage Scheduler</title>
	
	<link rel="stylesheet" href="css/design.css" type="text/css" />
	<link rel="stylesheet" href="css/etaap.css" type="text/css" />
	<link rel="stylesheet" href="css/toggle.css" type="text/css" />
	<script src="js/jquery_1.8.js" type="text/javascript"></script>
	<script src="js/toggles-min.js" type="text/javascript"></script>
	<script src="js/scheduler.js" type="text/javascript"></script>
	<script type="text/javascript" src='<c:url value="/js/common.js" />'></script>
	<link type="text/css" rel="stylesheet" href='<c:url value="/fonts/open-sans/stylesheet.css" />' />
	<style>
	.dropdown-menu{margin: 0;}
	</style>
	<script type="text/javascript">
	function pageAutoRefresh(isAutoRefresh) {
		if(isAutoRefresh)window.location.reload();
	}

	var schedulerJobsJsonVar = ${SchedulerJobsJsonString};
	var intervalList = ${interval_List};
	var apiList = ${api_List};
	var appList = ${app_List};
	var jobsList = "";
	</script>
	<c:if test="${not empty jobs_List}">
	    <script type="text/javascript">
			jobsList = ${jobs_List};
		</script>
	</c:if>
	<script type="text/javascript">
	$(document).ready(function() {
		//$('.toggle').toggles({text:{on:'Enable',off:'Disable'}});

		loadScheduleJobsContainer();
		setInterval("pageAutoRefresh(true);",20000);
	    function my_function() {
	        window.location = location.href;
	    }
	});
	/* $('pk_jobId') */
	function enableDisable(params){
	  //$('#'+params).toggles({text:{on:'Enable',off:'Disable'}});
	
	  var child = $('#'+params).children();
	  if (child.find('.toggle-on').hasClass('active')) {
	   child.find('.toggle-on').removeClass('active');
	   child.find('.toggle-off').addClass('active');
	   child.find('.toggle-inner').css('margin-left', '-30px');
	  }
	  else if (child.find('.toggle-off').hasClass('active')) {
	   child.find('.toggle-off').removeClass('active');
	   child.find('.toggle-on').addClass('active');
	   child.find('.toggle-inner').css('margin-left', '0px');
	  }
	 }
	</script>
</head>
<body>
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
	        	
				<!-- left content container ends here -->

	        	<!-- right content container starts here -->
	        	<article  style="width:1092px">
	        	<div class="content-container">
			<div class="right-container" style="width:100%">
				<h1>Manage Scheduler</h1>

				<div class="section-container">
					<div class="pagination-section margin-bottom5">
						<div class="button-section"><a class="tooltips add-icon" href="#" onclick="addMoreSchedulers();"><span class="add-clr">Add Record</span></a>&nbsp;&nbsp;<!-- <a class="tooltips remove-icon" href="#"><span>Delete the Record</span></a> --></div>
						<!-- <div class="pagination">
							<a href="#" class="prev"><span  class="previous-btn"></span> PREV</a>
							<span class="result-text">Showing 21 to 30</span>
							<a href="#" class="next">NEXT <span class="next-btn"></span></a>
						</div> -->
						<div class="clear"></div>
					</div>
					<div id="jobsContainer">
					
						<%-- <table class="table sch-table" cellpadding="0" cellspacing="0" id="jobsContainer_table">
						<thead>
						<tr class="hema">
							<th width="12%">Run Now</th>
							<th width="25%">Job Name</th>
							<th width="10%">Interval Type</th>
							<th width="10%">Schedule status</th>
							<th width="25%">Select API</th>
						</tr>
						</thead>

						<tbody>
						
						<tr>
							<td>
								<input type="button" class="sch-disable" value="Run Now"/>
							</td>
							<td>
								<div class="dropdown-menu" id="env_dd" style="width: 250px;">
									<div id="selected_envId">Select</div>
									<ul class="dropdown" id="env_list" style="z-index:100;">
										<li id="envId-${env.envId}" value="${env.envId}">${env.envName}</li>
									</ul>
								</div>
								<span class="last-run">Last Run On : Thur , 26th sep 2 pm IST</span>
							</td>
							<td>
								<div class="dropdown-menu" id="env_dd" style="width: 110px;">
									<div id="selected_envId">Select</div>
									<ul class="dropdown" id="env_list" style="z-index:100;">
										<li id="envId-${env.envId}" value="${env.envId}">${env.envName}</li>
									</ul>
								</div>
							</td>
							<td>
								<div class="toggle"></div>
							</td>
							<td>
								<div class="dropdown-menu" id="env_dd" style="width: 250px;">
									<div id="selected_envId">Select</div>
									<ul class="dropdown" id="env_list" style="z-index:100;">
										<li id="envId-${env.envId}" value="${env.envId}">${env.envName}</li>
									</ul>
								</div>
							</td>
						</tr>
						</tbody>
					</table> --%>
					
					</div>
					

					<!-- <div class="pagination-section margin-top5">
						<div class="button-section"></div>
						<div class="pagination">
							<a href="#" class="prev"><span  class="previous-btn"></span> PREV</a>
							<span class="result-text">Showing 21 to 30</span>
							<a href="#" class="next">NEXT <span class="next-btn"></span></a>
						</div>
						<div class="clear"></div>
					</div> -->
					<div class="btn-container" >
   <input type="button" id="createBtn" class="btn-blue" value="SAVE" onclick="javascript:if(validateScheduledJobs())saveScheduledJobs();"><input onclick="javascript:window.location.href='dashboard'" type="button" id="cancelBtn" class="btn-red" value="CANCEL">
  </div>

				</div>
			</div>
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
	<!-- main content container ends here -->
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.project-list-container').css('visibility', 'hidden');
			$('#ci').removeClass('active').addClass('deactive-link');
			dropdown();
			dropdownAutoSize();

			$('[id^=job_interval_]').each(function() {
				var id = $(this).attr('id');
				$(this).parent().find('ul li').each(function() {
					$(this).attr('id', id.substring(13, id.length));
				});
			});
			$('[id^=api_name_]').each(function() {
				var id = $(this).attr('id');
				$(this).parent().find('ul li').each(function() {
					$(this).attr('id', id.substring(9, id.length));
				});
			});
		});

		function dropdown() {
			function DropDown(el) {
				this.dd = el;
				this.initEvents();
			}

			/*DropDown.prototype = {
				initEvents : function() {
					var obj = this;
					obj.dd.on('click', function(event) {
					$(this).addClass('active');
						event.stopPropagation();
					});
				}
			};*/

			DropDown.prototype = {
				initEvents : function() {
					var obj = this;
					obj.dd.off('click').on('click', function(event) {
						var classRef = $(this).attr('class');
						$('.dropdown-menu').each(function() {
							if($(this).attr('class') != classRef) {
								$(this).removeClass('active');
							}
							$('.settings').removeClass('active');
						});
						$(this).toggleClass('active');
						event.stopPropagation();
					});
				}
			};

			$(function() {
				var dd = new DropDown($('.dropdown-menu'));
				$(document).click(function() {
					$('.dropdown-menu').removeClass('active');
				});
			});
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
	
		//$('ci').css('deactive-link');
	</script>