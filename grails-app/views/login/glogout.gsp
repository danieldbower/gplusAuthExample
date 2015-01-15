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
	</head>
	<body>
		<div id="page-body" role="main">
			<h1>Logout of Google</h1>
			<p>You tried to use an email (${emails.join(',')}) from ${domain} to 
			login, but you must use your school account from <b>${gplusDomain}</b>.  
			You may need to <a href="https://accounts.google.com/AccountChooser?continue=http://google.com" target="_blank">switch accounts.</a></p>
			<p>After doing so, you should be able to come back and <g:link action="index">sign in</g:link></p>
		</div>
	</body>
</html>
