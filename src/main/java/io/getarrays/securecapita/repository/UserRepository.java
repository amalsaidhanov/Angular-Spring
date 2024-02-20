package io.getarrays.securecapita.repository;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Amal
 * @version 1.0
 * @since 21/12/2023
 **/
public interface UserRepository<T extends User> {

    T create(T data);

    Collection<T> list(int page, int pageSize);

    T get(Long id);

    T update(T data);

    Boolean delete(Long id);

    //complex
    User getUserByEmail(String email);
    void sendVerificationCode(UserDTO userDTO);
}
