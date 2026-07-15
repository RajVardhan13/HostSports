package dev.raj.hostsports.dto.auth;

import dev.raj.hostsports.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Full Name is Required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be Valid")
    private String email;

    @NotBlank(message = "password is Required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @NotNull(message = "Role is Required")
    private Role role;
}
