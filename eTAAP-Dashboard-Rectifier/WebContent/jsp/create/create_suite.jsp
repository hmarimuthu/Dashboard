<div class="right-container">
	<%
		Object suiteDetails = request.getAttribute("suiteDetails");
		String title = "";
		String actionUrl = "";
		if(suiteDetails != null) {
			title = "Edit Test Suite Details";
			actionUrl = "update?paramName=suite";
		} else {
			title = "Create New Test Suite";
			actionUrl = "create?paramName=suite";
		}
	%>
	<h1><%= title %></h1>
	<div class="form-container section-container">
	<form:form id="create_form" action="<%= actionUrl %>" method="post" modelAttribute="testSuite">
		<div class="form-elements">
			<div class="label-name">Test Suite Name<sup class="req">*</sup>:</div>
			<div class="input-field"><form:input type="text" id="suiteName" path="suiteName" value="${suiteDetails.suiteName}" maxlength="35" specialChar ="_-"/></div>
			<div class="clear"></div>
		</div>
		<div class="form-elements">
			<div class="label-name">Active<sup class="req">*</sup>:</div>
			<div class="input-field radio-cnt">
				<div>
				<% if(suiteDetails != null) { %>
					<input id="option" type="radio" name="field" value="1" <c:if test="${suiteDetails.status == 1}">checked</c:if> />
					<label for="option"><span><span></span></span>Yes</label>
					<form:input type="hidden" id="suiteId" path="suiteId" value="${suiteDetails.suiteId}" />
				<% } else { %>
					<input id="option" type="radio" name="field" value="1" checked />
					<label for="option"><span><span></span></span>Yes</label>
				<% } %>
				</div>
				<div>
					<input id="option1" type="radio" name="field" value="0" <c:if test="${suiteDetails.status == 0}">checked</c:if> />
					<label for="option1"><span><span></span></span>No</label>
				</div>
				<form:input type="hidden" id="statusId" path="status" value="" />
			</div>
			<div class="clear"></div>
		</div>
		<div class="btn-container">
			<input type="button" id="createBtn" class="btn-blue" value="SAVE"><input type="button" id="cancelBtn" class="btn-red" value="CANCEL">
		</div>
	</form:form>
	</div>
</div>
<script>

	// function for enter event
	document.getElementById('suiteName').addEventListener('keypress', function(event) {
	    if (event.keyCode == 13) {
	        event.preventDefault();
	    	$('#createBtn').trigger('click');
	    }
	  });
	  
	$('#createBtn').click(function () {
		if ($("#suiteName").val().trim() != "" && $("input[type='radio']:checked").val()) {
			$('#statusId').val($("input[type='radio']:checked").val());

			var id = $("#suiteId").val();
			var name = $("#suiteName").val().trim();
			var flag = isNameExist('suite', id, name);

			if (flag == "true")
				alert("Suite name already exists");
			else
				$("#create_form").submit();
		}
		else {
			if ($("#suiteName").val().trim() == "") {
				alert("Enter Test Suite Name");
				return;
			}
		}
	});
	$('#cancelBtn').click(function () {
		window.history.back();
	});
</script>