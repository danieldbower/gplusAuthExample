<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Google Logout</title>
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

		</style>
		<script src="https://apis.google.com/js/client:platform.js" async defer></script>
		<script>
		function disconnectUser(access_token) {
			  var revokeUrl = 'https://accounts.google.com/o/oauth2/revoke?token=' +
			      access_token;

			  // Perform an asynchronous GET request.
			  $.ajax({
			    type: 'GET',
			    url: revokeUrl,
			    async: false,
			    contentType: "application/json",
			    dataType: 'jsonp',
			    success: function(nullResponse) {
			      // Do something now that user is disconnected
			      // The response is always undefined.
					console.log('Disconnected user')
			    },
			    error: function(e) {
			      // Handle the error
			      // console.log(e);
			      // You could point users to manually disconnect if unsuccessful
			      // https://plus.google.com/apps
			    }
			  });
		}
		</script>
	</head>
	<body>
		<div id="page-body" role="main">
			<h1>Logout of Google</h1>
			<p>You tried to use an email (${emails.join(',')}) from ${domain} to 
			login, but you must use your school account from <b>${gplusDomain}</b>.  
			You may need to <a href="https://accounts.google.com/AccountChooser?continue=http://google.com" target="_blank">switch accounts.</a></p>
			<p>After doing so, you should be able to come back and <g:link action="index">sign in</g:link></p>
			<button onclick="disconnectUser('${accessToken}');" value="Disconnect">Disconnect</button>
			<button onclick="console.log(gapi.auth.signOut());" value="Sign Out">Sign Out</button>
		</div>
	</body>
</html>
