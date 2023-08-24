package com.sendemail.webclient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

//import ch.qos.logback.classic.Logger;
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {
	
	//Logger logger = LoggerFactory.getLogger(WebFluxConfig.class);
	
	@Bean
	public WebClient getwebClient() throws Exception{
		
		SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
				                   .build();
		
		HttpClient httpClient = HttpClient.create().keepAlive(true).option(ChannelOption.SO_KEEPALIVE,true)
	        .wiretap("reactor.netty.http.client.HttpClient",LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
	        .secure(t -> t.sslContext(sslContext)).option(ChannelOption.CONNECT_TIMEOUT_MILLIS,60000)
            .responseTimeout(Duration.ofMillis(60000))
            .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(60000, TimeUnit.MILLISECONDS))
                  .addHandlerLast(new WriteTimeoutHandler(60000, TimeUnit.MILLISECONDS)));
	        
	        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));
	        
	        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
	        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
	        
	        return WebClient.builder().uriBuilderFactory(factory).clientConnector(connector)
	        		.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	
	
	}
}
