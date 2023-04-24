package com.budlee.oauth2.config;

import org.springframework.context.annotation.Configuration;
@Configuration
public class OAuth2Config {
//	@Bean
//	public OAuth2AuthorizedClientManager authorizedClientManager(
//			ClientRegistrationRepository clientRegistrationRepository,
//			OAuth2AuthorizedClientRepository authorizedClientRepository) {
//
//		JwtBearerOAuth2AuthorizedClientProvider jwtBearerAuthorizedClientProvider =
//				new JwtBearerOAuth2AuthorizedClientProvider();
//
//		OAuth2AuthorizedClientProvider authorizedClientProvider =
//				OAuth2AuthorizedClientProviderBuilder.builder()
//						.provider(jwtBearerAuthorizedClientProvider)
//						.build();
//
//		DefaultOAuth2AuthorizedClientManager authorizedClientManager =
//				new DefaultOAuth2AuthorizedClientManager(
//						clientRegistrationRepository, authorizedClientRepository);
//		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//		return authorizedClientManager;
//	}
}
