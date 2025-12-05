package com.ace.authservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String email;
    private String password;

    private String firstName;
    private String lastName;
    private String country;

    private Set<String> roles;
    private boolean verified;
    private String verificationToken;
}

