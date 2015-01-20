<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Login</title>
		<style type="text/css" media="screen">
			#login {
				margin: 15px 0px;
				padding: 0px;
				text-align: center;
			}
			#login .inner {
				width: 340px;
				padding-bottom: 6px;
				margin: 60px auto;
				text-align: left;
				border: 1px solid #aab;
				background-color: #f0f0fa;
				-moz-box-shadow: 2px 2px 2px #eee;
				-webkit-box-shadow: 2px 2px 2px #eee;
				-khtml-box-shadow: 2px 2px 2px #eee;
				box-shadow: 2px 2px 2px #eee;
			}
			#login .inner .fheader {
				padding: 18px 26px 14px 26px;
				background-color: #f7f7ff;
				margin: 0px 0 14px 0;
				color: #2e3741;
				font-size: 18px;
				font-weight: bold;
			}
			#login .inner .cssform p {
				clear: left;
				margin: 0;
				padding: 4px 0 3px 0;
				padding-left: 105px;
				margin-bottom: 20px;
				height: 1%;
			}
			#login .inner .cssform input[type='text'] {
				width: 120px;
			}
			#login .inner .cssform label {
				font-weight: bold;
				float: left;
				text-align: right;
				margin-left: -105px;
				width: 110px;
				padding-top: 3px;
				padding-right: 10px;
			}
			#login #remember_me_holder {
				padding-left: 120px;
			}
			#login #submit {
				margin-left: 15px;
			}
			#login .inner .login_message {
				padding: 6px 25px 20px 25px;
				color: #c33;
			}
			#login .inner .text_ {
				width: 120px;
			}
			#login .inner .chk {
				height: 12px;
			}
		</style>

		<script src="https://apis.google.com/js/client:platform.js" async defer></script>
		
		<script>
			function signinCallback(authResult) {
				<%--console.log(authResult);--%>

				if (authResult['status']['signed_in']) {
					document.getElementById('signinButton').setAttribute('style', 'display: none');

					document.getElementById('codeInput').value = authResult['code'];
					document.getElementById("loginForm").submit();
				} else {
					console.log('SigninCallback returned false signed_in status: ' + authResult['error']);
				}
			}
		</script>
	</head>
	<body>
		<div id='login'>
			<div class='inner'>
				<div class='fheader'><g:message code="springSecurity.login.header"/></div>
	
				<g:if test="${flash.message}">
					<div class="login_message">${flash.message}</div>
				</g:if>
				
					<span id="signinButton">
						<span
							class="g-signin"
							data-callback="signinCallback"
							data-clientid="${clientId}"
							data-redirecturi="postmessage"
							data-cookiepolicy="single_host_origin"
							data-scope="email">
						</span>
					</span>
				<g:form action="gplus" method="POST" name="loginForm" class="cssform" autocomplete='off' useToken="true">
					<g:field type="hidden" name="codeInput"/>
				</g:form>
			</div>
		</div>
	</body>
</html>
