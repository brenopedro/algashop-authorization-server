package com.algaworks.algashop.authorizationserver.infrastructure.sucurity;

import com.algaworks.algashop.authorizationserver.infrastructure.sucurity.oidc.OidcUserInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthorizationServerSecurityConfig {

    private final OidcUserInfoMapper oidcUserInfoMapper;
    private final OidcLogoutAuthenticationSuccessHandler oidcLogoutAuthenticationSuccessHandler;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerFilterChain(HttpSecurity httpSecurity) {
        OAuth2AuthorizationServerConfigurer authorizationServer = new OAuth2AuthorizationServerConfigurer();


        httpSecurity.securityMatcher(authorizationServer.getEndpointsMatcher())
                .with(authorizationServer, configurer -> configurer.oidc(
                        oidcConfigurer -> oidcConfigurer
                                .logoutEndpoint(logout -> logout.logoutResponseHandler(oidcLogoutAuthenticationSuccessHandler))
                                .userInfoEndpoint(userInfo -> userInfo.userInfoMapper(oidcUserInfoMapper))))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions.defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                ));
        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());
        return httpSecurity.build();
    }
}
