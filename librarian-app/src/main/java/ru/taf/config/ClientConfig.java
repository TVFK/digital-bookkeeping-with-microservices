package ru.taf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.taf.client.RestClientBooksRestClient;
import ru.taf.client.RestClientPeopleRestClient;

@Configuration
public class ClientConfig {

    @Bean
    public RestClientPeopleRestClient peopleRestClient(
            @Value("${services.bookkeeping.uri:http://localhost:8080}") String bookkeepingServiceBaseUri) {
        return new RestClientPeopleRestClient(RestClient.builder()
                .baseUrl(bookkeepingServiceBaseUri).build());
    }

    @Bean
    public RestClientBooksRestClient bookRestClient(
            @Value("${services.bookkeeping.uri:http://localhost:8080}") String bookkeepingServiceBaseUri) {
        return new RestClientBooksRestClient(RestClient.builder()
                .baseUrl(bookkeepingServiceBaseUri).build());
    }
}
