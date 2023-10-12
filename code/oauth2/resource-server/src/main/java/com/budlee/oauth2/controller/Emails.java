package com.budlee.oauth2.controller;

import java.util.List;
import java.util.Map;

import com.budlee.oauth2.controller.model.EmailContact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/emails")
public class Emails {

	Logger LOG = LoggerFactory.getLogger(Emails.class);

	Map<String, List<EmailContact>> clientEmailStore = Map.of(
//			ClientCredentialsApp
			"21d9fe28-1c77-4c95-b3ac-d6004032b94a", List.of(
					new EmailContact("noReply@mail.com", "No Reply Mail")
			),
//			Matt
			"ef3829bb-5191-435a-9a7b-c60851669fd6", List.of(
					new EmailContact("mum@mail.com", "Mum's email address"),
					new EmailContact("malcom@mail.com", "Malcom's email address")
			)
	);

	@GetMapping
	public List<EmailContact> getEmailContacts(JwtAuthenticationToken principal) {
		if (!principal.getToken().getClaimAsString("scope").contains("Contacts-API")){
			throw new AuthorizationServiceException("Not Authorized");
		}
		var clientEmails = clientEmailStore.get(principal.getToken().getClaimAsString("sub"));
		if (clientEmails == null) {
			throw new AuthorizationServiceException("Not Authorized");
		}
		LOG.info("sub [{}]",principal.getToken().getClaimAsString("sub") );
		LOG.info("emails found: [{}]", clientEmails);
		return clientEmails;
	}
}
