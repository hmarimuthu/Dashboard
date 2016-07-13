<div class="right-container">
	<%
		Object bedDetails = request.getAttribute("bedDetails");
		String title = "";
		String actionUrl = "";
		if(bedDetails != null) {
			title = "Edit Test Bed Details";
			actionUrl = "update?paramName=bed";
		} else {
			title = "Create New Test Bed";
			actionUrl = "create?paramName=bed";
		}
	%>
	<h1><%= title %></h1>
	<div class="form-container section-container">
	<form:form id="create_form" action="<%= actionUrl %>" method="post" modelAttribute="testBed">
		<div class="form-elements">
			<div class="label-name">Test Bed Name<sup class="req">*</sup>:</div>
			<div class="input-field"><form:input type="text" id="bedName" path="bedName" value="${bedDetails.bedName}"  maxlength="35" specialChar ="_-" /></div>
			<div class="clear"></div>
		</div>
		<div class="form-elements">
			<div class="label-name">Active<sup class="req">*</sup>:</div>
			<div class="input-field radio-cnt">
				<div>
				<% if(bedDetails != null) { %>
					<input id="option" type="radio" name="field" value="1" <c:if test="${bedDetails.status == 1}">checked</c:if> />
					<label for="option"><span><span></span></span>Yes</label>
					<form:input type="hidden" id="bedId" path="bedId" value="${bedDetails.bedId}" />
				<% } else { %>
					<input id="option" type="radio" name="field" value="1" checked />
					<label for="option"><span><span></span></span>Yes</label>
				<% } %>
				</div>
				<div>
					<input id="option1" type="radio" name="field" value="0" <c:if test="${bedDetails.status == 0}">checked</c:if> />
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
	document.getElementById('bedName').addEventListener('keypress', function(event) {
	    if (event.keyCode == 13) {
	        event.preventDefault();
	    	$('#createBtn').trigger('click');
	    }
	  });
  
	$('#createBtn').click(function () {
		if ($("#bedName").val().trim() != "" && $("input[type='radio']:checked").val()) {
			$('#statusId').val($("input[type='radio']:checked").val());

			var id = $("#bedId").val();
			var name = $("#bedName").val().trim();
			var flag = isNameExist('bed', id, name);

			if (flag == "true")
				alert("Test Bed name already exists");
			else
				$("#create_form").submit();
		}
		else {
			if ($("#bedName").val().trim() == "") {
				alert("Enter Test Bed Name");
				return;
			}
		}
	});
	$('#cancelBtn').click(function () {
		window.history.back();
	});
</script>