package com.budlee.oauth2.service;

import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailService {

	private final WebClient webClient;

	public EmailService(
	) {
		this.webClient = WebClient.builder().build();
	}

	public Mono<String> getEmailContacts(final OAuth2AccessToken accessToken) {
		return webClient.get().uri("http://localhost:9090/emails")
				.header(HttpHeaders.AUTHORIZATION, "Bearer "+ accessToken.getTokenValue())
				.exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))

				;
	}
}
