package io.getarrays.securecapita.service.implementation;

import io.getarrays.securecapita.domain.Role;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.mapper.dtomapper.userdtomapper.UserDTOMapper;
import io.getarrays.securecapita.repository.RoleRepository;
import io.getarrays.securecapita.repository.UserRepository;
import io.getarrays.securecapita.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static io.getarrays.securecapita.mapper.dtomapper.userdtomapper.UserDTOMapper.fromUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRoleRepository;


    @Override
    public UserDTO createUser(User user) {
        return fromUser(userRepository.create(user));
    }

    @Override
    public Collection<UserDTO> list(int page, int pageSize) {
//        logger.info("Trying to add!!!!!!!!!!!");
        return userRepository.list(page, pageSize);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
    }

}