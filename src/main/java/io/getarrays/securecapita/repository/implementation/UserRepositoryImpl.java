package io.getarrays.securecapita.repository.implementation;

import io.getarrays.securecapita.domain.Role;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.domain.UserPrincipal;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.Apiexception;
import io.getarrays.securecapita.mapper.rowmapper.usermapper.UserRowMapper;
import io.getarrays.securecapita.repository.RoleRepository;
import io.getarrays.securecapita.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

import static io.getarrays.securecapita.enumeration.RoleType.ROLE_USER;
import static io.getarrays.securecapita.enumeration.VerificationType.ACCOUNT;
import static io.getarrays.securecapita.query.UserQuery.*;
import static io.getarrays.securecapita.utils.SmsUtils.sendSMS;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.time.DateUtils.addDays;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {
    //    public static final String DATE_FORMAT = "dd MMMM yyyy";
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public User create(User user) {
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0)
            throw new Apiexception("Email already in use, Please use a different email and try again.");
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(requireNonNull(holder.getKey()).longValue());
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            String verificationURl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, of("userId", user.getId(), "url", verificationURl));
            //Send email to user with verifications URL
//            emailService.sendVerificationUrl(user.getFirstName(),user.getEmail(),verificationURl,ACCOUNT.getType());
            user.setEnabled(false);
            user.setNotLocked(true);
            return user;
        } catch (EmptyResultDataAccessException exception) {
            throw new Apiexception("No role found by name:" + ROLE_USER.name());
        } catch (Exception exception) {
            throw new Apiexception("An error occurred. Please try again");
        }
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize", pageSize);
        params.put("offset", (page - 1) * pageSize);

        UserRowMapper userRowMapper = new UserRowMapper(); // Создаем экземпляр UserRowMapper
        return jdbc.query(SELECT_ALL_USERS_QUERY, params, (rs, rowNum) -> {
            User user = userRowMapper.mapRow(rs, rowNum); // Применяем UserRowMapper
            return user;
        });
    }

    public User get(Long id) {
        String query = "SELECT * FROM users WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        return jdbc.queryForObject(query, params, new UserRowMapper());
    }

    @Override
    public User update(User updateRequest) {
        return updateRequest;
    }


    @Override
    public Boolean delete(Long id) {
        Map<String, Object> paramMap = Collections.singletonMap("id", id);
        int rowsAffected = jdbc.update(DELETE_USER_BY_ID_QUERY, paramMap);
        return rowsAffected > 0;
    }

    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource().addValue("firstName", user.getFirstName()).addValue("lastName", user.getLastName()).addValue("email", user.getEmail()).addValue("password", encoder.encode(user.getPassword()));
    }

    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, of("email", email), Integer.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if (user == null) {
            log.info("User not found in database: {}", email);
            throw new UsernameNotFoundException("User not found in database: " + email);
        } else {
            log.info("User found in database: {}", email);
            return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()).getPermission());
        }
    }


    @Override
    public User getUserByEmail(String email) {
        try {
            return jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email", email), new UserRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new Apiexception("No user found by email:" + email);
        } catch (DataAccessException exception) {
            log.error(exception.getMessage());
            throw new Apiexception("An error occurred. Please try again");
        }
    }

    @Override
    public void sendVerificationCode(UserDTO userDTO) {
        String expirationDate = DateFormatUtils.format(addDays(new Date(), 1), DATE_FORMAT);
        String verificationCode = RandomStringUtils.randomAlphabetic(8).toUpperCase();

        try {
            jdbc.update(DELETE_VERIFICATION_CODE_BY_USER_ID, of("id", userDTO.getId()));
            jdbc.update(INSERT_VERIFICATION_CODE_QUERY, of("userId", userDTO.getId(),"code",verificationCode,"expiration_date",expirationDate));
            sendSMS(userDTO.getPhone(), "From: SecureCapita \nVerification code\n" + verificationCode);

        }catch (Exception exception) {
            log.error(exception.getMessage());
            throw new Apiexception("An error occurred. Please try again");
        }
    }

}

