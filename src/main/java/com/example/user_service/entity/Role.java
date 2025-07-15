package com.example.user_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable, GrantedAuthority {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "authority")
    private String authority;

}
