package io.getarrays.securecapita.form;

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
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
