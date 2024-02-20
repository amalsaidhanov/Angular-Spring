package io.getarrays.securecapita.service.implementation;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.mapper.dtomapper.userdtomapper.UserDTOMapper;
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


    @Override
    public UserDTO createUser(User user) {
        return fromUser(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendVerification(UserDTO userDTO) {
userRepository.sendVerificationCode(userDTO);
    }

    //
    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.delete(id);
    }
    @Override
    public Collection<User> list(int page, int pageSize) {
        return userRepository.list(page, pageSize);
    }
}