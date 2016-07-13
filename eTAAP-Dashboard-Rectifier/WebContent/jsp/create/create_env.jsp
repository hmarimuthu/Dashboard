<div class="right-container">
	<%
		Object envDetails = request.getAttribute("envDetails");
	   
		String title = "";
		String actionUrl = "";
		if(envDetails != null) {
			title = "Edit Environment Details";
			actionUrl = "update?paramName=env";
		} else {
			title = "Create New Environment";
			actionUrl = "create?paramName=env";
		}
	%>
	
	<h1><%= title %></h1>
	<div class="form-container section-container">
	<form:form id="create_form" action="<%= actionUrl %>" method="post" modelAttribute="environment">
		<div class="form-elements">
			<div class="label-name">Environment Name<sup class="req">*</sup>:</div>
			<div class="input-field"><form:input type="text" id="envName" path="envName" value="${envDetails.envName}"  maxlength="35" specialChar ="_-" /></div>
			<div class="clear"></div>
		</div>
		<div class="form-elements">
			<div class="label-name">Active<sup class="req">*</sup>:</div>
			<div class="input-field radio-cnt">
				<div>
				<% if(envDetails != null) { %>
					<input id="option" type="radio" name="field" value="1" <c:if test="${envDetails.status == 1}">checked</c:if> />
					<label for="option"><span><span></span></span>Yes</label>
					<form:input type="hidden" id="envId" path="envId" value="${envDetails.envId}" />
				<% } else { %>
					<input id="option" type="radio" name="field" value="1" checked />
					<label for="option"><span><span></span></span>Yes</label>
				<% } %>
				</div>
				<div>
					<input id="option1" type="radio" name="field" value="0" <c:if test="${envDetails.status == 0}">checked</c:if> />
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
	document.getElementById('envName').addEventListener('keypress', function(event) {
	    if (event.keyCode == 13) {
            event.preventDefault();
	    	$('#createBtn').trigger('click');
        }
	  });
		
	$('#createBtn').click(function () {
		if ($("#envName").val().trim() != "" && $("input[type='radio']:checked").val()) {
			$('#statusId').val($("input[type='radio']:checked").val());

			var id = $("#envId").val();
			var name = $("#envName").val().trim();
			var flag = isNameExist('env', id, name);

			if (flag == "true")
				alert("Environment name already exists");
			else
				$("#create_form").submit();
			
			
		}
		else {
			
			if ($("#envName").val().trim() == "") {
				alert("Enter Environment Name");
				return false;
			}
		}
	});
	$('#cancelBtn').click(function () {
		window.history.back();
	});
</script>