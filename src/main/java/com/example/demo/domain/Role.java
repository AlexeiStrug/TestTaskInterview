package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles", schema = "public", catalog = "postgres")
public class Role extends BaseEntity {

    @Builder
    public Role(Long id, Timestamp createdAt, Timestamp updatedAt, String roleName, Set<Permission> permissions) {
        super(id, createdAt, updatedAt);
        this.roleName = roleName;
        this.permissions = permissions;
    }

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany
    @JsonIgnore
    private Set<Permission> permissions;
}
