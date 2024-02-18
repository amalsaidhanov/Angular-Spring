package io.getarrays.securecapita.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class User {

    @Id
    private Long id;

    @Size(min = 1, max = 255, message = "At least 1 and at most items are allowed")
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @Size(min = 1, max = 255, message = "At least 1 and at most items are allowed")
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @Size(min = 1, max = 255, message = "At least 1 and at most items are allowed")
    @NotEmpty(message = "email name cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    private String email;

    @NotEmpty(message = "password name cannot be empty")
    private String password;

    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private boolean enabled;
    private boolean isNotLocked;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;

}
