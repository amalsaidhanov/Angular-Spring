package io.getarrays.securecapita.resource;


import io.getarrays.securecapita.domain.HttpResponse;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.form.LoginForm;
import io.getarrays.securecapita.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

import static java.time.LocalTime.now;
import static java.util.Map.of;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        // we can use own (new SecurecapitaAthentication)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(),loginForm.getPassword()));
        UserDTO userDTO = userService.getUserByEmail(loginForm.getEmail());
        return userDTO.isUsingMfa() ? sendVerificationCode(userDTO) : sendResponse(userDTO);

//        return ResponseEntity.ok().body(
//                HttpResponse.builder()
//                        .timeStamp(now().toString())
//                        .data(of("user", userDTO))
//                        .message("Login Success")
//                        .status(HttpStatus.OK)
//                        .statusCode(HttpStatus.OK.value())
//                        .build()
//        );
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
        UserDTO userDTO = userService.createUser((user));
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDTO))
                        .message("User created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }



    @GetMapping("/getall")
    public Collection<User> getAllUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return userService.list(page, pageSize);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", id))
                        .message("User deleted")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
    private ResponseEntity<HttpResponse> sendResponse(UserDTO userDTO) {
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDTO))
                        .message("Login Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO userDTO) {
        userService.sendVerification(userDTO);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDTO))
                        .message("Verification code sent")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }
}