package com.example.demo.domain;

import com.example.demo.domain.enums.PermissionEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "permissions", schema = "public", catalog = "postgres")
public class Permission extends BaseEntity {

    @Builder
    public Permission(Long id, Timestamp createdAt, Timestamp updatedAt, String name) {
        super(id, createdAt, updatedAt);
        this.name = name;
    }

    private String name;
}
