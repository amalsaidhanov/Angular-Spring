package io.getarrays.securecapita.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Description of LoginForm
 *
 * @author mac
 * @version 1.0
 * @since 2/17/24
 **/
@Data
public class LoginForm {
    @NotEmpty(message = "email name cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    private String email;
    @NotEmpty(message = "password name cannot be empty")
    private String password;
}
