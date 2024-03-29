package com.budlee.oauth2.controller;

import java.security.Principal;

import com.budlee.oauth2.service.EmailService;
import reactor.core.publisher.Mono;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/contacts")
public class ContactsFinder {

	private final EmailService emailService;

	public ContactsFinder(final EmailService emailService) {
		this.emailService = emailService;

	}

	@GetMapping()
	public Mono<String> simpleRequest(
			Principal jwtAuthentication,
			@RegisteredOAuth2AuthorizedClient() OAuth2AuthorizedClient authorizedClient
	)

	{

		return emailService.getEmailContacts(authorizedClient.getAccessToken());
	}
}
