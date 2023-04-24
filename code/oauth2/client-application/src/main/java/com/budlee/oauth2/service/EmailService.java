package com.budlee.oauth2.service;

import org.springframework.scheduling.annotation.Scheduled;

public class EmailService {

	@Scheduled(fixedDelay = 10000l)
	public void getEmailsAndPrint(){
		String url = new String("http://localhost:9090/emails");

	}
}
