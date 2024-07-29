package org.sohan.BookManagementSystem.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @Email(message = "Email is not formatted")
    @NotEmpty(message = "Email is Mandatory")
    @NotBlank(message = "Email is Required")
    private String email;

    @NotEmpty(message = "Password is Mandatory")
    @NotBlank(message = "Password is Required")
    @Size(min = 8, max = 16, message = "Password has to be at least 8 characters")
    private String password;
}
