package io.getarrays.securecapita.service;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;

import java.util.Collection;

public interface UserService {
    UserDTO createUser(User user);
    UserDTO getUserByEmail(String email);
    void sendVerification(UserDTO userDTO);
    Collection<User> list(int page, int pageSize);
    void delete(Long id);
}
