package com.infinum.security

import groovy.json.JsonSlurper

import org.codehaus.groovy.grails.commons.GrailsApplication

import com.google.api.client.auth.oauth2.BearerToken
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.TokenResponseException
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpResponse
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.oauth2.Oauth2
import com.google.api.services.oauth2.model.Tokeninfo

/**
 * Authenticate a user with the Google Plus Api
 */
class GooglePlusAuthService {

	GrailsApplication grailsApplication
	
	private HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport()
	private JsonFactory jsonFactory = JacksonFactory.getDefaultInstance()
	private JsonSlurper slurper = new JsonSlurper()
	
	private final GenericUrl googleMeUrl = new GenericUrl(
			'https://www.googleapis.com/plus/v1/people/me')
	private final String googleRevokeTokenUrl = 
			'https://accounts.google.com/o/oauth2/revoke'
	
	/**
	 * Verify a user's google+ access Token with google
	 * @return map of success, domain of account, and list of email address for account
	 */
	Map verifyToken(String code){
		String clientId = grailsApplication.config.gplus.clientId
		String clientSecret = grailsApplication.config.gplus.clientSecret
		
		// Upgrade the authorization code into an access and refresh token.
		GoogleTokenResponse tokenResponse 
		try{
			tokenResponse =
				new GoogleAuthorizationCodeTokenRequest(httpTransport, 
						jsonFactory, clientId, clientSecret, code, 
						"postmessage").execute()
			log.debug("Token Response:  " + tokenResponse)
		} catch (TokenResponseException e) {
			log.warn "Failed to upgrade the authorization code."
			return [success:false]
		} catch (IOException e) {
			log.warn("Failed to read token data from Google. " + e.getMessage())
			return [success:false]
		}

		// Store the token in the session for later use.
		//request.session().attribute("token", tokenResponse.toString());
		
		// Create a credential representation of the token data.
		GoogleCredential credential = 
				new GoogleCredential.Builder().setJsonFactory(jsonFactory).setTransport(httpTransport).setClientSecrets(clientId, clientSecret).build().setFromTokenResponse(tokenResponse)
		
		// Check that the token is valid.
		Oauth2 oauth2 = new Oauth2.Builder(
				httpTransport, jsonFactory, credential).build()
		
		String accessToken = credential.getAccessToken()
		
		Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(accessToken).execute()
		log.debug("Token Info:  " + tokenInfo)
			
		// If there was an error in the token info, abort.
		if (tokenInfo.containsKey("error")) {
			log.error(tokenInfo.get("error").toString())
			return [success:false]
		}
		
		// Make sure the token we got is for our app.
		if (!tokenInfo.getIssuedTo().equals(clientId)) {
			log.error("Token's client ID does not match app's.")
			return [success:false]
		}
		
		HttpRequestFactory requestFactory = 
				createRequestFactory(accessToken)
		
		HttpResponse gResp = requestFactory.buildGetRequest(googleMeUrl).execute()
		
		def gProfile = slurper.parse(new BufferedReader(new InputStreamReader(gResp.content)))
		
		if(gResp.successStatusCode){
			log.debug(gProfile)
			return [success:true, domain:gProfile.domain, 
					email:tokenInfo.email, accessToken:accessToken]
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
		List validDomains = grailsApplication.config.gplus.domains
		return validDomains.contains(verifyTokenResult.domain)
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
