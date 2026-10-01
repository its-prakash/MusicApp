package com.musicBackend.Authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


import org.springframework.data.relational.core.mapping.Column;


@Setter
@Getter
@NoArgsConstructor
@Table("users")
public class User {

    public enum Role {
        ROLE_USER,
        ROLE_ARTIST,
        ROLE_ADMIN
    }

    @Id
    private Long id;

    @Column("userName")
    private String userName;

    @Column("email")
    private String email;

    private String password;
    private String role;



}
