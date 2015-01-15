<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Google+ Signin Demo</title>
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
			<h1>Google+ Signin Demo</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			
			<ul>
			<li><g:link controller="login">Login</g:link></li>
			<li><a href="https://accounts.google.com/logout">Logout</a>
			</ul>
		</div>
	</body>
</html>
