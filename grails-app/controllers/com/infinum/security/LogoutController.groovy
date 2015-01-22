package com.infinum.security
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

@Secured('permitAll')
class LogoutController {

	GrailsApplication grailsApplication
	SpringSecurityService springSecurityService
	
	private String gPlusLogoutUrl(){
		return "https://accounts.google.com/logout?continue=https://appengine.google.com/_ah/openid_logout?continue=${grailsApplication.config.grails.serverURL}"
	}
	
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
		
		if(grailsApplication.config.gplus.logOutOfGoogleImmediately){
			redirect(uri:gPlusLogoutUrl())
			return
		}else{
			return [gPlusLogoutUrl:gPlusLogoutUrl()]
		}
	}
}
