package com.runtime.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

	@Value("${jwt.decoder.endpoint}")
	private String jwtDecoderEndPoint;

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.cors();
		http.csrf().disable();
		http.authorizeExchange()
		.pathMatchers("/set-run-update")
						
				.permitAll().anyExchange().authenticated().and().oauth2ResourceServer().jwt();
		return http.build();
	}

	@Bean
	public ReactiveJwtDecoder jwtDecoder() {
		return ReactiveJwtDecoders.fromOidcIssuerLocation(jwtDecoderEndPoint);
	}

	@Bean
	public TokenStore jwkTokenStore() {
		return new JwkTokenStore(Collections.singletonList(jwtDecoderEndPoint), new CognitoAccessTokenConvertor(),
				null);

	}
}
