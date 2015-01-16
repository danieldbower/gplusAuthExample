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

/**
 * Authenticate a user with the Google Plus Api
 */
class GooglePlusAuthService {

	GrailsApplication grailsApplication
	
	private HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport()
	private JsonSlurper slurper = new JsonSlurper()
	
	private final GenericUrl googleMeUrl = new GenericUrl(
			'https://www.googleapis.com/plus/v1/people/me')
	private final String googleRevokeTokenUrl = 
			'https://accounts.google.com/o/oauth2/revoke'
	
	/**
	 * Verify a user's google+ access Token with google
	 * @return map of success, domain of account, and list of email address for account
	 */
	Map verifyToken(String accessToken){
		HttpRequestFactory requestFactory = 
				createRequestFactory(accessToken)

		HttpResponse gResp = requestFactory.buildGetRequest(googleMeUrl).execute()

		def gProfile = slurper.parse(new BufferedReader(new InputStreamReader(gResp.content)))
		
		if(gResp.successStatusCode){
			log.debug(gProfile)
			return [success:true, domain:gProfile.domain, emails:gProfile.emails.value]
		}else{
			log.warn(gProfile)
			return [success:false]
		}
	}
	
	/**
	 * Verify the domain of the account is accepted for this application
	 * @return valid
	 */
	boolean verifyProfileDomain(Map verifyTokenResult){
		return verifyTokenResult.domain == grailsApplication.config.gplus.domain
	}
	
	/**
	 * This flow revokes access for this application to the user's profile, 
	 * effectively disassociating that account from this application.
	 * @return success
	 */
	boolean revokeToken(String accessToken){
		HttpRequestFactory requestFactory = 
				createRequestFactory(accessToken)
		
		HttpResponse revokeResponse = requestFactory.buildGetRequest(
						new GenericUrl(googleRevokeTokenUrl + '?token=' + accessToken)).execute()
		if(revokeResponse.successStatusCode){
			log.debug("Successfully revoked accessToken from Google")
			return true
		}else{
			log.error("Failed to revoke accessToken from Google")
			return false
		}
	}

	/**
	 * Builds an HttpRequestFactory for google using the accessToken
	 * @param accessToken
	 */
	private HttpRequestFactory createRequestFactory(String accessToken){
		Credential credential = new Credential(
				BearerToken.authorizationHeaderAccessMethod())
		credential.setAccessToken(accessToken)
		
		return httpTransport.createRequestFactory(credential)
	}
}
