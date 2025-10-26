package com.skytech.projectmanagement.user.entity;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
public class UserRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_refresh_token_id_gen")
    @SequenceGenerator(name = "user_refresh_token_id_gen",
            sequenceName = "user_refresh_token_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "ip_address")
    private String ipAddress;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
