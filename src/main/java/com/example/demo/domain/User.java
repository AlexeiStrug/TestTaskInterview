package com.example.demo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users", schema = "public", catalog = "postgres")
public class User extends BaseEntity {

    @Builder
    public User(Long id, Timestamp createdAt, Timestamp updatedAt, String username, String password, Role role) {
        super(id, createdAt, updatedAt);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Column(unique = true)
    private String username;

    private String password;

    private String token;

    @Column(name = "is_user_can_reset_password")
    private Boolean isUserCanResetPassword;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
