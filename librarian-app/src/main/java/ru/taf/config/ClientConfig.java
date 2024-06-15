package ru.taf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import ru.taf.client.RestClientBooksRestClient;
import ru.taf.client.RestClientPeopleRestClient;
import ru.taf.security.OAuth2ClientHttpRequestInterceptor;

@Configuration
public class ClientConfig {

    @Bean
    public RestClientPeopleRestClient peopleRestClient(
            @Value("${services.bookkeeping.uri:http://localhost:8080}") String bookkeepingServiceBaseUri,
            @Value("${services.bookkeeping.registration-id:keycloak}") String registrationId,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {
        return new RestClientPeopleRestClient(RestClient.builder()
                .baseUrl(bookkeepingServiceBaseUri)
                .requestInterceptor(new OAuth2ClientHttpRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(
                                clientRegistrationRepository,
                                authorizedClientRepository
                        ), registrationId
                ))
                .build());
    }

    @Bean
    public RestClientBooksRestClient booksRestClient(
            @Value("${services.bookkeeping.uri:http://localhost:8080}") String bookkeepingServiceBaseUri,
            @Value("${services.bookkeeping.registration-id:keycloak}") String registrationId,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {
        return new RestClientBooksRestClient(RestClient.builder()
                .baseUrl(bookkeepingServiceBaseUri)
                .requestInterceptor(new OAuth2ClientHttpRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(
                                clientRegistrationRepository,
                                authorizedClientRepository
                        ), registrationId
                ))
                .build());
    }
}
