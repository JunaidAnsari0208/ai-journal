package com.junaid.ai_journal.security;


import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter // Lombok annotation to automatically create the getUserId() method
public class UserPrincipal extends User {
    private final Long userId;

    public UserPrincipal(Long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        // We pass an empty password as it's not needed after authentication.
        super(username, "", authorities);
        this.userId = userId;
    }
}
