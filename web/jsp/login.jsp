<%@page import="com.iceapp.x.ShiroUtil"%>

<html>
<html>
<head>
<title>Login</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Add some nice styling and functionality.  We'll just use Twitter Bootstrap -->
<link rel="stylesheet" href="../css/bootstrap.css">
<link rel="stylesheet" href="../css/bootstrap-theme.css">
<style>
body {
	padding-top: 20px;
}
</style>
</head>
<body class="body-image">

	<div class="container">

		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<h2></h2>
				<div class="panel panel-default">

					<div class="panel-heading">
						<h3 class="panel-title">Please log in</h3>
					</div>
					<div class="panel-body">
						<form name="loginform" action="" method="POST"
							accept-charset="UTF-8" role="form">
							<fieldset>
								<div class="form-group">
									<input class="form-control" placeholder="Username"
										name="username" type="text">
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="Password"
										name="password" type="password" value="">
								</div>
								<div class="checkbox">
									<label> <input name="rememberMe" type="checkbox"
										value="true"> Remember me
									</label>
								</div>
								<input class="btn btn-lg btn-primary btn-block" type="submit"
									value="Login">
							</fieldset>
						</form>
					</div>
				</div>
			</div>

			<%
				String errorMsg = null;
				String invalid = request.getParameter("invalid");
				if ("1".equals(invalid)) {
					errorMsg = "Invalid access";
				} else if (ShiroUtil.hasAdminRole()) {
					errorMsg = "Already logged in, Go to <a href=\"home.jsp\">Home</a>";
			%>
			<div class="col-md-4 col-md-offset-4">
				<div id="errorMessageDialog" class="alert alert-danger" role="alert">
					<center><%=errorMsg%></center>
				</div>
			</div>
			<%
				}
			%>

		</div>
	</div>
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="../js/jquery.js"></script>
	<script src="../js/bootstrap.min.js"></script>
</body>
</html>
