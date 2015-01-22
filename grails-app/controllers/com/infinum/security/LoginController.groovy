package com.infinum.security

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Secured('permitAll')
class LoginController {

	GrailsApplication grailsApplication
	GooglePlusAuthService googlePlusAuthService
	SpringSecurityService springSecurityService
	
	/**
	 * Default action; redirects to 'defaultTargetUrl' if logged in, /login/auth otherwise.
	 */
	def index(){
		if (springSecurityService.isLoggedIn()) {
			redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
		} else {
			log.debug "User is not logged in, redirecting to auth page"
			redirect action: 'auth', params: params
		}
	}

	/**
	 * Display Login screen containing Google+ login button
	 */
	def auth(){
		def config = SpringSecurityUtils.securityConfig

		if (springSecurityService.isLoggedIn()) {
			redirect uri: config.successHandler.defaultTargetUrl
			return
		}
		
		[clientId:(grailsApplication.config.gplus.clientId), 
				approvalPrompt:(params.switchAccounts)?'force':'auto']
	}
	
	/**
	 * When a user successfully authenticates with Google+, they are redirected 
	 * here with their Access Token.  We use that token to pull the current 
	 * user's profile so that we can verify it is the user
	 */
	def gplus(){

		log.debug("Google Plus sign in starting")

		withForm {
			String code = params.codeInput
			log.debug "Code: ${code}"
			
			if(!code){
				flash.message = 'Invalid Code Received, please try again.'
				redirect(action:'auth')
				return
			}
			
			Map verifyTokenResult = googlePlusAuthService.verifyToken(code)
			if(verifyTokenResult.success){
				log.debug("Successfully verified Google Plus Token")
				if(googlePlusAuthService.verifyProfileDomain(verifyTokenResult)){
					try{
						springSecurityService.reauthenticate verifyTokenResult.email
						flash.message = "Welcome ${verifyTokenResult.email}"
						redirect(uri:"/")
						return
					} catch(UsernameNotFoundException unfe){
						flash.message = "Your user account (${verifyTokenResult.email}) has not been setup.  Please try again later, or contact support."
						redirect(uri:"/")
						return
					}
				}else{
					boolean revokeTokenSuccess = 
							googlePlusAuthService.revokeToken(verifyTokenResult.accessToken)
					render(view:"glogout", model:[
							domain:verifyTokenResult.domain,
							email: verifyTokenResult.email,
							org:grailsApplication.config.gplus.org,
							revokeTokenSuccess: revokeTokenSuccess])
					return
				}
			}else{
				flash.message = 'Login Failed'
				log.error("Login Failed: ${verifyTokenResult}")
				redirect(action:'auth')
			}
	
		}.invalidToken {
			flash.message = 'Login Failed'
			log.error("Invalid CSRF Token")
			redirect(action:'auth')
		}
	}

	/**
	 * Callback after a failed login. Redirects to the auth page with a warning message.
	 */
	def authfail(){

		def username = session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY]
		String msg = ''
		def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
		if (exception) {
			if (exception instanceof AccountExpiredException) {
				msg = g.message(code: "springSecurity.errors.login.expired")
			}
			else if (exception instanceof CredentialsExpiredException) {
				msg = g.message(code: "springSecurity.errors.login.passwordExpired")
			}
			else if (exception instanceof DisabledException) {
				msg = g.message(code: "springSecurity.errors.login.disabled")
			}
			else if (exception instanceof LockedException) {
				msg = g.message(code: "springSecurity.errors.login.locked")
			}
			else {
				msg = g.message(code: "springSecurity.errors.login.fail")
			}
		}

		if (springSecurityService.isAjax(request)) {
			render([error: msg] as JSON)
		}
		else {
			flash.message = msg
			redirect action: 'auth', params: params
		}
	}

	/**
	 * Show denied page.
	 */
	def denied(){
		if (springSecurityService.isLoggedIn()) {
			flash.message = "Access denied"
			redirect('uri':'/')
			return
		}

		flash.message = "Access denied"
		log.warn "Access Denied.  Redirecting to home page."
		redirect('uri':'/')
	}

}
