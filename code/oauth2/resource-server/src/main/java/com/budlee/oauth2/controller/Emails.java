package com.budlee.oauth2.controller;

import java.util.List;
import java.util.Map;

import com.budlee.oauth2.controller.model.EmailContact;

import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/emails")
public class Emails {

	Map<String, List<EmailContact>> clientEmailStore = Map.of(
			"ClientCredentialsApp", List.of(
					new EmailContact("noReply@mail.com", "No Reply Mail")
			)
	);

	@GetMapping
	public List<EmailContact> getEmailContacts(JwtAuthenticationToken principal) {
		var clientEmails = clientEmailStore.get(principal.getToken().getClaimAsString("azp"));
		if (clientEmails == null) {
			throw new AuthorizationServiceException("Not Authorized");
		}
		return clientEmails;
	}
}
