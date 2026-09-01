package com.algaworks.algashop.authorizationserver.infrastructure.sucurity.oidc;

import com.algaworks.algashop.authorizationserver.domain.model.AuthUser;
import com.algaworks.algashop.authorizationserver.domain.model.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OidcUserInfoService {

    private final AuthUserRepository authUserRepository;

    public OidcUserInfo loadUser(String email) {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + email));

        return OidcUserInfo.builder()
                .subject(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .claim("type", user.getType().name())
                .claim("created_at", String.valueOf(user.getCreatedAt().toEpochSecond()))
                .build();
    }
}
