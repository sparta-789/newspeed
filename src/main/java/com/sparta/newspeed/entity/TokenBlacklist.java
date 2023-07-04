package com.sparta.newspeed.entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Component
public class TokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;
    private String token;

    public TokenBlacklist() {
    }

    public TokenBlacklist(String token) {
        this.token = token;
    }
}
