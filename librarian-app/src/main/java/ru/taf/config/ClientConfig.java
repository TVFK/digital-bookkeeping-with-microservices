package ru.taf.config;

import de.codecentric.boot.admin.client.registration.BlockingRegistrationClient;
import de.codecentric.boot.admin.client.registration.RegistrationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ru.taf.client.RestClientBooksRestClient;
import ru.taf.client.RestClientPeopleRestClient;
import ru.taf.security.OAuth2ClientHttpRequestInterceptor;

import java.util.Objects;

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

    @Bean
    @ConditionalOnProperty(name = "spring.boot.admin.client.enabled", havingValue = "true")
    public RegistrationClient registrationClient(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository,
                        authorizedClientService);

        RestTemplate restTemplate = new RestTemplateBuilder()
                .interceptors((request, body, execution) -> {
                    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(OAuth2AuthorizeRequest
                                .withClientRegistrationId("metrics")
                                .principal("librarian-app")
                                .build());

                        request.getHeaders().setBearerAuth(Objects.requireNonNull(authorizedClient, "тут это как бы null...").getAccessToken().getTokenValue());
                    }

                    return execution.execute(request, body);
                })
                .build();
        return new BlockingRegistrationClient(restTemplate);
    }
}
