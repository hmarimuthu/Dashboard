<META HTTP-EQUIV="refresh" CONTENT="<%= session.getMaxInactiveInterval() %>; URL=/eTAAP-Dashboard/logout" />
<% String getContextPath = request.getContextPath(); %>
<script type="text/javascript">	        		
	var contextPath = '<%=getContextPath%>';
</script>
<div class="logo-container">
	<img alt="eTAAP Logo" title="eTAAP" class="logo" src='<c:url value="/img/eTAAP-logo.png" />' onclick="window.location.href ='<%=getContextPath %>/';return;"/>
<!-- logo container ends here -->
</div>

<!-- project list container starts here -->
<div id="prjctDd" class="project-list-container">
	<c:choose>
		<c:when test="${empty requestedApp}">
			<h1 id="selected-project" class="selected-project">Dashboard</h1>
		</c:when>
		<c:otherwise>
			<h1 id="selected-project" class="selected-project">${requestedApp}</h1>
		</c:otherwise>
	</c:choose>
	<c:if test="${sessionScope.userSession != null}" >
	<div class="prjct-drpdwn-arrow"></div>
	<ul id="app" class="project-list">
		<c:forEach var="app" items="${appList}">
			<li id="appId-${app.appId}" value="${app.appId}" data-priority="${app.priority}" onclick="changeDashboardApp('changeApp',this);">${app.appName}</li>
		</c:forEach>
	</ul>
	</c:if>
	<input type="hidden" id="appId" name="appId" value="">
	<input type="hidden" id="lastAppId" name="lastAppId" value="">
<!-- project list container ends here -->
</div>

<!-- nav admin section starts here -->
<div class="nav-admin-container">
	<!-- top nav starts here -->
	<nav>
		<!--<ul>
			<li id="defects" class="deactive-link"><a href="<%=getContextPath%>/defects">Defects</a></li>
           	<li id="ci" class="active"><a href="<%=getContextPath%>/home">CI Results</a></li>
           	<li id="tcm" class="deactive-link" ><a href="<%=getContextPath%>/tcm">TCM</a></li>
       	</ul>-->
		<ul class="main-nav">
			<li class="deactive-link"><a href="#">Quality</a>
				<ul class="subnav">
					<li><a href="<%=getContextPath%>/defects-redesign">Defects</a></li>
					<li><a href="<%=getContextPath%>/home-redesign">CI Results</a></li>
					<li class="zero-border"><a href="<%=getContextPath%>/tcm">TCM</a></li>
					<div class="clear"></div>
				</ul>
			</li>
			<li class="deactive-link"><a href="#">Dev</a>
				<ul class="subnav">
					<li><a href="<%=getContextPath%>/iterations-redesign">Iterations</a></li>
					<li><a href="<%=getContextPath%>/velocity">Velocity</a></li>
					<div class="clear"></div>
				</ul>
			</li>
			<!-- <li class="deactive-link"><a href="#">Operations</a>
				<ul class="subnav">
					<li><a href="#">Link 1</a></li>
					<li><a href="#">Link 2</a></li>
					<li class="zero-border"><a href="#">Link 3</a></li>
					<div class="clear"></div>
				</ul>
			</li> -->
			<div class="clear"></div>
		</ul>
		<!-- top nav ends here -->
	<div class="clear"></div>
    </nav>
<c:choose>
	<c:when test="${sessionScope.userSession != null && sessionScope.userSession.fname != \"\"}">
	
		<!-- Logout logic -->
		<!-- user container starts here -->                   
		<div class="user-container">
			<div class="login-details">
				<div class="user-details">
					Signed in as
					<p class="user-name">${sessionScope.userSession.fname} ${sessionScope.userSession.lname}</p>
			</div>
			<div class="log-out-details" onclick="logout();">Log out
				<a href="#" class="log-out-icon"></a>
			</div>
			<div class="clear"></div>
		</div>
		<div class="user-icon"></div>
		<div id="settings" class="settings">
			<div class="settings-details"></div>
			<div class="settings-container">
				<div id="pop-up" class="pop-up">
					<ul class="setting-list">
						<li><a href="<%=getContextPath %>/manage?paramName=env&page=&orderBy=&orderType=">Manage Environment</a></li>
						<li><a href="<%=getContextPath %>/manage?paramName=suite&page=&orderBy=&orderType=">Manage Test Suite</a></li>
						<li><a href="<%=getContextPath %>/manage?paramName=bed&page=&orderBy=&orderType=">Manage Test Bed</a></li>
						<li><a href="<%=getContextPath %>/manage?paramName=sys&page=&orderBy=&orderType=">Manage API</a></li>
					</ul>
					<div class="clear"></div>
				</div>
				<ul class="setting-list">
					<!--li><a href="create?paramName=">Create Application</a></li-->
					<li><a href="<%=getContextPath %>/manage?paramName=app&page=&orderBy=&orderType=">Manage Application</a></li>
					<li><a href="<%=getContextPath %>/manage?paramName=tc&page=&orderBy=&orderType=">Manage Test Case</a></li>
					<li><a href="<%=getContextPath %>/scheduler">Manage Scheduler</a></li>
					<li id="other-settings"><a href="<%=getContextPath %>/manage?paramName=env&page=&orderBy=&orderType=">Other Settings</a></li>
				</ul>
				<div class="clear"></div>
			</div>
			<div class="clear"></div>
		</div>
		<!-- user container ends here -->                    
		<div class="clear"></div>               
		<!-- nav admin section ends here -->
		</div>
		
	</c:when>
	<c:otherwise>
		<!-- Login logic -->
		<div class="user-container">
		<div class="login-details" style="width: 75px !important">
			<div class="log-out-details" onclick="window.location.href ='<%=getContextPath %>/login';return;">Log In
				<a href="#" class="log-in-icon"></a>
			</div>
			<div class="clear"></div>
		</div>	
		</div>
	</c:otherwise>
</c:choose>

<div class="clear"></div>
<script type="text/javascript">
	function logout(){
		 var logout_form = document.createElement("form");
		 logout_form.action = "<%=getContextPath %>/logout";
		 document.body.appendChild(logout_form);
		 logout_form.submit();
		 document.body.removeChild(logout_form);
	}

	$(document).ready(function() {
		if($('.log-out-details').text().trim() == "Log In")
			$('#prjctDd').css('cursor','default');
	});
</script>