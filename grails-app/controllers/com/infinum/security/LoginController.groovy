package com.infinum.security

import org.codehaus.groovy.grails.commons.GrailsApplication

import com.google.api.client.auth.oauth2.TokenResponseException
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.oauth2.Oauth2
import com.google.api.services.oauth2.model.Tokeninfo

class LoginController {

	GrailsApplication grailsApplication
	GooglePlusAuthService googlePlusAuthService

	/**
	 * Display Login screen containing Google+ login button
	 */
	def index(){
		[clientId:(grailsApplication.config.gplus.clientId)]
	}
	
	/**
	 * When a user successfully authenticates with Google+, they are redirected 
	 * here with their Access Token.  We use that token to pull the current 
	 * user's profile so that we can verify it is the user
	 */
	def gplus(){
		withForm {
			String code = params.codeInput
			log.debug "Code: ${code}"
			
			if(!code){
				flash.message = 'Invalid Code Received, please try again.'
				redirect(action:'index')
				return
			}
			
			Map verifyTokenResult = googlePlusAuthService.verifyToken(code)
			if(verifyTokenResult.success){
				if(googlePlusAuthService.verifyProfileDomain(verifyTokenResult)){
					flash.message = "Welcome ${verifyTokenResult.email}"
					redirect(uri:"/")
					return
				}else{
					boolean revokeTokenSuccess = 
							googlePlusAuthService.revokeToken(verifyTokenResult.accessToken)
					render(view:"glogout", model:[
							domain:verifyTokenResult.domain,
							email: verifyTokenResult.email,
							gplusDomain:grailsApplication.config.gplus.domain,
							revokeTokenSuccess: revokeTokenSuccess])
					return
				}
			}else{
				flash.message = 'Login Failed'
				log.error("Login Failed: ${verifyTokenResult}")
				redirect(action:'index')
			}
	
		}.invalidToken {
			flash.message = 'Login Failed'
			log.error("Invalid CSRF Token")
			redirect(action:'index')
		}
	}
}
