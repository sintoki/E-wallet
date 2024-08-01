package org.demo.UserService.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.demo.UserService.model.User;

import jakarta.validation.constraints.NotNull;
import org.demo.utils.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateUserRequest {

    private String name;
@NotBlank
    private String contact;
@NotBlank
    private String email;

    @NotBlank
    private String password;
    @NotNull
    private UserIdentifier userIdentifier;
    private String userIdentifierValue;

public User toUser(){

        return User.builder()
                .name(this.name)
                .contact(this.contact)
                .email(this.email)
                .userIdentifier(this.userIdentifier)
                .userIdentifierValue(this.userIdentifierValue)
                .build();
    }
}
