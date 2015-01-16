package com.infinum.security

import groovy.json.JsonSlurper

import org.codehaus.groovy.grails.commons.GrailsApplication

import com.google.api.client.auth.oauth2.BearerToken
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpResponse
import com.google.api.client.http.HttpTransport

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
			String accessToken = params.accessTokenInput
			log.debug "\n\nAuth Access: ${accessToken} \n"
			
			if(!accessToken){
				flash.message = 'Invalid Access Token Received, please try again.'
				redirect(action:'index')
				return
			}
	
			Map verifyTokenResult = googlePlusAuthService.verifyToken(accessToken)
			if(verifyTokenResult.success){
				if(googlePlusAuthService.verifyProfileDomain(verifyTokenResult)){
					flash.message = "Welcome ${verifyTokenResult.emails}"
					redirect(uri:"/")
					return
				}else{
					boolean revokeTokenSuccess = googlePlusAuthService.revokeToken(accessToken)
					render(view:"glogout", model:[
							domain:verifyTokenResult.domain,
							emails: verifyTokenResult.emails,
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
