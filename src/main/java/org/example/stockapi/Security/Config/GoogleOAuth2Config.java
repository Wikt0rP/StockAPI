package org.example.stockapi.Security.Config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleOAuth2Config {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() throws IOException {
       ObjectMapper mapper = new ObjectMapper();
       InputStream inputStream = new ClassPathResource("credentials.json").getInputStream();
       JsonNode credentials = mapper.readTree(inputStream);

       String clientId = credentials.get("installed").get("client_id").asText();
       String clientSecret = credentials.get("installed").get("client_secret").asText();

       return new InMemoryClientRegistrationRepository(getGoogleClientRegistration(clientId, clientSecret));
    }

    private ClientRegistration getGoogleClientRegistration(String clientId, String clientSecret){
        return ClientRegistration.withRegistrationId("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scope("profile", "email")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://accounts.google.com/o/oauth2/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .redirectUri("{baseUrl}/login/oauth2/code/google")
                .clientName("Google")
                .build();
    }
}
