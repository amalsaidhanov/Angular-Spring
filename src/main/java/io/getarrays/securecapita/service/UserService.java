package io.getarrays.securecapita.service;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;

import java.util.Collection;

public interface UserService {
    UserDTO createUser(User user);


    Collection<UserDTO> list(int page, int pageSize);


}
