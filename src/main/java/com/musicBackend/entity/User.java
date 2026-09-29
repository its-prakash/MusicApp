package com.musicBackend.entity;

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

    @Id
    private Long id;

    @Column("userName")
    private String userName;

    private String password;
    private String role;

    // getters and setters



}
