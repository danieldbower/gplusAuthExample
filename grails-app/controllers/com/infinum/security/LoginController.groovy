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
	
	HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport()
	
	JsonSlurper slurper = new JsonSlurper()
	
	private final GenericUrl googleMeUrl = new GenericUrl('https://www.googleapis.com/plus/v1/people/me')

	def index(){
		[clientId:(grailsApplication.config.gplus.clientId)]
	}
	
	def gplus(){
		String accessToken = params.accessTokenInput
		log.debug "\n\nAuth Access: ${accessToken} \n"
		
		if(!accessToken){
			flash.message = 'Invalid Access Token Received, please try again.'
			redirect(action:'index')
		}

		Credential credential = new Credential(
				BearerToken.authorizationHeaderAccessMethod())
		credential.setAccessToken(accessToken)

		HttpRequestFactory requestFactory = 
				httpTransport.createRequestFactory(credential)

		HttpResponse gResp = requestFactory.buildGetRequest(googleMeUrl).execute()

		def gProfile = slurper.parse(new BufferedReader(new InputStreamReader(gResp.content)))
		log.debug(gProfile)

		if(gResp.successStatusCode){
			if(gProfile.domain == grailsApplication.config.gplus.domain){
				flash.message = "Welcome ${gProfile.emails[0].value}"
				redirect(uri:"/")
			}else{
				render(view:"glogout", 
						model:[
								domain:gProfile.domain, 
								emails: gProfile.emails.value, 
								gplusDomain:grailsApplication.config.gplus.domain])
			}
		}else{
			flash.message = 'Login Failed'
			log.error("Login Failed: ${gProfile}")
			redirect(action:'index')
		}
	}

}
