package com.skytech.projectmanagement.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "hash_password", columnDefinition = "TEXT")
    private String hashPassword;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "is_product_owner")
    private Boolean isProductOwner;
}
