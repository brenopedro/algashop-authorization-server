package com.algaworks.algashop.authorizationserver.application.user.management;

import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserNotFoundException;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUser;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthUserManagementApplicationService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthUserOutput create(AuthUserInput input) {
        if (authUserRepository.existsByEmail(input.getEmail())) {
            throw new AuthUserEmailAlreadyInUseException(input.getEmail());
        }

        String tempPassword = RandomStringUtils.secure().nextAlphanumeric(12);
        log.info("Generated temporary password for user {}: {}", input.getEmail(), tempPassword); // TODO: send via email

        String passwordHash = passwordEncoder.encode(tempPassword);

        AuthUser authUser = AuthUser.brandNew(input.getEmail(), input.getName(), input.getType(), passwordHash);

        return AuthUserOutput.from(authUserRepository.save(authUser));
    }

    public AuthUserOutput update(UUID userId, AuthUserUpdateInput input) {
        AuthUser user = authUserRepository.findById(userId)
                .orElseThrow(() -> new AuthUserNotFoundException(userId));

        user.setName(input.getName());
        user.setType(input.getType());
        user.setEnabled(input.isEnabled());

        return AuthUserOutput.from(authUserRepository.save(user));
    }

    public void delete(UUID userId) {
        AuthUser user = authUserRepository.findById(userId)
                .orElseThrow(() -> new AuthUserNotFoundException(userId));
        user.anonymize();
        authUserRepository.save(user);
    }
}
