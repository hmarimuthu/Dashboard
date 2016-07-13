<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<script type="text/javascript" >
</script>
<%@page import="java.util.ArrayList"%>
<%@page import="com.etaap.domain.Application"%>
<%@page import="com.etaap.domain.SystemAPI"%>

<div class="right-container" style="height:100%; width:100%">
	<%
		Object appDetails = request.getAttribute("appDetails");
		int api = ((Application) appDetails).getApiId();
		System.out.println("api: "+api);
		String dept = ((Application) appDetails).getDeptType();
		String app_name = ((Application) appDetails).getAppName();
		boolean flag = ((Application) appDetails).isFlag();
		int status = ((Application) appDetails).getStatus();
		ArrayList urlAlias_listfromDB = (ArrayList) request.getAttribute("urlAlias_listfromDB");
		System.out.println("urlAlias_listfromDB: "+urlAlias_listfromDB);
		
	 	String title = "";
		String actionUrl = "";
	
		if(appDetails != null && app_name != null && flag) {
			title = "Edit Application Details";
			actionUrl = "update?paramName=app&apiId="+api+"&deptType="+dept;
		} else {
			title = "Create New Application";
			actionUrl = "create?paramName=app&apiId="+api+"&deptType="+dept;
		}


		String contextPath = request.getContextPath();
	%>
	<h1><%= title %></h1>
	<div class="form-container section-container">
	<form:form id="create_form" action="<%= actionUrl %>" method="post" modelAttribute="application">
		<div class="form-elements">
			<div class="label-name">Application Name<sup class="req">*</sup>:</div>
			<div class="input-field"><form:input type="text" id="appName" path="appName" value="${appDetails.appName}" maxlength="35" specialChar ="_-"/></div>
			<div class="clear"></div>
		</div>
		<div class="form-elements">
			<div class="label-name">Fiscal Year Start Month<sup class="req">*</sup>:</div>
			<div class="input-field">
				<div class="dropdown-menu" id="month_dd">
					<div id="selected_monthId" >Select</div>
					<ul class="dropdown" id="month_list" style="z-index:100;">
						<li value="1">January</li>
						<li value="2">February</li>
						<li value="3">March</li>
						<li value="4">April</li>
						<li value="5">May</li>
						<li value="6">June</li>
						<li value="7">July</li>
						<li value="8">August</li>
						<li value="9">September</li>
						<li value="10">October</li>
						<li value="11">November</li>
						<li value="12">December</li>
					</ul>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		<div class="form-elements">
			<div class="label-name">Active<sup class="req">*</sup>:</div>
			<div class="input-field radio-cnt">
				<div>
				<% if(appDetails != null && app_name != null) { %>
					<input id="option" type="radio" name="field" value="1" <c:if test="${appDetails.status == 1}">checked</c:if> />
					<label for="option"><span><span></span></span>Yes</label>
					<form:input type="hidden" id="applicationId" path="appId" value="${appDetails.appId}" />
				<% } else { %>
					<input id="option" type="radio" name="field" value="1" checked />
					<label for="option"><span><span></span></span>Yes</label>
				<% } %>
				</div>
				<div>
				<% if(appDetails != null && app_name != null) { %>
					<input id="option1" type="radio" name="field" value="0" <c:if test="${appDetails.status == 0}">checked</c:if> />
					<label for="option1"><span><span></span></span>No</label>
				<% } else { %>
					<input id="option1" type="radio" name="field" value="0" />
					<label for="option1"><span><span></span></span>No</label>
				<% } %>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		
		<% if (appDetails != null && api > 0) { %>
		
		<h3 class="h3-title">API Details</h3>
		
		
		
			<% if (appDetails != null && app_name != null && flag) {
				ArrayList<SystemAPI> sys_list = (ArrayList<SystemAPI>) request.getAttribute("sys_list");
				/*ArrayList sysList = (ArrayList)((Application) appDetails).getSysIdList();*/
				ArrayList mapList = (ArrayList)((Application) appDetails).getMapIdList();
				ArrayList tabList = (ArrayList)((Application) appDetails).getTabIdList();
				ArrayList urlAliasList = (ArrayList)((Application) appDetails).getUrlAliasList();
				ArrayList envList = (ArrayList)((Application) appDetails).getEnvIdList();
				ArrayList suiteList = (ArrayList)((Application) appDetails).getSuiteIdList();
				ArrayList bedList = (ArrayList)((Application) appDetails).getBedIdList();
				ArrayList defaultList = (ArrayList)((Application) appDetails).getIsDefaultList();
				ArrayList apiList = (ArrayList)((Application) appDetails).getApiIdList();
				ArrayList urlAliasConcatedList = (ArrayList)((Application) appDetails).getConcatedUrlAliasList();
			%>
			<!-- API tabs content container starts here -->
			<div class="api-tabs-container">
        	<!-- horizontal tabs starts here-->
        		<div class="horz-tabs">
          			<ul id="dept" class="horz-tabs">
						<c:forEach var="dept" items="${deptList}">
							 <li class="tab-title <c:if test="${appDetails.deptType == dept.value}">active-2</c:if>" id="dept-${dept.key}" value="${dept.value}">
							 <a href="edit?paramName=app&recordId=${appDetails.appId}&apiId=${appDetails.apiId}&deptType=${dept.value}">${dept.value}</a>
							 </li> 
							<%--<li class="tab-title <c:if test="${appDetails.deptType == dept.value}">active-2</c:if>" id="dept-${dept.key}" value="${dept.value}"><a href="javascript:getValue('edit?paramName=app&recordId=${appDetails.appId}&apiId=${appDetails.apiId}&deptType=${dept.value}')">${dept.value}</a></li>--%>
						</c:forEach>
					</ul>
          	<!-- horizontal tabs ends here --> 
        		</div>
        		<div class="clear"></div>
        	<!-- tab-container starts here -->
        		<div class="tab-container"> 
          	<!-- vertical tabs starts here-->
          			<ul id="api" class="vert-tabs">
						<c:forEach var="api" items="${apiList}">
							 <li class="tab-title <c:if test="${appDetails.apiId == api.key}">active-2</c:if>" id="api-${api.key}" value="${api.value}"><a href="edit?paramName=app&recordId=${appDetails.appId}&apiId=${api.key}&deptType=${appDetails.deptType}">${api.value}</a></li>
							<%--<li class="tab-title <c:if test="${appDetails.apiId == api.key}">active-2</c:if>" id="api-${api.key}" value="${api.value}"><a href="javascript:getValue('edit?paramName=app&recordId=${appDetails.appId}&apiId=${api.key}&deptType=${appDetails.deptType}">${api.value}</a></li> --%>
						</c:forEach>
					</ul>
          	<!-- vertical tabs ends here -->
          	<!-- API table starts here -->
          			<div class="api-table-container">
            			<div class="api-table">
              				<div class="action-menu">
                				<div class="add-record" onclick="addRow(${appDetails.apiId}, '');" title="Add New Row">
                  					<p><a href="#">Add Row</a></p>
                				</div>
                				<!--div class="pagination-container">
                  					<p class="page-details"><span>Showing results</span>
                    				<input type="text" value="4" />
                    				<span>of 5 pages for 20 records</span></p>
                				</div-->
                				<div class="delete-record" id="deleteRecord" ><span><a href="#">Delete</a></span></div>
                				<div class="clear"></div>
							</div>
              				<div class="clear"></div>
								<div class="form-elements creat-prop-lay">
								<table id="system-dtls-tbl" class="table api-table-details" cellpadding="0" cellspacing="0">
									<tr>
                    					<th class="api-table-heading checkbox-cnt">
                    						<!-- <div class="checkbox-cnt"> -->
                        						<input id="default_checkbox" type="checkbox" value="">
                        						<label for="default_checkbox"></label>
                      						<!-- </div> -->
                    					</th>
                    					<% if (!dept.equalsIgnoreCase("dev")) { %>
                    					<th class="api-table-heading" width="20">Default</th>
                    					<% } %>
                    					<th class="api-table-heading" width="175">Name</th>
                    					<th class="api-table-heading" width="75">API Name</th>
                    					<% if (!dept.equalsIgnoreCase("dev")) { %>
                    					<th class="api-table-heading" width="130">Environment Name</th>
                    					<th class="api-table-heading <c:if test="${appDetails.apiId == 2}">td-hide</c:if>" width="130">Suite Name</th>
                    					<th class="api-table-heading <c:if test="${appDetails.apiId == 2}">td-hide</c:if>" width="130">Bed Name</th>
                    					<% } %>
                  					</tr>
                          			<%
                          			for (int a=0; a<sys_list.size(); a++) {
        								SystemAPI sys = (SystemAPI) sys_list.get(a);
        								String hideShowTh = sys.getApiId() == 2 ? "td-hide" : "";

	                          			if (mapList != null) {
	                          				for (int b=0; b<tabList.size(); b++) {
	                          					int apiid =Integer.parseInt(apiList.get(b).toString());
	                          					int sysid =Integer.parseInt(apiList.get(b).toString());
	                          					String jenkinsId = sysid+""+b;
	                          					String checkid = mapList.get(b).toString();
	                          					String checkboxid = checkid.replace(';', '_');
	                          					if (String.valueOf(sys.getSysId()).equalsIgnoreCase(tabList.get(b).toString())) {
	                          						int sysapiIndex = 11000-b;
	                          						int envIndex = 10000-b;
	              									int suiteIndex = 9000-b;
	              									int bedIndex = 8000-b;
	                          						String defaultValue = Integer.parseInt(defaultList.get(b).toString()) == 1 ? "checked" : "";
	                          						String ddEnableDisable = sys.getApiId() == 2 ? "dropdown-menu-disable" : "dropdown-menu";
	                          						String hideShowCol = sys.getApiId() == 2 ? "td-hide" : "";
                          			%>
									<tr class="system-dtls">
										<td class="checkbox-cnt">
                    						<!-- <div class="checkbox-cnt"> -->
                    					
	                        					<input id="checkbox_<%=checkboxid%>" class="checkApp" type="checkbox" value="<%=mapList.get(b) %>">
	                        					<label id="checkbox_lb<%=mapList.get(b) %>" for="checkbox_<%=checkboxid%>"></label>
                      						<!-- </div> -->
                      					</td>
                      					<% if (!dept.equalsIgnoreCase("dev")) { %>
										<td class="radio-td">
											<input id="default_rd<%=mapList.get(b) %>" type="radio" name="default<%=apiid %>" value="<%=defaultList.get(b)%>" <%=defaultValue %> />
		 									<label id="default_lb<%=mapList.get(b) %>" for="default_rd<%=mapList.get(b) %>"><span><span></span></span></label>
		 								</td>
		 								<% } %>
										<td class="name"><% if (apiid ==1) { %>
											<%-- <input type="textarea" id="urlAlias<%=jenkinsId %>" name="urlAlias" class="api-table-cell" value="<%=urlAliasList.get(b) %>" /> --%>
											<textarea id="urlAlias<%=jenkinsId%>" name="urlAlias" rows="2" cols="10" wrap="Hard" style="width: 190px; height:20px;" value="<%=urlAliasList.get(b) %>" ><%=urlAliasList.get(b) %></textarea>
											<!--input type="hidden" id="tabId" name="tabId" value="<%=tabList.get(b) %>" /-->
											<input type="hidden" id="apiId" name="apiId" value="${appDetails.apiId}" />
											<input type="hidden" id="mapId" name="mapId" value="<%=mapList.get(b)+":"+urlAliasConcatedList.get(b)%>" />
											<% } else if (apiid ==2 && dept.equalsIgnoreCase("dev")) { %>
											<%-- <input type="textarea" id="urlAlias<%=mapList.get(b) %>" name="urlAlias" class="api-table-cell" value="<%=urlAliasList.get(b) %>" /> --%>
											<input type="text" id="urlAlias<%=mapList.get(b) %>" name="urlAlias" rows="2" cols="10" wrap="Hard" style="width: 190px; height:18px;" value="<%=urlAliasList.get(b) %>"  /> 
											<!--input type="hidden" id="tabId" name="tabId" value="<%=tabList.get(b) %>" /-->
											<input type="hidden" id="apiId" name="apiId" value="${appDetails.apiId}" />
											<input type="hidden" id="mapId" name="mapId" value="<%=mapList.get(b) %>" />
											<% } else { %>
											<%-- <input type="textarea" id="urlAlias<%=mapList.get(b) %>" name="urlAlias" class="api-table-cell" value="<%=urlAliasList.get(b) %>" /> --%>
											<input type="text" id="urlAlias<%=mapList.get(b) %>" name="urlAlias" rows="2" cols="10" wrap="Hard" style="width: 190px; height:18px;" value="<%=urlAliasList.get(b) %>" />
											<!--input type="hidden" id="tabId" name="tabId" value="<%=tabList.get(b) %>" /-->
											<input type="hidden" id="apiId" name="apiId" value="${appDetails.apiId}" />
											<input type="hidden" id="mapId" name="mapId" value="<%=mapList.get(b) %>" />
											<% } %>
										</td>
										<td> <% if(apiid == 1) { %>
											<div class="api-table-cell input-field drpdwn-container dropdown-menu" id="sysapi_dd<%=jenkinsId %>">
												<div class="name-drpdwn" id="selected_sysapiId<%=jenkinsId %>" sysapiid="<%=tabList.get(b)%>">Select</div>
												<ul class="dropdown" id="sysapi_list<%=jenkinsId %>" style="z-index:<%=sysapiIndex %>;">
													<c:forEach var="sysapi" items="${sys_list}">
														<li id="sysapiId-${sysapi.sysId}" value="${sysapi.sysId}" name="${sysapi.sysName}">${sysapi.sysName}</li>
													</c:forEach>
												</ul>
											</div>
											<div class="clear"></div>
											<% } else if (apiid ==2 && dept.equalsIgnoreCase("dev")) { %>
											<div class="api-table-cell input-field drpdwn-container dropdown-menu" id="sysapi_dd<%=mapList.get(b) %>">
												<div class="name-drpdwn" id="selected_sysapiId<%=mapList.get(b) %>" sysapiid="<%=tabList.get(b)%>">Select</div>
												<ul class="dropdown" id="sysapi_list<%=mapList.get(b) %>" style="z-index:<%=sysapiIndex %>;">
													<c:forEach var="sysapi" items="${sys_list}">
														<li id="sysapiId-${sysapi.sysId}" value="${sysapi.sysId}" name="${sysapi.sysName}">${sysapi.sysName}</li>
													</c:forEach>
												</ul>
											</div>
											<div class="clear"></div>
											<% } else { %>
											<div class="api-table-cell input-field drpdwn-container dropdown-menu" id="sysapi_dd<%=mapList.get(b) %>">
												<div class="name-drpdwn" id="selected_sysapiId<%=mapList.get(b) %>" sysapiid="<%=tabList.get(b)%>">Select</div>
												<ul class="dropdown" id="sysapi_list<%=mapList.get(b) %>" style="z-index:<%=sysapiIndex %>;">
													<c:forEach var="sysapi" items="${sys_list}">
														<li id="sysapiId-${sysapi.sysId}" value="${sysapi.sysId}" name="${sysapi.sysName}">${sysapi.sysName}</li>
													</c:forEach>
												</ul>
											</div>
											<div class="clear"></div>
											<% } %>
										</td>
										<% if (!dept.equalsIgnoreCase("dev")) { %>
										<td> <% if(apiid == 1) { %>
											<div class="api-table-cell input-field drpdwn-container dropdown-menu" id="env_dd<%=jenkinsId %>">
												<div class="name-drpdwn" id="selected_envId<%=jenkinsId %>" envid="<%=envList.get(b)%>">Select</div>
												<ul class="dropdown" id="env_list<%=jenkinsId %>" style="z-index:<%=envIndex %>;">
													<c:forEach var="env" items="${_env_list}">
														<li id="envId-${env.envId}" value="${env.envId}" name="${env.envName}">${env.envName}</li>
													</c:forEach>
												</ul>
											</div>
											<div class="clear"></div>
											<% } else { %>
											<div class="api-table-cell input-field drpdwn-container dropdown-menu" id="env_dd<%=mapList.get(b) %>">
												<div class="name-drpdwn" id="selected_envId<%=mapList.get(b) %>" envid="<%=envList.get(b)%>">Select</div>
												<ul class="dropdown" id="env_list<%=mapList.get(b) %>" style="z-index:<%=envIndex %>;">
													<c:forEach var="env" items="${_env_list}">
														<li id="envId-${env.envId}" value="${env.envId}" name="${env.envName}">${env.envName}</li>
													</c:forEach>
												</ul>
											</div>
											<div class="clear"></div>
											<% } %>
										</td>
										<td class="<%=hideShowCol %>">
										<% if(apiid == 1) { %>
											<div class="api-table-cell input-field drpdwn-container <%=ddEnableDisable %>" id="suite_dd<%=jenkinsId %>">
												<div class="name-drpdwn" id="selected_suiteId<%=jenkinsId %>" suiteid="<%=suiteList.get(b)%>">Select</div>
												<ul class="dropdown" id="suite_list<%=jenkinsId %>" style="z-index:<%=suiteIndex %>;">
													<c:forEach var="suite" items="${_suite_list}">
														<li id="suiteId-${suite.suiteId}" value="${suite.suiteId}" name="${suite.suiteName}">${suite.suiteName}</li>
													</c:forEach>
												</ul>
											</div>
											<div class="clear"></div>
											<% } else { %>
											<div class="api-table-cell input-field drpdwn-container <%=ddEnableDisable %>" id="suite_dd<%=mapList.get(b) %>">
												<div class="name-drpdwn" id="selected_suiteId<%=mapList.get(b) %>" suiteid="<%=suiteList.get(b)%>">Select</div>
												<ul class="dropdown" id="suite_list<%=mapList.get(b) %>" style="z-index:<%=suiteIndex %>;">
													<c:forEach var="suite" items="${_suite_list}">
														<li id="suiteId-${suite.suiteId}" value="${suite.suiteId}" name="${suite.suiteName}">${suite.suiteName}</li>
													</c:forEach>
												</ul>
											</div>
											<div class="clear"></div>
											<% } %>
										</td>
										<td class="<%=hideShowCol %>">
										<% if(apiid == 1) { %>
											<div class="api-table-cell input-field drpdwn-container <%=ddEnableDisable %>" id="bed_dd<%=jenkinsId %>">
												<div class="name-drpdwn" id="selected_bedId<%=jenkinsId %>" bedid="<%=bedList.get(b)%>">Select</div>
												<ul class="dropdown" id="bed_list<%=jenkinsId %>" style="z-index:<%=bedIndex %>;">
													<c:forEach var="bed" items="${_bed_list}">
														<li id="bedId-${bed.bedId}" value="${bed.bedId}" name="${bed.bedName}">${bed.bedName}</li>
													</c:forEach>
												</ul>
											</div>
											<div class="clear"></div>
											<% } else { %>	
											<div class="api-table-cell input-field drpdwn-container <%=ddEnableDisable %>" id="bed_dd<%=mapList.get(b) %>">
												<div class="name-drpdwn" id="selected_bedId<%=mapList.get(b) %>" bedid="<%=bedList.get(b)%>">Select</div>
												<ul class="dropdown" id="bed_list<%=mapList.get(b) %>" style="z-index:<%=bedIndex %>;">
													<c:forEach var="bed" items="${_bed_list}">
														<li id="bedId-${bed.bedId}" value="${bed.bedId}" name="${bed.bedName}">${bed.bedName}</li>
													</c:forEach>
												</ul>
											</div>
											<div class="clear"></div>
											<% } %>
										</td>
										<% } %>
										<!-- <td><span class="removeRow" title="Delete Row"></span></td> -->
									</tr>
								<% 				}
	                          				}
	                          			}
                          			}
                          		%>
								</table>
                            <!--p class="note"><b>Note:</b><br/><b>For Jenkins:</b> Name indicates Job Name. It should be same as in Jenkins.<br/><b>For Jira:</b><br>1) Name indicates Application Alias Name. It should be same as in Jira.<br>2) Environment Name should be same as in Jira.</p-->
							</div>
							<div class="clear"></div>
              				<div class="action-menu">
                				<div class="add-record" onclick="addRow(${appDetails.apiId}, '');" title="Add New Row">
                  					<p><a href="#">Add Row</a></p>
                				</div>
                				<!--div class="pagination-container">
                  					<p class="page-details"><span>Showing results</span>
                    				<input type="text" value="4" />
                    				<span>of 5 pages for 20 records</span> </p>
                				</div-->
                				<div class="delete-record" id="deleteRecord" ><span><a href="#">Delete</a></span></div>
                				<div class="clear"></div>
              				</div>
              			</div>
			<!-- API table starts here --> 
					</div>
          			<div class="clear"></div>
			<!-- tab-container ends here --> 
        		</div>
			<!-- API tabs content container ends here --> 
      		</div>
			<%
				 } else {
					 int rdButton = 1;
			%>
			<!-- API tabs content container starts here -->
			<div class="api-tabs-container">
        	<!-- horizontal tabs starts here-->
        		<div class="horz-tabs">
          			<ul id="dept" class="horz-tabs">
						<c:forEach var="dept" items="${deptList}">
							<li class="tab-title <c:if test="${appDetails.deptType == dept.value}">active-2</c:if>" id="dept-${dept.key}" value="${dept.value}"><a href="javascript:getValue('create?paramName=app&apiId=${appDetails.apiId}&deptType=${dept.value}')">${dept.value}</a></li>
						</c:forEach>
					</ul>
          	<!-- horizontal tabs ends here --> 
        		</div>
        		<div class="clear"></div>
        	<!-- tab-container starts here -->
        		<div class="tab-container"> 
          	<!-- vertical tabs starts here-->
          			<ul id="api" class="vert-tabs">
						<c:forEach var="api" items="${apiList}">
							<li class="tab-title <c:if test="${appDetails.apiId == api.key}">active-2</c:if>" id="api-${api.key}" value="${api.value}"><a href="javascript:getValue('create?paramName=app&apiId=${api.key}&deptType=${appDetails.deptType}')">${api.value}</a></li>
						</c:forEach>
					</ul>
          	<!-- vertical tabs ends here -->
          	<!-- API table starts here -->
          			<div class="api-table-container">
            			<div class="api-table">
              				<div class="action-menu">
                				<div class="add-record" onclick="addRow(${appDetails.apiId}, '');" title="Add New Row">
                  					<p><a href="#">Add Row</a></p>
                				</div>
                				<!-- <div class="pagination-container">
                  					<p class="page-details"><span>Showing results</span>
                    				<input type="text" value="4" />
                    				<span>of 5 pages for 20 records</span></p>
                				</div> -->
                				<div class="delete-record" id="deleteRecord" ><span><a href="#">Delete</a></span></div>
                				<div class="clear"></div>
							</div>
              				<div class="clear"></div>
								<div class="form-elements creat-prop-lay">
								<table id="system-dtls-tbl" class="table api-table-details" cellpadding="0" cellspacing="0">
									<tr>
                    					<th class="api-table-heading checkbox-cnt">
                    						<!-- <div class="checkbox-cnt"> -->
                        						<input id="default_checkbox" type="checkbox" value="">
                        						<label for="default_checkbox"></label>
                      						<!-- </div> -->
                    					</th>
                    					<% if (!dept.equalsIgnoreCase("dev")) { %>
                    					<th class="api-table-heading" width="20">Default</th>
                    					<% } %>
                    					<th class="api-table-heading" width="175">Name</th>
                    					<th class="api-table-heading" width="75">API Name</th>
                    					<% if (!dept.equalsIgnoreCase("dev")) { %>
                    					<th class="api-table-heading" width="130">Environment Name</th>
                    					<th class="api-table-heading <c:if test="${appDetails.apiId == 2}">td-hide</c:if>" width="130">Suite Name</th>
                    					<th class="api-table-heading <c:if test="${appDetails.apiId == 2}">td-hide</c:if>" width="130">Bed Name</th>
                    					<% } %>
                  					</tr>
              					</table>
              				</div>
							<div class="clear"></div>
              				<div class="action-menu">
                				<div class="add-record" onclick="addRow(${appDetails.apiId}, '');" title="Add New Row">
                  					<p><a href="#">Add Row</a></p>
                				</div>
                				<!-- <div class="pagination-container">
                  					<p class="page-details"><span>Showing results</span>
                    				<input type="text" value="4" />
                    				<span>of 5 pages for 20 records</span> </p>
                				</div> -->
                				<div class="delete-record" id="deleteRecord" ><span><a href="#">Delete</a></span></div>
                				<div class="clear"></div>
              				</div>
              			</div>
			<!-- API table starts here --> 
					</div>
          			<div class="clear"></div>
			<!-- tab-container ends here --> 
        		</div>
			<!-- API tabs content container ends here --> 
      		</div>
			<% } %>
			
			<% } %>

		<form:input type="hidden" id="statusId" path="status" value="" />
		<form:input type="hidden" id="sys_id" path="systemId" value="" />
		<form:input type="hidden" id="env_id" path="envIds" value="" />
		<form:input type="hidden" id="suite_id" path="suiteIds" value="" />
		<form:input type="hidden" id="bed_id" path="bedIds" value="" />
		<form:input type="hidden" id="is_default" path="isDefault" value="" />
		<form:input type="hidden" id="month_id" path="monthId" value="${appDetails.monthId}" />
		<form:input type="hidden" id="month_name" path="monthName" value="${appDetails.monthName}" />
	
		<%-- <form:input type="hidden" id="appName" path="appName" value="${appName}" /> --%>
		<div class="btn-container">
			<input type="button" id="createBtn" class="btn-blue" value="SAVE"><input type="button" id="cancelBtn" class="btn-red" value="CANCEL">
		</div>
	</form:form>
	</div>
