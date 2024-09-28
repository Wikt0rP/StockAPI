package org.example.stockapi.Security.Service;

import org.example.stockapi.Security.Impl.CustomOAuth2User;

import org.example.stockapi.Service.OAuthUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthUserService authUserService;

    public CustomOAuth2UserService(OAuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User user = super.loadUser(oAuth2UserRequest);

        String email = user.getAttribute("email");
        String givenName = user.getAttribute("given_name");
        String googleId = user.getAttribute("sub");

        authUserService.processOAuthPostLogin(email, givenName, googleId);
        return new CustomOAuth2User(user);
    }

}
