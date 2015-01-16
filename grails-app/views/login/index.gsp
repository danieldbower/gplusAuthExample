<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Login</title>
		<style type="text/css" media="screen">
			#page-body {
				margin: 1em 1em 1em 1em;
			}
		</style>

		<script src="https://apis.google.com/js/client:platform.js" async defer></script>
		
		<script>
			function signinCallback(authResult) {
				<%--console.log(authResult);--%>

				if (authResult['status']['signed_in']) {
					document.getElementById('signinButton').setAttribute('style', 'display: none');

					document.getElementById('accessTokenInput').value = authResult['access_token'];
					document.getElementById("authForm").submit();
				} else {
					console.log('SigninCallback returned false signed_in status: ' + authResult['error']);
				}
			}
		</script>
	</head>
	<body>
		<div id="page-body" role="main">
			<h1>Login</h1>
			<span id="signinButton">
				<span
					class="g-signin"
					data-callback="signinCallback"
					data-clientid="${clientId}"
					data-cookiepolicy="single_host_origin"
					data-scope="email">
				</span>
			</span>
			<g:form action="gplus" method="POST" name="authForm">
				<g:field type="hidden" name="accessTokenInput"/>
			</g:form>
		</div>
	</body>
</html>
