package com.algaworks.algashop.authorizationserver.infrastructure.security.cookie;

import com.algaworks.algashop.authorizationserver.infrastructure.security.AlgaShopSecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class CookieConfig {

    @Bean
    CookieSerializer cookieSerializer(AlgaShopSecurityProperties properties) {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setCookiePath("/");
        serializer.setUseHttpOnlyCookie(true);

        AlgaShopSecurityProperties.CookieProperties cookieProperties = properties.getCookie();
        serializer.setUseSecureCookie(cookieProperties.getSecure());
        serializer.setSameSite(cookieProperties.getSameSite());
        serializer.setDomainName(cookieProperties.getDomainName());

        return serializer;
    }
}
