<div class="right-container">
	<%
		Object periodDetails = request.getAttribute("periodDetails");
		String title = "";
		String actionUrl = "";
		if(periodDetails != null) {
			title = "Edit Time Period Details";
			actionUrl = "update?paramName=period";
		} else {
			title = "Create New Time Period";
			actionUrl = "create?paramName=period";
		}
	%>
	<h1><%= title %></h1>
	<div class="form-container section-container">
	<form:form id="create_form" action="<%= actionUrl %>" method="post" modelAttribute="timePeriod">
		<div class="form-elements">
			<div class="label-name">Property Name:</div>
			<div class="input-field">
				<div class="dropdown-menu" id="app_dd">
					<div id="selected-appId">Select</div>
					<ul class="dropdown" id="app_list" style="z-index:100;">
						<c:forEach var="app" items="${app_list}">
							<li id="appId-${app.appId}" value="${app.appId}">${app.appName}</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		<div class="form-elements">
			<div class="label-name">Month:</div>
			<div class="input-field">
				<div class="dropdown-menu" id="month_dd">
					<div id="selected-monthId">Select</div>
					<ul class="dropdown" id="month_list">
						<li value="1">Jan</li>
						<li value="2">Feb</li>
						<li value="3">Mar</li>
						<li value="4">Apr</li>
						<li value="5">May</li>
						<li value="6">Jun</li>
						<li value="7">Jul</li>
						<li value="8">Aug</li>
						<li value="9">Sep</li>
						<li value="10">Oct</li>
						<li value="11">Nov</li>
						<li value="12">Dec</li>
					</ul>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		<% if(periodDetails != null) { %>
				<form:input type="hidden" path="periodId" value="${periodDetails.periodId}" />
		<% } %>
		<form:input type="hidden" id="app_id" path="appId" value="${periodDetails.appId}" />
		<form:input type="hidden" id="month_id" path="monthId" value="${periodDetails.monthId}" />
		<form:input type="hidden" id="month_name" path="monthName" value="${periodDetails.monthName}" />
		<div class="btn-container">
			<input type="button" id="createBtn" class="btn-blue" value="SAVE"><input type="button" id="cancelBtn" class="btn-red" value="CANCEL">
		</div>
	</form:form>
	</div>
</div>
<script type="text/javascript">
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
	}

	$(function() {
		var dd = new DropDown($('#app_dd, #month_dd'));
		$(document).click(function() {
			$('#app_dd, #month_dd').removeClass('active');
		});
	});

	$(document).click(function() {
		$('#app_dd, #month_dd').removeClass('active');
	});

	$('#app_dd').click(function() {
		$('#month_dd').removeClass('active');
	});

	$('#month_dd').click(function() {
		$('#app_dd').removeClass('active');
	});

	$('#app_list li').click(function() {
		$('#selected-appId').text($(this).text()).css('color', '#575757');
		$('#app_id').val($(this).val());
	});

	$("#month_list li").click(function() {
		$('#selected-monthId').text($(this).text()).css('color', '#575757');
		$('#month_id').val($(this).val());
		$('#month_name').val($(this).text());
	});

	
	$('#createBtn').click(function () {
		if ($('#app_id').val() != 0 && $('#month_id').val() != 0) {
			$("#create_form").submit();
		}
		else {
			alert('All fields are required');
		}
	});

	$('#cancelBtn').click(function () {
		window.history.back();
	});

	$(document).ready(function() {
		if ('${periodDetails}') {
			$('#selected-appId').text("${appName}").css('color', '#575757');
			$('#selected-monthId').text('${periodDetails.monthName}').css('color', '#575757');
		}
    });
</script>