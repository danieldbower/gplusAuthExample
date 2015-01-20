<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Google+ Signin Demo</title>
		<style type="text/css" media="screen">
			#page-body {
				margin: 1em 1em 1em 1em;
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
				<sec:ifLoggedIn>
					<li><g:link controller="logout">Log out <sec:loggedInUserInfo field="username"/></g:link></li>
				</sec:ifLoggedIn>
				<sec:ifNotLoggedIn>
					<li><g:link controller="login">Log in</g:link></li>
				</sec:ifNotLoggedIn>
				<li><g:form controller="user" action="create" useToken="true" method="POST">
					<g:field type="text" name="email" />
					<g:submitButton name="submit" value="Add User with Email" />
				</g:form></li>
			</ul>
		</div>
	</body>
</html>
