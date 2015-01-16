<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Google Logout</title>
		<style type="text/css" media="screen">
			#page-body {
				margin: 1em 1em 1em 1em;
			}

			p {
				line-height: 1.5;
				margin: 0.25em 0;
			}
		</style>
	</head>
	<body>
		<div id="page-body" role="main">
			<h1>Wrong Account</h1>
			<p>You tried to use email address: (${emails.join(',')}) to 
			login, but you must use your school account from <b>${gplusDomain}</b>.  
			<p>Please <g:link action="index">try again</g:link> with your school account </p>
		</div>
	</body>
</html>
