package com.db.coffeestore9.user.service;

import com.db.coffeestore9.security.common.Role;
import com.db.coffeestore9.user.common.RegistrationForm;
import com.db.coffeestore9.user.domain.User;
import com.db.coffeestore9.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    @Transactional
    public void processRegistration(RegistrationForm form) {
        User user = form.toUser();
        user.addAuthority(Role.ROLE_USER);
        userRepository.save(user);
    }


}
