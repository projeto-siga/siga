<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Welcome to JBoss Application Server 7</title>
</head>

<body>
	<div class="loginBox"
		style="margin-bottom: 80px; border: 1px solid #000000; width: 310px; background-color: #F8F8F8; align: center;">
		<form id="login_form" name="login_form" method="post"
			action="j_security_check" enctype="application/x-www-form-urlencoded">
			<center>
				<p>
					Welcome to the <b>Siga Identity Provider</b>
				</p>
				<p style="color: red">Login failed, please try again.</p>
			</center>

			<div style="margin-left: 15px;">
				<p>
					<label for="username"> Username</label><br /> <input id="username"
						type="text" name="j_username" size="20" />
				</p>
				<p>
					<label for="password"> Password</label><br /> <input id="password"
						type="password" name="j_password" value="" size="20" />
				</p>
				<center>
					<input id="submit" type="submit" name="submit" value="Login"
						class="buttonmed" />
				</center>
			</div>
		</form>
	</div>
</body>
</html>