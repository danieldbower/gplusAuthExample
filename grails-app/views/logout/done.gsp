<html>
<head>
	<meta name='layout' content='main' />
	<title>Logout</title>
</head>
<body>
	<h2>
		<g:if test='${flash.message}'>
			<span>${flash.message}</span>
		</g:if>
		<g:else>
			Thank you for using ${grailsApplication.metadata['app.name']}.
		</g:else>
	</h2>
	<p>Log <a href="${grailsApplication.config.grails.serverURL}">back in</a>?</p>
	
	<p>Logout of <a href="https://accounts.google.com/logout?continue=https://appengine.google.com/_ah/openid_logout?continue=http://localhost:8080/gplusAuthExample">Google, too</a>?</p>
</body>
</html>
