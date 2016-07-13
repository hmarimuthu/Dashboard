<%@page import="java.util.ArrayList"%>
<%@page import="com.etaap.domain.SystemAPI"%>

<div class="right-container">
	<%
		Object sysDetails = request.getAttribute("sysDetails");
		String title = "";
		String actionUrl = "";
		String apiName = "";
		if(sysDetails != null) {
			title = "Edit API Details";
			actionUrl = "update?paramName=sys";
			apiName = ((SystemAPI) sysDetails).getApiName().trim();
		} else {
			title = "Create New API";
			actionUrl = "create?paramName=sys";
		}
	%>
	<h1><%= title %></h1>
	<div class="form-container section-container">
	<form:form id="create_form" action="<%= actionUrl %>" method="post" modelAttribute="systemAPI">
		<div class="form-elements">
			<div class="label-name">API Name<sup class="req">*</sup>:</div>
			<div class="input-field"><form:input type="text" id="sysName" path="sysName" value="${sysDetails.sysName}" maxlength="35" specialChar ="_-"/></div>
			<div class="clear"></div>
		</div>
		<div class="form-elements">
			<div class="label-name">API<sup class="req">*</sup>:</div>
			<div class="input-field">
				<div class="dropdown-menu" id="api_dd" >
					<div id="selected_apiId">Select</div>
					<ul class="dropdown" id="api_list">
						<li value="1">Jenkins</li>
						<li value="2">Jira</li>
					</ul>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		<div class="form-elements">
			<div class="label-name">Url<sup class="req">*</sup>:</div>
			<div class="input-field"><form:input type="text" id="url" path="url" value="${sysDetails.url}" /></div>
			<div class="clear"></div>
		</div>
		<div id="user_auth" style="display: none;">
			<div class="form-elements">
				<div class="label-name">User ID<sup class="req" id="userIdReq" style="display: none;">*</sup>:</div>
				<div class="input-field"><form:input type="text" id="userId" path="userId" value="${sysDetails.userId}" /></div>
				<div class="clear"></div>
			</div>
			<div class="form-elements">
				<div class="label-name">Password<sup class="req" id="pwasswordReq" style="display: none;">*</sup>:</div>
				<div class="input-field"><form:input type="password" id="password" path="password" value="${sysDetails.password}" /></div>
				<div class="clear"></div>
			</div>
		</div>
		<div class="form-elements">
			<div class="label-name">API for<sup class="req">*</sup>:</div>
			<div id="dept" class="checkbox-group">
				<c:forEach var="dept" items="${dept_list}">
					<div class="input-field checkbox-cnt">
						<input type="checkbox" id="checkbox${dept.key}" value="${dept.value}" />
						<label for="checkbox${dept.key}">${dept.value}</label>
					</div>
				</c:forEach>
			</div>
			<div class="clear"></div>
		</div>
		<div class="form-elements">
			<div class="label-name">Active<sup class="req">*</sup>:</div>
			<div class="input-field radio-cnt">
				<div>
				<% if(sysDetails != null) { %>
					<input id="option" type="radio" name="field" value="1" <c:if test="${sysDetails.status == 1}">checked</c:if> />
					<label for="option"><span><span></span></span>Yes</label>
					<form:input type="hidden" id="sysId" path="sysId" value="${sysDetails.sysId}" />
				<% } else { %>
					<input id="option" type="radio" name="field" value="1" checked />
					<label for="option"><span><span></span></span>Yes</label>
				<% } %>
				</div>
				<div>
					<input id="option1" type="radio" name="field" value="0" <c:if test="${sysDetails.status == 0}">checked</c:if> />
					<label for="option1"><span><span></span></span>No</label>
				</div>

				<form:input type="hidden" id="statusId" path="status" value="" />
				<form:input type="hidden" id="api_id" path="apiId" value="${sysDetails.apiId}" />
				<form:input type="hidden" id="api_name" path="apiName" value="${sysDetails.apiName}" />
				<form:input type="hidden" id="department" path="department" value="" />
			</div>
			<div class="clear"></div>
		</div>
		<div id="custom_field" class="form-elements" style="display: none; margin-bottom:0;">
			<div class="label-name">Custom Fields<sup class="req">*</sup>:</div>
			<div class="input-field">
				<!--  new content added here -->
				<div class="system-dtls-tbl" style="float: left;">
					<div class="form-elements creat-prop-lay">
						<div class="section-div tabs">
							<div class="tab-content-new" style="height: auto">
								<div class="input-field form-elements content-new" style="width: auto; padding: 0 14px 5px;">
									<table id="system-dtls-tbl">
									<%
										ArrayList customKeyList = null;
										ArrayList customValueList = null;

										if (sysDetails != null) {
											customKeyList = (ArrayList)((SystemAPI) sysDetails).getCustomKeyList();
											customValueList = (ArrayList)((SystemAPI) sysDetails).getCustomValueList();
										}

										if (sysDetails != null && customKeyList != null  && (apiName.toLowerCase().indexOf("ji") >= 0)) {
									%>
										<tr>
											<th>Name</th>
											<th>Equivalent To</th>
											<th><span class="addRows" onclick="addRow();" title="Add New Row"></span></th>
										</tr>
										<%
											for (int a=0; a<customKeyList.size(); a++) {
												int custValIndex = 10000-a;
										%>
										<tr class="system-dtls">
											<td>
												<input type="text" id="customKey<%=a %>" name="customKey" style="width: 210px;" value="<%=customKeyList.get(a) %>" />
											</td>
											<td>
												<div class="dropdown-menu" id="custom_dd<%=a %>" style="width: 150px;">
													<div id="selected_customVal<%=a %>" value="">Select</div>
													<ul class="dropdown" id="custom_value_list<%=a %>" style="z-index:<%=custValIndex %>;">
														<li>Environment</li>
														<li>Severity</li>
													</ul>
												</div>
												<div class="clear"></div>
											</td>
											<td><span class="removeRow" title="Delete Row"></span></td>
										</tr>
									<% 		}
										} else {
									%>
										<tr>
											<th>Name</th>
											<th>Equivalent To</th>
											<th><span class="addRows" onclick="addRow();" title="Add New Row"></span></th>
										</tr>
										<tr class="system-dtls">
											<td>
												<input type="text" id="customKey" name="customKey" style="width: 210px;" value="" />
											</td>
											<td>
												<div class="dropdown-menu" id="custom_dd" style="width: 150px;">
													<div id="selected_customVal" value="">Select</div>
													<ul class="dropdown" id="custom_value_list" style="z-index:10000;">
														<li>Environment</li>
														<li>Severity</li>
													</ul>
												</div>
												<div class="clear"></div>
											</td>
											<td><span class="removeRow" title="Delete Row"></span></td>
										</tr>
									<% 	} %>
									</table>
								</div>
	                            <div class="clear"></div>
							</div>
		                	<div class="clear"></div>
						</div>
						<div class="clear"></div>
					</div>
				</div>
				<!--  new content ends here  -->
			</div>
			<div class="clear"></div>
		</div>
		
		<div id="priority" class="form-elements" style="display: none; margin-bottom:0;">
			<div class="label-name">Priority<sup class="req">*</sup>:</div>
			<div class="input-field">
				<!--  new content added here -->
				<div class="system-dtls-tbl" style="float: left;">
					<div class="form-elements creat-prop-lay">
						<div class="section-div tabs">
							<div class="tab-content-new" style="height: auto">
								<div class="input-field form-elements content-new" style="width: auto; padding: 0 14px 5px;">
									<table id="system-dtls-tbl">
									<%
										
										ArrayList priorityNameList = null;

										if (sysDetails != null) {
										
											priorityNameList = (ArrayList)((SystemAPI) sysDetails).getPriorityNameList();
										}

										if (sysDetails != null && priorityNameList != null  && (apiName.toLowerCase().indexOf("ji") >= 0)) {
									%>
										<tr>
											<th>Name</th>
											<th>Equivalent To</th>
										
										</tr>
										
										<%
											//for (int a=0; a<priorityNameList.size(); a++) {
											//	int custValIndex = 10000-a;
										%>
										<tr class="system-dtls">
											<td>
												<input type="text" id="priorityOneId" name="priorityOneName" style="width: 210px;" value="<%=priorityNameList.get(0) %>" />
											</td>
											<td>
												<div><input name="priorityOne" VALUE="P1" DISABLED></div>
												<div class="clear"></div>
											</td>
										</tr>
										
										<tr class="system-dtls">
											<td>
												<input type="text" id="priorityTwoId" name="priorityTwoName" style="width: 210px;" value="<%=priorityNameList.get(1) %>" />
											</td>
											<td>
												<div><input name="priorityTwo" VALUE="P2" DISABLED></div>
												<div class="clear"></div>
											</td>
										</tr>
									<% 		//}
										} else {
									%>
										<tr>
											<th>Name</th>
											<th>Equivalent To</th>
										<!-- 	<th><span class="addRows" onclick="addRow();" title="Add New Row"></span></th> -->
										</tr>
										<tr class="system-dtls">
											<td>
												<input type="text" id="priorityOneId" name="priorityOneName" style="width: 210px;" value="" />
											</td>
											<td>
												<div><input name="priorityOne" VALUE="P1" DISABLED></div>
												<div class="clear"></div>
											</td>
										</tr>
										
										<tr class="system-dtls">
											<td>
												<input type="text" id="priorityTwoId" name="priorityTwoName" style="width: 210px;" value="" />
											</td>
											<td>
											<div><input name="priorityTwo" VALUE="P2" DISABLED></div>	
											<div class="clear"></div>
											</td>
										</tr>
									<% 	} %>
									</table>
								</div>
	                            <div class="clear"></div>
							</div>
		                	<div class="clear"></div>
						</div>
						<div class="clear"></div>
					</div>
				</div>
				<!--  new content ends here  -->
			</div>
			<div class="clear"></div>
		</div>
		
		<div id="status" class="form-elements" style="display: none; margin-bottom:0;">
			<div class="label-name">Status<sup class="req">*</sup>:</div>
			<div class="input-field">
				<!--  new content added here -->
				<div class="system-dtls-tbl" style="float: left;">
					<div class="form-elements creat-prop-lay">
						<div class="section-div tabs">
							<div class="tab-content-new" style="height: auto">
								<div class="input-field form-elements content-new" style="width: auto; padding: 0 14px 5px;">
									<table id="system-dtls-tbl">
									<%
										ArrayList statusNameList = null;

										if (sysDetails != null) {
											statusNameList = (ArrayList)((SystemAPI) sysDetails).getStatusNameList();
										}

										if (sysDetails != null && statusNameList != null  && (apiName.toLowerCase().indexOf("ji") >= 0)) {
									%>
										<tr>
											<th>Name</th>
											<th>Equivalent To</th>
										</tr>
										<%
											//for (int a=0; a<statusNameList.size(); a++) {
											//	int custValIndex = 10000-a;
										%>
										<tr class="system-dtls">
											<td>
												<input type="text" id="statusNewId" name="statusNewName" style="width: 210px;" value="<%=statusNameList.get(0) %>" />
											</td>
											<td>
												<div><input name="statusNew" VALUE="New" DISABLED></div>
												<div class="clear"></div>
											</td>
										</tr>
										<tr class="system-dtls">
											<td>
										 		<input type="text" id="statusClosedId" name="statusClosedName" style="width: 210px;" value="<%=statusNameList.get(1) %>" />
											</td>
											<td>
												<div><input name="statusClosed" VALUE="Closed" DISABLED></div>
												<div class="clear"></div>
											</td>
										</tr>
										
										<tr class="system-dtls">
											<td>
												<input type="text" id="statusInProgressId" name="statusInProgressName" style="width: 210px;" value="<%=statusNameList.get(2) %>" />
											</td>
											<td>
												<div><input name="statusInProgress" VALUE="In Progress" DISABLED></div>
												<div class="clear"></div>
											</td>
										</tr>
										
										<tr class="system-dtls">
											<td>
												<input type="text" id="statusVerifyId" name="statusVerifyName" style="width: 210px;" value="<%=statusNameList.get(3) %>" />
											</td>
											<td>
												<div><input name="statusVerify" VALUE="Verify" DISABLED></div>
												<div class="clear"></div>
											</td>
										</tr>
									<% 	//	}
										} else {
									%>
										<tr>
											<th>Name</th>
											<th>Equivalent To</th>
										</tr>
										<tr class="system-dtls">
											<td>
												<input type="text" id="statusNewId" name="statusNewName" style="width: 210px;" value="" />
											</td>
											<td>
												<div><input name="statusNew" VALUE="New" DISABLED></div>
												<div class="clear"></div>
											</td>
									
										</tr>
										
										<tr class="system-dtls">
											<td>
											 	<input type="text" id="statusClosedId" name="statusClosedName" style="width: 210px;" value="" />
												</td>
											<td>
												<div><input name="statusClosed" VALUE="Closed" DISABLED></div>
												<div class="clear"></div>
											</td>
										</tr>
										
											<tr class="system-dtls">
											<td>
												<input type="text" id="statusInProgressId" name="statusInProgressName" style="width: 210px;" value="" />
											</td>
											<td>
												<div><input name="statusInProgress" VALUE="In Progress" DISABLED></div>
												<div class="clear"></div>
											</td>
										</tr>
										
											<tr class="system-dtls">
											<td>
												<div><input type="text" id="statusVerifyId" name="statusVerifyName" style="width: 210px;" value="" /></div>
											</td>
											<td>
												<input name="statusVerify" VALUE="Verify" DISABLED>
												<div class="clear"></div>
											</td>
										</tr>
									<% 	} %>
									</table>
								</div>
	                            <div class="clear"></div>
							</div>
		                	<div class="clear"></div>
						</div>
						<div class="clear"></div>
					</div>
				</div>
				<!--  new content ends here  -->
			</div>
			<div class="clear"></div>
		</div>

		<form:input type="hidden" id="custom_value" path="customValue" value="" />
		
		<form:input type="hidden" id="priorityOne_value" path="priorityOneValue" value="" />
		<form:input type="hidden" id="priorityTwo_value" path="priorityTwoValue" value="" />
		
		<form:input type="hidden" id="statusNew_value" path="statusNewValue" value="" />
		<form:input type="hidden" id="statusClosed_value" path="statusClosedValue" value="" />
		<form:input type="hidden" id="statusInProgress_value" path="statusInProgressValue" value="" />
		<form:input type="hidden" id="statusVerify_value" path="statusVerifyValue" value="" /> 

		<div class="btn-container">
			<input type="button" id="createBtn" class="btn-blue" value="SAVE"><input type="button" id="cancelBtn" class="btn-red" value="CANCEL">
		</div>
	</form:form>
	</div>
