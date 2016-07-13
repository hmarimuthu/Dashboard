<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta charset="utf-8">
<title>eTAAP Dashboard - Login</title>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<link rel="stylesheet" href="css/design.css" type="text/css" />
	<link type="text/css" rel="stylesheet" href='<c:url value="/fonts/open-sans/stylesheet.css" />' />
	<link rel="stylesheet" href="css/etaap.css" type="text/css" />
	<!--link rel="stylesheet" href="css/etaap-old.css" type="text/css" /-->
<style type="text/css">
	.label-error{
	 color: red;
	 position: absolute;
	 width: 310px;
	 text-align: center;
	 margin-top: -20px;
	 padding-left:5px;
	}
	/* Login Style */
.login-form{
 background:#f3f3f3;
 border: 1px solid #fCfCfC;
 border-radius: 10px;
 padding: 30px;
 margin: 70px auto 100px;
 width: 310px;
}
.login-cnt{
 padding: 5px 0;
}
.label-cnt{
 float: left;
 line-height: 30px;
 width: 100px;
}
.login-form .btn-container {
 margin-left: 100px;
 padding: 5px 0;
}
.login-form .btn-container .btn-red, .login-form .btn-container .btn-blue {
 padding: 5px 16px 3px;
}
.login-form p{padding: 10px 0 0 100px;}

.dashboard-bg{
 background: url(../images/dashboard-bg.png);
 padding-bottom: 20px;
}
.db-box{
 background: #ffffff;
 border: 1px solid #d6d6d6;
 box-shadow: 0px 4px 5px -2px #d6d6d6;
 float: left;
 height: 220px;
 border-radius: 5px;
 width: 328px;margin-left: 100px;
 margin: 35px 22px 0 0;
}
.db-box-1{margin-left: 50px;}
.db-box .db-box-title{
 color: #666666;
 font-size: 14px;
 text-align: center;
 margin-top: 6px;
}
</style>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
</head>

<body>

<div class="main-content-container">
	<div class="dashboard-container">
		<header>
			<div class="logo-container"><% String getContextPath = request.getContextPath(); %>
				<img alt="eTAAP Logo" title="eTAAP" class="logo" src='<c:url value="/img/eTAAP-logo.png" />'  onclick="window.location.href ='<%=getContextPath %>/';return;"/>
				<!-- logo container ends here -->
			</div>
			<div class="clear"></div>
		</header>
		<div class="clear"></div>

		<div class="content-container">
			
			<div class="right-container" style="width:100%">
				<h1>Login</h1>
				<spring:url value="/loginAuth" var="loginAuth"></spring:url>
				<form id="login_form" action="loginAuth" method="post">
					<div class="form-container section-container">	
						<div class="login-form">
						<c:if test="${not empty exception}">
									<div class="label-error">Invalid Credentials</div>
							</c:if>
							<div class="login-cnt">
								<div class="label-cnt">User Name</div>
								<div class="input-field"><input type="text" name="userName" value="" style="width:190px;" /></div>
							</div>
							<div class="login-cnt">
								<div class="label-cnt">Password</div>
								<div class="input-field"><input type="password" name="password" value="" style="width:190px;" /></div>
							</div>
							<div class="login-cnt">
								<div class="btn-container">
									<input type="submit" id="createBtn" class="btn-blue" value="SUBMIT"><input type="button" id="cancelBtn" class="btn-red" value="CANCEL" onclick="window.location.href ='<%=getContextPath %>/';return;">
								</div>
							</div>
							<!-- <p><a href="javascript:;">Forgot password?</a> --> 
						</div>
						<div class="clear"></div>
					</div>
				</form>
			</div>
		</div>

		<!-- footer starts here -->
		<footer>
			<%@ include file="./footer.jsp" %>
        </footer>	
	    <!-- footer ends here -->
	</div>
</div>
</body>
</html>