package com.budlee.oauth2.controller;

import java.security.Principal;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/myToken")
public class Tokens {

	@GetMapping()
	public String simpleRequest(
			Principal jwtAuthentication,
			@RegisteredOAuth2AuthorizedClient() OAuth2AuthorizedClient authorizedClient
	)

	{
		return authorizedClient.getAccessToken().getTokenValue();
	}
}
