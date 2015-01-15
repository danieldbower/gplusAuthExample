<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Login</title>
		<style type="text/css" media="screen">
			#page-body {
				margin: 1em 1em 1em 1em;
			}

			h2 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				font-size: 1em;
			}

			p {
				line-height: 1.5;
				margin: 0.25em 0;
			}

			#controller-list ul {
				list-style-position: inside;
			}

			#controller-list li {
				line-height: 1.3;
				list-style-position: inside;
				margin: 0.25em 0;
			}

		</style>
		<script src="https://apis.google.com/js/client:platform.js" async defer></script>
		
		<script>
			function signinCallback(authResult) {
				if (authResult['status']['signed_in']) {
					// Update the app to reflect a signed in user
					// Hide the sign-in button now that the user is authorized, for example:
					document.getElementById('signinButton').setAttribute('style', 'display: none');

					document.getElementById('accessTokenInput').value = authResult['access_token']
					document.getElementById('idTokenInput').value = authResult['id_token']
					
					console.log(authResult)

					document.getElementById("authForm").submit();
				} else {
					// Update the app to reflect a signed out user
					// Possible error values:
					//   "user_signed_out" - User is signed-out
					//   "access_denied" - User denied access to your app
					//   "immediate_failed" - Could not automatically log in the user
					console.log('Sign-in state: ' + authResult['error']);
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
				<g:field type="hidden" name="idTokenInput"/>
			</g:form>
		</div>
	</body>
</html>
