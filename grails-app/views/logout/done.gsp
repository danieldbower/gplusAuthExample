<html>
<head>
	<meta name='layout' content='main' />
	<title>${grailsApplication.metadata['app.name']}<g:message code="springSecurity.login.title" /></title>
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
	
	<g:if test="${grailsApplication.config.gplus.active}">
		<p>Logout of <a href="${gPlusLogoutUrl}">Google, too</a>?</p>
	</g:if>
</body>
</html>
