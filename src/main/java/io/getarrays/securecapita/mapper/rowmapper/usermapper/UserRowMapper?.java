//package io.getarrays.securecapita.mapper.rowmapper.usermapper;
//
//import io.getarrays.securecapita.domain.User;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@Component
//public class UserRowMapper implements RowMapper<User> {
//    @Override
//    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//        User user = new User();
//        user.setId(rs.getLong("id"));
//        user.setFirstName(rs.getString("first_name"));
//        user.setLastName(rs.getString("last_name"));
//        user.setEmail(rs.getString("email"));
//        user.setPassword(rs.getString("password"));
//        user.setAddress(rs.getString("address"));
//        user.setPhone(rs.getString("phone"));
//        user.setTitle(rs.getString("title"));
//        user.setBio(rs.getString("bio"));
//        user.setImageUrl(rs.getString("image_url"));
//        user.setEnabled(rs.getBoolean("enabled"));
//        user.setNotLocked(rs.getBoolean("non_locked"));
//        user.setUsingMfa(rs.getBoolean("using_mfa"));
//        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
//        return user;
//    }
//}