</div>
<script type="text/javascript">

<%-- 		function urlAliasCheck(aliasUrl) {
		//	alert(aliasUrl);
			var urlAliasUI = $(aliasUrl).map(function(){return $(this).attr("value");}).get();
		//	alert(urlAliasUI);
			var urlAliasList="<%=urlAlias_listfromDB%>";
			var selectedApiName = $('#api li.active-2').text();
	        var selectedDeptName= $('#dept li.active-2').text();
	        
	         if(selectedApiName.trim() == 'Jira'  && selectedDeptName.trim() == "Dev"){
	       // 	 alert(urlAliasUI);
	    	 	if(urlAliasUI != "" && urlAliasList.indexOf(urlAliasUI) != -1){
					alert("Rapidview Id "+urlAliasUI+" has already mapped with some other application");
				}
		      }
		} --%>

	function getValue(url)
	{
		var appName =  document.getElementById('appName').value;
		var monthName = $('#selected_monthId').html();
		var monthId = $('#month_id').val();
		var newURL = url + "&app_Name=" + appName + "&monthId=" + monthId + "&month_Name=" + monthName + "&flag=false";
		location.href = newURL;
	}
	// function for ener event
	document.getElementById('appName').addEventListener('keypress', function(event) {
	    if (event.keyCode == 13) {
            event.preventDefault();
	    	$('#createBtn').trigger('click');
        }
	  });
	
	/* document.onkeydown = function (evt) {
		  var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
		  if (keyCode == 13) {
			// For Enter.
			$('#createBtn').trigger('click');
			return false;
		  }
		}; */

	$('#month_list li').click(function(event) {
		$('#selected_monthId').text($(this).text()).css('color', '#575757');
		$('#month_id').val($(this).val());
		$('#month_name').val($(this).text());
		$(this).parent().parent().removeClass("active");
		event.stopPropagation();
	});
	$('[id^=urlAlias]').on('click', function() {
		$('[id^=urlAlias]').css('border', '');
	});
	$('[id^=sysapi_list]').on('click', 'li', function(event) {
		$(this).parent().siblings("[id^=selected_sysapiId]").text($(this).text()).css('color', '#575757');
		$(this).parent().siblings("[id^=selected_sysapiId]").attr("sysapiid", $(this).val());
		$(this).parent().parent().removeClass("active");
		$('.dropdown-menu').css('border','');
		event.stopPropagation();
	});
	$('[id^=env_list]').on('click', 'li', function(event) {
		$(this).parent().siblings("[id^=selected_envId]").text($(this).text()).css('color', '#575757');
		$(this).parent().siblings("[id^=selected_envId]").attr("envid", $(this).val());
		$(this).parent().parent().removeClass("active");
		$('.dropdown-menu').css('border','');
		event.stopPropagation();
	});
	$('[id^=suite_list]').on('click', 'li', function(event) {
		$(this).parent().siblings("[id^=selected_suiteId]").text($(this).text()).css('color', '#575757');
		$(this).parent().siblings("[id^=selected_suiteId]").attr("suiteid", $(this).val());
		$(this).parent().parent().removeClass("active");
		$('.dropdown-menu').css('border','');
		event.stopPropagation();
	});
	$('[id^=bed_list]').on('click', 'li', function(event) {
		$(this).parent().siblings("[id^=selected_bedId]").text($(this).text()).css('color', '#575757');
		$(this).parent().siblings("[id^=selected_bedId]").attr("bedid", $(this).val());
		$(this).parent().parent().removeClass("active");
		$('.dropdown-menu').css('border','');
		event.stopPropagation();
	});
	$('[id^=default_rd]').on('click', function() {
		$('[name^='+$(this).attr('name')+']').val(0);
		$(this).val(1);
	});
	$('#deleteRecord').live( "click", function(){
		//alert("inside delete");
		  var mapids;
		  var checkedValues = $('.checkApp:checked').map(function() {
		   if (this.value.length > 0) {
		    var deleteIdsArray = this.value.split(';');
		    return deleteIdsArray;
		   }
		  }).get();

		  var idValues = $('.checkApp:checked').map(function() {
		   var IdsArray = this.id;
		   return IdsArray;
		  }).get();

		  if (checkedValues.length == 0 && idValues.length == 0) {
		       alert('please select checkbox');
		       return;
		  }

		  if (checkedValues.length == 0 && idValues.length > 0) {
		   for (var j = 0; j < idValues.length; j++) {
		    $('#'+idValues[j]).parent().parent().remove();
		    $("#default_checkbox").prop("checked", false);
		   }
		  }
		   if (checkedValues.length > 0) {
		   mapids = checkedValues.toString();
		   var flag = disableRecordForApp(mapids);
		   if(flag == "true") { 
		    for (var j = 0; j < idValues.length; j++){
		    $('#'+idValues[j]).parent().parent().remove();
		    $("#default_checkbox").prop("checked", false);
		    }
		   }
		  }
		   hideShowDelete();
		 });
	dropdownAutoSize();
	
	$('#createBtn').click(function (e) {
		
		e.preventDefault();
		
			var urlAliasUI = $('[name^=urlAlias]').map(function(){return $(this).attr("value");}).get();
			//alert(urlAliasUI);
			var urlAliasList="<%=urlAlias_listfromDB%>";
			var selectedApiName = $('#api li.active-2').text();
	        var selectedDeptName= $('#dept li.active-2').text();
	        
	         if(selectedApiName.trim() == 'Jira'  && selectedDeptName.trim() == "Dev"){
	        	//alert("Jira"+urlAliasUI);
	    	 	if(urlAliasUI != "" && urlAliasList.indexOf(urlAliasUI) != -1){
					var r = confirm("Rapidview Id "+urlAliasUI+" is already mapped");
					if (r == true) {
					} else {
					   return false;
					} 
				}
		      } 
		
			if ($("#appName").val().trim() != "" && $("input[type='radio']:checked").val() 
					&& $('#month_id').val() != 0) {
				//alert("3")
				$('#statusId').val($("input[type='radio']:checked").val());
	
				var tabId_list = $('[id^=tabId]').map(function(){return $(this).attr("value");}).get();
				var apiId_list = $('[id^=apiId]').map(function(){return $(this).attr("value");}).get();
				var urlAlias_id_list = $('[id^=urlAlias]').map(function(){return $(this).attr("id");}).get();
				var urlAlias_list = $('[id^=urlAlias]').map(function(){return $(this).attr("value");}).get();
				var sysapiid_id_list = $('[id^=selected_sysapiId]').map(function(){return $(this).parent().attr("id");}).get();
				var sysapiid_list = $('[id^=selected_sysapiId]').map(function(){return $(this).attr("sysapiid");}).get();
				var envid_id_list = $('[id^=selected_envId]').map(function(){return $(this).parent().attr("id");}).get();
				var envid_list = $('[id^=selected_envId]').map(function(){return $(this).attr("envid");}).get();
				var suiteid_id_list = $('[id^=selected_suiteId]').map(function(){return $(this).parent().attr("id");}).get();
				var suiteid_list = $('[id^=selected_suiteId]').map(function(){return $(this).attr("suiteid");}).get();
				var bedid_id_list = $('[id^=selected_bedId]').map(function(){return $(this).parent().attr("id");}).get();
				var bedid_list = $('[id^=selected_bedId]').map(function(){return $(this).attr("bedid");}).get();
				var default_list = $('[name^=default]').map(function(){return $(this).attr("value");}).get();
				$('#sys_id').val(sysapiid_list);
				$('#env_id').val(envid_list);
				$('#suite_id').val(suiteid_list);
				$('#bed_id').val(bedid_list);
				$('#is_default').val(default_list);
				//alert("env"+envid_list);
				//alert("apisys"+sysapiid_list);
				//alert("url" +urlAlias_list);
				
				<%-- var urlAliasUI = $('[name^=urlAlias]').map(function(){return $(this).attr("value");}).get();
				var urlAliasList="<%=urlAlias_listfromDB%>";
				if(urlAliasList.indexOf(urlAliasUI) != -1){
					alert("Already mapped tp other application");
				} --%>
		
				
				if (envid_list.length > 0) {
					for (var ii=0; ii<envid_list.length; ii++) {
						var apiid1 = <%=api%>;//apiId_list[0];
						//alert("envid list ++"+apiid1);
						var jobname = urlAlias_list[ii];
						var sysapi1 = sysapiid_list[ii];
						var env1 = envid_list[ii];
						var suite1 = suiteid_list[ii];
						var bed1 = bedid_list[ii];
	
						if (apiid1==1) {
							if (jobname=="") {
								alert("Enter Job Name");
								$("#"+urlAlias_id_list[ii]).css('border', '1px solid red');
								return;
							} else if (sysapi1==0) {
								alert("Select API");
								$("#"+sysapiid_id_list[ii]).css('border', '1px solid red');
								return;
							} else if (env1==0) {
								alert("Select Environment");
								$("#"+envid_id_list[ii]).css('border', '1px solid red');
								return;
							} else if (suite1==0) {
								alert("Enter Test Suite");
								$("#"+suiteid_id_list[ii]).css('border', '1px solid red');
								return;
							} else if (bed1==0) {
								alert("Enter Test Bed");
								$("#"+bedid_id_list[ii]).css('border', '1px solid red');
								return;
							}
						} else if (<%=api%>==2 && <%=dept.equalsIgnoreCase("dev")%>) {
							var jobname = urlAlias_list[ii];
							var sysapi1 = sysapiid_list[ii];
							if (jobname.trim()=="") {
								alert("Enter Rapid View Id in Name field");
								$("#"+urlAlias_id_list[ii]).css('border', '1px solid red');
								return;
							}  else if (sysapi1==0) {
								alert("Select API");
								$("#"+sysapiid_id_list[ii]).css('border', '1px solid red');
								return;
							}
						} else {
							if (jobname.trim()=="") {
								alert("Enter Jira Alias Name");
								$("#"+urlAlias_id_list[ii]).css('border', '1px solid red');
								return;
							}  else if (sysapi1==0) {
								alert("Select API");
								$("#"+sysapiid_id_list[ii]).css('border', '1px solid red');
								return;
							} else if (env1==0) {
								alert("Select Environment");
								$("#"+envid_id_list[ii]).css('border', '1px solid red');
								return;
							}
						}
	
						for (var jj=ii+1; jj<envid_list.length; jj++) {
							var apiid2 = <%=api%>;//apiId_list[0];
							var sysapi2 = sysapiid_list[jj];
							var env2 = envid_list[jj];
							var suite2 = suiteid_list[jj];
							var bed2 = bedid_list[jj];
	
							if (apiid1==1) {
								if (/*sysapi1==sysapi2 &&*/ env1==env2 && suite1==suite2 && bed1==bed2) {
									alert("Same combination of API, Environment, Test Suite and Test Bed is not allowed for Jenkins");
									//$("#"+sysapiid_id_list[ii]).css('border', '1px solid red');
									$("#"+envid_id_list[ii]).css('border', '1px solid red');
									$("#"+suiteid_id_list[ii]).css('border', '1px solid red');
									$("#"+bedid_id_list[ii]).css('border', '1px solid red');
									//$("#"+sysapiid_id_list[jj]).css('border', '1px solid red');
									$("#"+envid_id_list[jj]).css('border', '1px solid red');
									$("#"+suiteid_id_list[jj]).css('border', '1px solid red');
									$("#"+bedid_id_list[jj]).css('border', '1px solid red');
									return;
								}
							} else {
								if (apiid1==apiid2 && env1==env2) {
									alert("Same combination of Environment is not allowed for Jira");
									$("#"+sysapiid_id_list[ii]).css('border', '1px solid red');
									$("#"+envid_id_list[ii]).css('border', '1px solid red');
									$("#"+sysapiid_id_list[jj]).css('border', '1px solid red');
									$("#"+envid_id_list[jj]).css('border', '1px solid red');
									return;
								}
							}
						}
					}
	
					// to make sure same job name for same api is not entered for jenkins
					for (var kk=0; kk<envid_list.length; kk++) {
						var sysapi3 = sysapiid_list[kk];
						var apiid3 = <%=api%>;//apiId_list[kk];
						var jobname1 = urlAlias_list[kk];
	
						for (var ll=kk+1; ll<envid_list.length; ll++) {
							var sysapi4 = sysapiid_list[ll];
							var jobname2 = urlAlias_list[ll];
	
							if (sysapi3==sysapi4 && jobname1==jobname2) {
								if (apiid3==1) {
									alert("Same Job Name for same API is not allowed for Jenkins");
									$("#"+urlAlias_id_list[kk]).css('border', '1px solid red');
									$("#"+sysapiid_id_list[kk]).css('border', '1px solid red');
									$("#"+urlAlias_id_list[ll]).css('border', '1px solid red');
									$("#"+sysapiid_id_list[ll]).css('border', '1px solid red');
									return;
								}
							}
						}
					}
					// -----
	
					// to make sure default radio button is selected for jenkins and jira
					var jenkins_data_present = false;
					var jira_data_present = false;
					var default_jenkins_flag = false;
					var default_jira_flag = false;
					for (var mm=0; mm<envid_list.length; mm++) {
						var apiid4 = <%=api%>;//apiId_list[mm];
						//alert("important api " +apiid4);
						var defaultid1 = default_list[mm];
	
						if (apiid4 == 1) {
							jenkins_data_present = true;
							if (defaultid1 == 1)
								default_jenkins_flag = true;
						} else if (apiid4 == 2) {
							jira_data_present = true;
							if (defaultid1 == 1)
								default_jira_flag = true;
						}
					}
	
					if (jenkins_data_present) {
						if (!default_jenkins_flag) {
							alert("Select default for Jenkins");
							return;
						}
					}
					if (jira_data_present) {
						if (!default_jira_flag && <%=dept.equalsIgnoreCase("qa")%>) {
							alert("Select default for Jira");
							return;
						}
					}
					// -----
				} else {
					if (<%=api%>==2 && <%=dept.equalsIgnoreCase("dev")%>) {
						var jobname = urlAlias_list[0];
						var sysapi1 = sysapiid_list[0];
						if (jobname=="") {
							alert("Enter Rapid View Id in Name field");
							$("#"+urlAlias_id_list[0]).css('border', '1px solid red');
							return;
						}  else if (sysapi1==0) {
							alert("Select API");
							$("#"+sysapiid_id_list[0]).css('border', '1px solid red');
							return;
						}
					}
				}
	
				/*var a = new Array();
				var b = new Array();
				$("input[name='radio-set']").each(function() {
					a.push($(this).val());
					b.push($('label[for="'+$(this).attr('id')+'"]').attr("name"));
				});
				$('#sys_id').val(a);
				$('#sys_name').val(b);
				
				alert($('#sys_id').val());
				alert($('#sys_name').val());*/
	
				var id = $("#applicationId").val();
				var name = $("#appName").val().trim();
				var flag = isNameExist('app', id, name);
				
				if (flag == "true")
					alert("Application name already exists");
				else {
					//
					var urlAliasElements = $('textarea[name=urlAlias]');
					//alert(urlAliasElements.length);
					$(urlAliasElements).each(function() {
						var urlAliasElement = $(this);
						//alert(urlAliasElement.val());
						var urlAliasOrg = urlAliasElement.val();
						var urlAliasReplaced = urlAliasOrg.replace(/,/g,';');
						//alert(urlAliasReplaced);
						urlAliasElement.val(urlAliasReplaced);
						
					});
					alert("before submission");
	
					$("#create_form").submit();
					//alert("after submission");
				}
			}
			else {
				if ($("#appName").val().trim() == "") {
					alert("Enter Application Name");
					return false;
				}
				
				if ($('#month_id').val() == 0) {
					alert("Select Fiscal Year Start Month");
					return false;
				}
			}
		
	});

	$('#cancelBtn').click(function () {
		window.location.href ='<%=contextPath %>/manage?paramName=app&page=&orderBy=&orderType=';
		//window.history.back();
	});

	$(document).ready(function() {
		$("#default_checkbox").change(function(){
			if(this.checked){ //Check selected or not
				$("input:checkbox").each(function(){ //Loop through each checkbox with class chk and select it
					this.checked = true; 
				})              
			} else {
				$("input:checkbox").each(function(){ //Loop through each checkbox with class chk and deselect it
					this.checked = false;
				})              
			}
		});
 
		$("input:checkbox").click(function () { //On click at any checkbox with chk class
			if (!$(this).is(":checked")){ //If particular one is unchecked then uncheck the Select All
				$("#default_checkbox").prop("checked", false);
			} else {
				var flag = 1; //Setting flag to 1 while assuming all checkboxes with class chk are selected
				$("input:checkbox").each(function(){
					if(!this.checked)
						flag = 0; //If particular or more are unchecked then set flag to 0
				})              
				if(flag) { // All checkboxes are selected so also select Select All checkbox
					$("#default_checkbox").prop("checked", true);
				}
			}
       });
		if ('${appDetails}' && '${appDetails.appName}') {
			<%
				if (appDetails != null && app_name != null) {
					try {
					//ArrayList sysIdList = (ArrayList)((Application) appDetails).getSysIdList();
					ArrayList tabIdList = (ArrayList)((Application) appDetails).getTabIdList();
					ArrayList mapList = (ArrayList)((Application) appDetails).getMapIdList();
					ArrayList envIdList = (ArrayList)((Application) appDetails).getEnvIdList();
					ArrayList suiteIdList = (ArrayList)((Application) appDetails).getSuiteIdList();
					ArrayList bedIdList = (ArrayList)((Application) appDetails).getBedIdList();
					ArrayList isDefaultList = (ArrayList)((Application) appDetails).getIsDefaultList();
					ArrayList apiList = (ArrayList)((Application) appDetails).getApiIdList();
					for (int k=0; k<envIdList.size(); k++) {
						int apiid =Integer.parseInt(apiList.get(k).toString());
      					 int sysid =Integer.parseInt(apiList.get(k).toString());
      					String jenkinsId = sysid+""+k;
      					
						//String str = sysIdList.get(k).toString();
						String sysapiStr = tabIdList.get(k).toString();
						String envStr = envIdList.get(k).toString();
						String suiteStr = suiteIdList.get(k).toString();
						String bedStr = bedIdList.get(k).toString();
			%>
					<c:forEach items="${sys_list}" var="sysapi_list">
					    if ('${sysapi_list.sysId}' == <%=sysapiStr%>) {
					    <% if (apiid==1){%>
					    	$('#selected_sysapiId'+<%=jenkinsId%>).text('${sysapi_list.sysName}').css('color', '#575757');
					  <% } else {%>
					  $('#selected_sysapiId'+<%=mapList.get(k)%>).text('${sysapi_list.sysName}').css('color', '#575757');
					 <% }%>
						}
					</c:forEach>
					<c:forEach items="${_env_list}" var="env_list">
					    if ('${env_list.envId}' == <%=envStr%>) {
					    <% if (apiid ==1){%>
					    	$('#selected_envId'+<%=jenkinsId%>).text('${env_list.envName}').css('color', '#575757');
					  <% } else {%>
					  $('#selected_envId'+<%=mapList.get(k)%>).text('${env_list.envName}').css('color', '#575757');
					 <% }%>
						}
					</c:forEach>

					<c:forEach items="${_suite_list}" var="suite_list">
			    		if ('${suite_list.suiteId}' == <%=suiteStr%>) {
			    			<% if (apiid ==1){%>
			    			$('#selected_suiteId'+<%=jenkinsId%>).text('${suite_list.suiteName}').css('color', '#575757');
					       <% } else {%>
					        $('#selected_suiteId'+<%=mapList.get(k)%>).text('${suite_list.suiteName}').css('color', '#575757');
					       <% }%>
						}
					</c:forEach>

					<c:forEach items="${_bed_list}" var="bed_list">
					    if ('${bed_list.bedId}' == <%=bedStr%>) {
					    	<% if (apiid ==1){%>
					    	$('#selected_bedId'+<%=jenkinsId%>).text('${bed_list.bedName}').css('color', '#575757');
					       <% } else {%>
					       $('#selected_bedId'+<%=jenkinsId%>).text('${bed_list.bedName}').css('color', '#575757');
					       <% }%>
						}
					</c:forEach>
			<%
					}
					} catch (Exception e) {}
				}
			%>

			$('#month_list li').each(function() {
				if ($(this).val() == '${appDetails.monthId}') {
					$('#selected_monthId').text($(this).text()).css('color', '#575757');
				}
			});
		}

	    $('.removeRow').live("click",  function() {
			$(this).parent().parent().remove();
		});

	    dropdown();

		// js for tab navigation
		/*$('[id^=jenkins_div]').first().removeClass("tab-hide");
		$('[id^=jenkins_div]').first().addClass("current");
		$(".tab-name:eq(0)").addClass('active-tab');
		$('[id^=tab_name]').on('click', function() {
			$(".tab-name").removeClass('active-tab');
			$(this).addClass('active-tab');
			
			$(".content").removeClass('current');
			$(".content").addClass('tab-hide');
			$("#jenkins_div_"+$(this).attr("value")).removeClass('tab-hide');
			$("#jenkins_div_"+$(this).attr("value")).addClass('current');
		});*/
		
		/* New UI changes */
		/*$('[id^=api-]').first().addClass("active-2");
		$('[id^=dept-]').first().addClass("active-2");*/
		/* */
		
		hideShowDelete();
    });

	var i = 1000;
	function addRow(apiId, sysId) {
		
		var selectedApiName = $('#api li.active-2').text();
        var selectedDeptName= $('#dept li.active-2').text();
        
    
       
	   if(selectedApiName.trim() == 'Jenkins'  && selectedDeptName.trim() == "Dev"){
	    	alert("Can not add row for jenkins and dev combination"); 
	    	return false;
	    }
   
		var div = document.createElement('tr');
		div.className = 'system-dtls';
		//div.innerHTML = '<td class="checkbox-cnt"><input type="checkbox" id="appId-2"><label for="appId-2"></label></td><td class="radio-td"><input id="default_rdd" type="radio" name="default" value="0" /><label id="default_lbb" for="default_rdd"><span><span></span></span></label></td><td class="name"><input type="textarea" id="urlAlias" name="urlAlias" class="api-table-cell" value="" /><input type="hidden" id="mapId" name="mapId" value="0" /></td>\
		div.innerHTML = '<td class="checkbox-cnt"><input id="checkbox_d" class="checkApp" type="checkbox" value=""><label id="checkbox_lbd" for="checkbox_d"></label></td><td class="radio-td"><input id="default_rdd" type="radio" name="default" value="0" /><label id="default_lbb" for="default_rdd"><span><span></span></span></label></td><td class="name"><textarea id="urlAlias" name="urlAlias" style="width:190px; height:20px;" value="" ></textarea><input type="hidden" id="mapId" name="mapId" value="0" /></td>\
				<td>\
					<div class="api-table-cell input-field drpdwn-container dropdown-menu" id="sysapi_ddd">\
						<div class="name-drpdwn" id="selected_sysapiIdd" sysapiid="0">Select</div>\
						<ul class="dropdown" id="sysapi_listd">\
							<c:forEach var="sysapi" items="${sys_list}">\
								<li id="sysapiId-${sysapi.sysId}" value="${sysapi.sysId}">${sysapi.sysName}</li>\
							</c:forEach>\
						</ul>\
					</div>\
					<div class="clear"></div>\
				</td>\
				<td>\
					<div class="api-table-cell input-field drpdwn-container dropdown-menu" id="env_ddd">\
						<div class="name-drpdwn" id="selected_envIdd" envid="0">Select</div>\
						<ul class="dropdown" id="env_listd">\
							<c:forEach var="env" items="${env_list}">\
								<li id="envId-${env.envId}" value="${env.envId}">${env.envName}</li>\
							</c:forEach>\
						</ul>\
					</div>\
					<div class="clear"></div>\
				</td>\
				<td>\
					<div class="api-table-cell input-field drpdwn-container dropdown-menu" id="suite_ddd">\
						<div class="name-drpdwn" id="selected_suiteIdd" suiteid="0">Select</div>\
						<ul class="dropdown" id="suite_listd">\
							<c:forEach var="suite" items="${suite_list}">\
								<li id="suiteId-${suite.suiteId}" value="${suite.suiteId}">${suite.suiteName}</li>\
							</c:forEach>\
						</ul>\
					</div>\
					<div class="clear"></div>\
				</td>\
				<td>\
					<div class="api-table-cell input-field drpdwn-container dropdown-menu" id="bed_ddd">\
						<div class="name-drpdwn" id="selected_bedIdd" bedid="0">Select</div>\
						<ul class="dropdown" id="bed_listd">\
							<c:forEach var="bed" items="${bed_list}">\
								<li id="bedId-${bed.bedId}" value="${bed.bedId}">${bed.bedName}</li>\
							</c:forEach>\
						</ul>\
					</div>\
					<div class="clear"></div>\
				</td>\
				<!--td><span class="removeRow" title="Delete Row"></span></td-->';

		document.getElementById('system-dtls-tbl').appendChild(div);

		$('#urlAlias').attr('id', 'urlAlias'+i);

		$('#sysapi_ddd').css('z-index', 11000-i);
		$('#sysapi_ddd').attr('id', 'sysapi_dd'+i);
		$('#sysapi_listd').attr('id', 'sysapi_list'+i);
		$('#selected_sysapiIdd').attr('id', 'selected_sysapiId'+i);

		if (apiId==2 && <%=dept.equalsIgnoreCase("dev")%>) {
			$('.radio-td').addClass('td-hide');
			$('#env_ddd').removeClass('dropdown-menu');
			$('#env_ddd').addClass('dropdown-menu-disable');
			$('#env_ddd').parent().addClass('td-hide');
			$('#suite_ddd').removeClass('dropdown-menu');
			$('#suite_ddd').addClass('dropdown-menu-disable');
			$('#suite_ddd').parent().addClass('td-hide');
			$('#bed_ddd').removeClass('dropdown-menu');
			$('#bed_ddd').addClass('dropdown-menu-disable');
			$('#bed_ddd').parent().addClass('td-hide');
		} else if (apiId==2) {
			$('#suite_ddd').removeClass('dropdown-menu');
			$('#suite_ddd').addClass('dropdown-menu-disable');
			$('#suite_ddd').parent().addClass('td-hide');
			$('#bed_ddd').removeClass('dropdown-menu');
			$('#bed_ddd').addClass('dropdown-menu-disable');
			$('#bed_ddd').parent().addClass('td-hide');
		}

		$('#env_ddd').css('z-index', 10000-i);
		$('#env_ddd').attr('id', 'env_dd'+i);
		$('#env_listd').attr('id', 'env_list'+i);
		$('#selected_envIdd').attr('id', 'selected_envId'+i);
		$('#suite_ddd').css('z-index', 9000-i);
	 	$('#suite_ddd').attr('id', 'suite_dd'+i);
	 	$('#suite_listd').attr('id', 'suite_list'+i);
	 	$('#selected_suiteIdd').attr('id', 'selected_suiteId'+i);
	 	$('#bed_ddd').css('z-index', 8000-i);
	 	$('#bed_ddd').attr('id', 'bed_dd'+i);
	 	$('#bed_listd').attr('id', 'bed_list'+i);
	 	$('#selected_bedIdd').attr('id', 'selected_bedId'+i);
	 	$('#default_rdd').attr('name', 'default'+apiId);
	 	$('#default_rdd').attr('id', 'default_rd'+i);
	 	$('#default_lbb').attr('for', 'default_rd'+i);
		$('#default_lbb').attr('id', 'default_lbb'+i);
	
	 	$('#checkbox_d').attr('id', 'checkbox_'+i);
	 	$('#checkbox_lbd').attr('for', 'checkbox_'+i);
		$('#checkbox_lbd').attr('id', 'checkbox_lb'+i);

		if ('${appDetails}' && '${appDetails.appName}') {
		} else {
			$("[name=default"+apiId+"]").val(0);
			$("[name=default"+apiId+"]").attr('checked',false);
			$('#default_rd'+i).attr('checked',true);
			$('#default_rd'+i).val(1);
		}

		/*$('#sys_list'+i).on('click', 'li', function() {
			$(this).parent().siblings("[id^=selected_sysId]").text($(this).text()).css('color', '#575757');
			if ($(this).text().toLowerCase().indexOf("ji") >= 0) {
				$('#url_alias'+$(this).parent().attr('Id').substring(8)).hide();
			} else {
				$('#url_alias'+$(this).parent().attr('Id').substring(8)).show();
			}
			//$('#sys_id').val($('#sys_id').val()+','+$(this).val());
			//$('#sys_name').val($('#sys_name').val()+','+$(this).text());
			$(this).parent().siblings("[id^=selected_sysId]").attr("sysid", $(this).val());*/
			/*$(this).parent().siblings("[id^=selected_sysId]").attr("sysname", $(this).text());
		});*/

		$('#urlAlias'+i).on('click', function() {
			$('[id^=urlAlias]').css('border', '');
		});
		$('#sysapi_list'+i).on('click', 'li', function(event) {
			$(this).parent().siblings("[id^=selected_sysapiId]").text($(this).text()).css('color', '#575757');
			$(this).parent().siblings("[id^=selected_sysapiId]").attr("sysapiid", $(this).val());
			$(this).parent().parent().removeClass("active");
			$('.dropdown-menu').css('border','');
			event.stopPropagation();
		});
		$('#env_list'+i).on('click', 'li', function(event) {
			$(this).parent().siblings("[id^=selected_envId]").text($(this).text()).css('color', '#575757');
			$(this).parent().siblings("[id^=selected_envId]").attr("envid", $(this).val());
			$(this).parent().parent().removeClass("active");
			$('.dropdown-menu').css('border','');
			event.stopPropagation();
		});
		$('#suite_list'+i).on('click', 'li', function(event) {
			$(this).parent().siblings("[id^=selected_suiteId]").text($(this).text()).css('color', '#575757');
			$(this).parent().siblings("[id^=selected_suiteId]").attr("suiteid", $(this).val());
			$(this).parent().parent().removeClass("active");
			$('.dropdown-menu').css('border','');
			event.stopPropagation();
		});
		$('#bed_list'+i).on('click', 'li', function(event) {
			$(this).parent().siblings("[id^=selected_bedId]").text($(this).text()).css('color', '#575757');
			$(this).parent().siblings("[id^=selected_bedId]").attr("bedid", $(this).val());
			$(this).parent().parent().removeClass("active");
			$('.dropdown-menu').css('border','');
			event.stopPropagation();
		});
		$('#default_rd'+i).on('click', function() {
			$("[name=default"+apiId+"]").val(0);
			$(this).val(1);
		});

		dropdownAutoSize();
		i++;
		dropdown();
		hideShowDelete();
	}

	/*$('.checkApp').click(function () {
		if ($('label[for="'+$(this).attr('id')+'"]').attr('name').toLowerCase().indexOf("je")>=0) {
			$('#jenkins_div').toggle();
		}
	});*/

	function dropdown() {
		function DropDown(el) {
			this.sys_dd = el;
			this.initEvents();
		}

		/*DropDown.prototype = {
			initEvents : function() {
				var obj = this;
				obj.sys_dd.on('click', function(event) {
				$(this).addClass('active');
					event.stopPropagation();
				});
			}
		};*/

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
			$('[id^=sysapi_list]').css('height','195px');
			$('[id^=sysapi_list]').css('overflow','auto');
		} else {
			$('[id^=sysapi_list]').css('height','auto');
			$('[id^=sysapi_list]').css('overflow','hidden');
		}

		if ('${fn:length(env_list)}' > 5) {
			$('[id^=env_list]').css('height','195px');
			$('[id^=env_list]').css('overflow','auto');
		} else {
			$('[id^=env_list]').css('height','auto');
			$('[id^=env_list]').css('overflow','hidden');
		}

		if ('${fn:length(suite_list)}' > 5) {
			$('[id^=suite_list]').css('height','195px');
			$('[id^=suite_list]').css('overflow','auto');
		} else {
			$('[id^=suite_list]').css('height','auto');
			$('[id^=suite_list]').css('overflow','hidden');
		}

		if ('${fn:length(bed_list)}' > 5) {
			$('[id^=bed_list]').css('height','195px');
			$('[id^=bed_list]').css('overflow','auto');
		} else {
			$('[id^=bed_list]').css('height','auto');
			$('[id^=bed_list]').css('overflow','hidden');
		}
	}

	function hideShowDelete() {
		 var selectedApiName = $('#api li.active-2').text();
	     var selectedDeptName= $('#dept li.active-2').text();

		  if(selectedApiName.trim() == 'Jira'  && selectedDeptName.trim() == "Dev"){
		  
		   if ($("tr").hasClass("system-dtls"))
		    $(".add-record").hide();
		   else
		    $(".add-record").show(); 
		   }
	
		  if ($("tr").hasClass("system-dtls"))
		   $(".delete-record").show();
		  else
		   $(".delete-record").hide();
	  
		if ($("tr").hasClass("system-dtls"))
			$(".delete-record").show();
		else
			$(".delete-record").hide();
	}
</script>
