package com.infinum.security
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

@Secured('permitAll')
class LogoutController {

	SpringSecurityService springSecurityService
	
	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index(){
		log.info "Logging Out"
		
		redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
	}
	
	def done(){
		if (springSecurityService.isLoggedIn()) {
			redirect action: 'index'
			return
		}
		
		
	}
}