</div>
<script type="text/javascript">


	$('#dept input').click(function(event) {
		$('#dept input').each(function() {
			if($(this).val().toLowerCase() == 'qa' && $(this).is(':checked') && $('#selected_apiId').text().toLowerCase().indexOf("ji")>=0) {
				$('#custom_field').show();
				$('#priority').show();
				$('#status').show();
				return false;
			}
			else {
				$('#custom_field').hide();
				$('#priority').hide();
				$('#status').hide();
			}
		});
	});

	$('[id^=customKey]').on('click', function() {
		$('[id^=customKey]').css('border', '');
	});

	$('[id^=custom_value_list]').on('click', 'li', function(event) {
		$(this).parent().siblings("[id^=selected_customVal]").text($(this).text()).css('color', '#575757');
		$(this).parent().siblings("[id^=selected_customVal]").attr("value", $(this).text());
		$(this).parent().parent().removeClass("active");
		$('.dropdown-menu').css('border','');
		event.stopPropagation();
	});

	// function for enter event
	document.getElementById('sysName').addEventListener('keypress', function(event) {
	    if (event.keyCode == 13) {
	        event.preventDefault();
	    	$('#createBtn').trigger('click');
	    }
	  });
	document.getElementById('api_id').addEventListener('keypress', function(event) {
	    if (event.keyCode == 13) {
	        event.preventDefault();
	    	$('#createBtn').trigger('click');
	    }
	  });
	
	document.getElementById('url').addEventListener('keypress', function(event) {
	    if (event.keyCode == 13) {
	        event.preventDefault();
	    	$('#createBtn').trigger('click');
	    }
	  });
	
	$('#createBtn').click(function () {
		var statusNewId = document.getElementById("statusNewId").value;
		var statusClosedId = document.getElementById("statusClosedId").value;
		var statusInProgressId = document.getElementById("statusInProgressId").value;
		var statusVerifyId = document.getElementById("statusVerifyId").value;
		var priorityOneId = document.getElementById("priorityOneId").value;
		var priorityTwoId = document.getElementById("priorityTwoId").value;

	 	$('#statusNew_value').val(statusNewId);
	 	$('#statusClosed_value').val(statusClosedId);
		$('#statusInProgress_value').val(statusInProgressId);
		$('#statusVerify_value').val(statusVerifyId);
		
		$('#priorityOne_value').val(priorityOneId);
		$('#priorityTwo_value').val(priorityTwoId);
		
		if ($("#sysName").val().trim() != "" && $('#api_id').val() != 0 && $("#url").val().trim() != "" && $("input[type='radio']:checked").val()) {
			$('#statusId').val($("input[type='radio']:checked").val());

			if(!_validateUrl($('#url').val())){
				alert("Invalid Url");
				return;
			}

			if ($('#selected_apiId').text().toLowerCase().indexOf("ji")>=0) {
			
				if ($("#userId").val().trim() == "") {
					alert("User ID is required");
					return;
				} else if ($("#password").val().trim() == "") {
					alert("Password is required");
					return;
				}
			
				var qa = false;
				$('#dept input:checked').each(function() {
					if($(this).val().toLowerCase() == 'qa')
						qa = true;
				});

				if (qa) {
				//Validaton for priorities can not be duplicated
				if($("#priorityOne_value").val().trim() == ""){
					alert("P1 name is required");
					return;
				}
				
				if($("#priorityTwo_value").val().trim() == ""){
					alert("P2 name is required");
					return;
				}
				
				if ($("#priorityOne_value").val().trim() == $("#priorityTwo_value").val().trim() ) {
					alert("Priority names can not be same");
					return;
				} 
				
				if($("#statusNew_value").val().trim() == "" || $("#statusClosed_value").val().trim() == "" 
						|| $("#statusInProgress_value").val().trim() == "" || $("#statusVerify_value").val().trim() == ""){
					alert("All status names are mandatory");
					return;
				}
				
				if ($("#statusNew_value").val().trim() == $("#statusClosed_value").val().trim() 
						|| $("#statusNew_value").val().trim() == $("#statusInProgress_value").val().trim()
						|| $("#statusNew_value").val().trim() == $("#statusVerify_value").val().trim()
						|| $("#statusClosed_value").val().trim() == $("#statusInProgress_value").val().trim()
						|| $("#statusClosed_value").val().trim() == $("#statusVerify_value").val().trim()
						|| $("#statusInProgress_value").val().trim() == $("#statusVerify_value").val().trim())
				{
					alert("Status names should be unique");
					return;
				}
				}
			}
			
			
			if ($('#selected_apiId').text().toLowerCase().indexOf("je")>=0) {
				if ($("#userId").val().trim() != "" && $('#password').val() ==""){
					alert("Password is required");
					return;
				}
				else if	($("#password").val().trim() != "" && $('#userId').val() ==""){
					alert("User ID is required");
					return;
				}
			}
			
			
			var customKey_id_list = $('[id^=customKey]').map(function(){return $(this).attr("id");}).get();

			var customKey_list = $('[id^=customKey]').map(function(){return $(this).attr("value");}).get();
			var customValue_id_list = $('[id^=selected_customVal]').map(function(){return $(this).parent().attr("id");}).get();
			var customValue_list = $('[id^=selected_customVal]').map(function(){return $(this).attr("value");}).get();
			$('#custom_value').val(customValue_list);

			if (customKey_id_list.length > 0) {
				for (var j=0; j<customKey_id_list.length; j++) {
					var custKey = customKey_list[j];
					var custVal = customValue_list[j];

					if (custKey.trim()=="" && custVal.trim()!="") {
						alert("Enter Name");
						$("#"+customKey_id_list[j]).css('border', '1px solid red');
						return;
					} else if (custKey.trim()!="" && custVal.trim()=="") {
						alert("Select Equivalent To");
						$("#"+customValue_id_list[j]).css('border', '1px solid red');
						return;
					}

					for (var k=j+1; k<customKey_id_list.length; k++) {
						var custKey2 = customKey_list[k];
						var custVal2 = customValue_list[k];

						if (custKey==custKey2) {
							alert("Same combination of Name is not allowed");
							$("#"+customKey_id_list[j]).css('border', '1px solid red');
							$("#"+customKey_id_list[k]).css('border', '1px solid red');
							return;
						}
						if (custVal==custVal2) {
							alert("Same combination of Equivalent To is not allowed");
							$("#"+customValue_id_list[j]).css('border', '1px solid red');
							$("#"+customValue_id_list[k]).css('border', '1px solid red');
							return;
						}
					}
				}
			}

			var selected_dept = [];
			$('#dept input:checked').each(function() {
				selected_dept.push($(this).attr('value'));
			});

			if (selected_dept.length > 0)
				$('#department').val(selected_dept);
			else {
				alert("Select at least one 'API for' field");
				return;
			}

			var id = $("#sysId").val();
			var name = $("#sysName").val().trim();
			var flag = isNameExist('sys', id, name);

			if (flag == "true")
				alert("System API name already exists");
			else
				$("#create_form").submit();
		}
		else {
			if($("#sysName").val().trim() == "") {
				alert('Enter API Name');
				return;
			}
			if($('#api_id').val() == 0) {
				alert('Select API');
				return;
			}
			if($("#url").val().trim() == "") {
				alert('Enter Url');
				return;
			}
		}
	});

	$('#cancelBtn').click(function () {
		window.history.back();
	});


	$('#api_list li').click(function(event) {
		
		$('#selected_apiId').text($(this).text()).css('color', '#575757');
		$('#api_id').val($(this).val());
		$('#api_name').val($(this).text());

		$(this).parent().parent().removeClass("active");
		event.stopPropagation();
		
		if ($(this).text().toLowerCase().indexOf("je")>=0) {
			
			 if($("input[type='checkbox']").val() == "Dev"){
		
				 if(!($($("input[type='checkbox']")[0]).is(":disabled"))){
					 $($("input[type='checkbox']")[0]).attr("checked", false);
				 }
				 var headerName = $('.right-container h1').text();
				 if(headerName == 'Create New API'){
						$($("input[type='checkbox']")[1]).attr("checked", false);
					}
				   $($("input[type='checkbox']")[0]).attr("disabled", true);
				   $($('label:contains("Dev")')).removeClass("enabled-field");
		           $($('label:contains("Dev")')).addClass("disabled-field");
		           $($("input[type='checkbox']")[2]).attr("disabled", true);
		           $($('label:contains("Operations")')).addClass("disabled-field");
		    }
			$('#user_auth').show();
			$('#userIdReq, #pwasswordReq').hide();
			$('#custom_field').hide();
			$('#priority').hide();
			$('#status').hide();
		} else if ($(this).text().toLowerCase().indexOf("ji")>=0) {
			
			var headerName = $('.right-container h1').text();
			
			if($("input[type='checkbox']").val() == "Dev"){
				
				if(($($("input[type='checkbox']")[0]).is(":disabled")) && headerName == 'Edit API Details'){
					 $($("input[type='checkbox']")[0]).attr("checked", true);
				 } 
				
				if(headerName == 'Create New API'){
					$($("input[type='checkbox']")[1]).attr("checked", false);
				}
				$($("input[type='checkbox']")[0]).attr("disabled", false);
			    $($('label:contains("Dev")')).removeClass("disabled-field");
	            $($('label:contains("Dev")')).addClass("enabled-field");
	            $($("input[type='checkbox']")[2]).attr("disabled", true);
	            $($('label:contains("Operations")')).addClass("disabled-field");
	          }
			
			$('#user_auth').show();
			$('#userIdReq,#pwasswordReq').show();
			
			$('#dept input:checked').each(function() {
				if($(this).val().toLowerCase() == 'qa') {
					$('#custom_field').show();
					$('#priority').show();
					$('#status').show();
					return false;
				}
				else {
					$('#custom_field').hide();
					$('#priority').hide();
					$('#status').hide();
				}
			});
		}
	});
	$(document).ready(function() {
		var headerName = $('.right-container h1').text();
		if ('${sysDetails}') {
			<%
				int isQa = 0;
				ArrayList<String> departmentList = null;
				ArrayList customValueList = null;
				if (sysDetails != null) {
					isQa = ((SystemAPI) sysDetails).getIsQa();
					departmentList = (ArrayList)((SystemAPI) sysDetails).getDepartmentList();
					customValueList = (ArrayList)((SystemAPI) sysDetails).getCustomValueList();
				}

				if (sysDetails != null && departmentList != null) {
					for (String deptName : departmentList) {
					%>
						$('#dept input').each(function() {
							if ($(this).val() == '<%=deptName%>') {
								$(this).attr('checked','checked');
							}
						});
					<%
					}
				}

				if (sysDetails != null && customValueList != null && (apiName.toLowerCase().indexOf("ji") >= 0)) {
					for (int k=0; k<customValueList.size(); k++) {
						String customValue = customValueList.get(k).toString();
			%>
						$('#selected_customVal'+<%=k%>).text('<%=customValue%>').css('color', '#575757');
						$('#selected_customVal'+<%=k%>).attr('value','<%=customValue%>');
			<%
					}
				}
			%>

			$('#selected_apiId').text('${sysDetails.apiName}').css('color', '#575757');
			
			if(headerName == 'Edit API Details'){
				$("#api_dd").removeClass("dropdown-menu");
				$("#api_dd").addClass("dropdown-menu-disable disabledbutton");
			}
			
			if ($('#selected_apiId').text().toLowerCase().indexOf("je")>=0) {
				/* alert("Edit je");
				 if($("input[type='checkbox']").val() == "Dev"){
			           $($("input[type='checkbox']")[0]).attr("disabled", true);
			           $($('label:contains("Dev")')).removeClass("enabled-field");
			           $($('label:contains("Dev")')).addClass("disabled-field");
			           $($("input[type='checkbox']")[2]).attr("disabled", true);
			           $($('label:contains("Operations")')).addClass("disabled-field");
			    } */
			    $($('label:contains("Dev")')).addClass("disabled-field");
			    $($('label:contains("Operations")')).addClass("disabled-field");
			    
			    if($($('label:contains("Dev")')).hasClass("disabled-field") && headerName == 'Edit API Details'){
			    	$($("input[type='checkbox']")[0]).prop("disabled", 'disabled');
				 } 
			   
			    if($($('label:contains("Operations")')).hasClass("disabled-field") && headerName == 'Edit API Details'){
			    	$($("input[type='checkbox']")[2]).prop("disabled", 'disabled');
				 } 
				
				$('#user_auth').show();
				$('#custom_field').hide();
				$('#priority').hide();
				$('#status').hide();
			} else if ($(this).text().toLowerCase().indexOf("ji")>=0) {
				/* alert("Edit ji");
				if($("input[type='checkbox']").val() == "Dev"){
		            $($("input[type='checkbox']")[0]).attr("disabled", false);
		            $($('label:contains("Dev")')).removeClass("disabled-field");
		            $($('label:contains("Dev")')).addClass("enabled-field");
		            $($("input[type='checkbox']")[2]).attr("disabled", true);
		            $($('label:contains("Operations")')).addClass("disabled-field");
		          } */
		          
		         		          
		        $($('label:contains("Operations")')).addClass("disabled-field");
		          $($("input[type='checkbox']")[2]).prop("disabled", 'disabled');
				
				$('#user_auth').show();

				if (<%=isQa%> == 1) {
					$('#custom_field').show();
					$('#priority').show();
					$('#status').show();
				}
			} 
			 
		}
	
		$($('label:contains("Dev")')).removeClass("enabled-field");
		$($('label:contains("Operations")')).addClass("disabled-field");
		
		if($($('label:contains("Operations")')).hasClass("disabled-field") && headerName == 'Create New API'){
	    	$($("input[type='checkbox']")[2]).prop("disabled", 'disabled');
	    } 
		 
		
		$('.removeRow').on("click",null, function() {
			//$(this).closest('.system-dtls').remove();
			$(this).parent().parent().remove();
		});

		dropdownAutoSize();
		dropdown();
    });

	var i = 1000;
	function addRow() {
		var div = document.createElement('tr');
		div.className = 'system-dtls';
		div.innerHTML = '<td><input type="text" id="customKeyd" name="customKey" style="width: 210px;" value="" /></td>\
				<td>\
					<div class="dropdown-menu" id="custom_ddd" style="width: 150px;">\
						<div id="selected_customVald" value="">Select</div>\
						<ul class="dropdown" id="custom_value_listd">\
							<li>Environment</li>\
							<li>Severity</li>\
						</ul>\
					</div>\
					<div class="clear"></div>\
				</td>\
				<td><span class="removeRow" title="Delete Row"></span></td>';

		document.getElementById('system-dtls-tbl').appendChild(div);

		$('#customKeyd').attr('id', 'customKey'+i);
		$('#custom_ddd').css('z-index', 10000-i);
		$('#custom_ddd').attr('id', 'custom_dd'+i);
		$('#custom_value_listd').attr('id', 'custom_value_list'+i);
		$('#selected_customVald').attr('id', 'selected_customVal'+i);

		$('#customKey'+i).on('click', function() {
			$('[id^=customKey]').css('border', '');
		});
		$('#custom_value_list'+i).on('click', 'li', function(event) {
			$(this).parent().siblings("[id^=selected_customVal]").text($(this).text()).css('color', '#575757');
			$(this).parent().siblings("[id^=selected_customVal]").attr("value", $(this).text());
			$(this).parent().parent().removeClass("active");
			$('.dropdown-menu').css('border','');
			event.stopPropagation();
		});

		dropdownAutoSize();
		i++;
		dropdown();
	}

	function dropdown() {
		function DropDown(el) {
			this.sys_dd = el;
			this.initEvents();
		}

		DropDown.prototype = {
			initEvents : function() {
				var obj = this;
				obj.sys_dd.off('click').on('click', function(event) {
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
			var sys_dd = new DropDown($('.dropdown-menu'));
			$(document).click(function() {
				$('.dropdown-menu').removeClass('active');
			});
		});
	}

	function dropdownAutoSize() {
		if ('${fn:length(sys_list)}' > 5) {
			$('.dropdown').css('height','195px');
			$('.dropdown').css('overflow','auto');
		} else {
			$('.dropdown').css('height','auto');
			$('.dropdown').css('overflow','hidden');
		}
	}

	function validateUrl(url) {
		var re = /^(?:(?:https?|ftp):\/\/)(?:\S+(?::\S*)?@)?(?:(?!10(?:\.\d{1,3}){3})(?!127(?:\.\d{1,3}){3})(?!169\.254(?:\.\d{1,3}){2})(?!192\.168(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]+-?)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]+-?)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/[^\s]*)?$/i;
		return re.test(url);
	}

	function _validateUrl(url) {
		var re = /^(?:(?:https?|ftp):\/\/)(?:\S+(?::\S*)?@)?(?:(?!10(?:\.\d{1,3}){3})(?!127(?:\.\d{1,3}){3})(?!169\.254(?:\.\d{1,3}){2})(?!192\.168(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]+-?)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]+-?)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/[^\s]*)?$/i;
		var isSuccess = re.test(url);
		if (isSuccess) {
			return isSuccess;
		} else {
			isSuccess = validateUrlIPIf(url);
			return isSuccess;
		}
	}

	function validateUrlIPIf(url) {
		return ValidateIPaddress(url);
	}

	function ValidateIPaddress(ipaddress) {  
		//var regexp = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
		var regexp1 = ipaddress.match(/\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):[0-9]{1,5}/g);
	 	if (regexp1!=null) {
	    	return (true);
		}

		//alert("You have entered an invalid IP address!");
		return (false);
	}
</script>