package com.db.coffeestore9.user.common;

import com.db.coffeestore9.user.domain.User;

public record RegistrationForm(String username, String password, String name, String nickname) {

    public User toUser() {
        return User.builder()
            .username(this.username)
            .password(this.password)
            .name(this.name)
            .nickname(this.nickname)
            .build();
    }
}
