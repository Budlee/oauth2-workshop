package com.budlee.oauth2.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/contacts")
public class ContactsFinder {


	@GetMapping()
	public String simpleRequest(
	Principal jwtAuthentication
	){
//		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
//				.principal(jwtAuthentication.getName())
//				.build();
//		OAuth2AuthorizedClient authorizedClient = authorizeRequest.getAuthorizedClient();
//		OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
		return "Hello, your access token information is: " + jwtAuthentication;
	}

}
