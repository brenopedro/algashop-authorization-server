package com.algaworks.algashop.authorizationserver.infrastructure.sucurity.oidc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;

@Configuration
public class OidcLogoutSuccessHandlerConfig {

    @Bean
    public OidcLogoutAuthenticationSuccessHandler oidcLogoutAuthenticationSuccessHandler(
            OidcRevokeAuthorizationsLogoutHandler logoutHandler
    ) {
        var logoutSuccessHandler = new OidcLogoutAuthenticationSuccessHandler();
        logoutSuccessHandler.setLogoutHandler(logoutHandler);
        return logoutSuccessHandler;
    }

}

