package io.getarrays.securecapita.query;

public class UserQuery {
    public static final String
            INSERT_USER_QUERY =
            "INSERT INTO Users (first_name, last_name, email, password) VALUES(:firstName, :lastName, :email, :password)";
    public static final String
            COUNT_USER_EMAIL_QUERY =
            "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String
            INSERT_ACCOUNT_VERIFICATION_URL_QUERY =
            "INSERT INTO AccountVerifications (user_id, url) VALUES (:userId, :url)";
    public static final String
            SELECT_ALL_USERS_QUERY =
            "SELECT * FROM Users LIMIT :pageSize OFFSET :offset";
    public static final String
            SELECT_ONE_USER_QUERY =
            "SELECT * FROM Users WHERE id = :id";
    public static final String UPDATE_USER_QUERY =
            "UPDATE users " +
                    "SET " +
                    "  first_name = ?, " +
                    "  last_name = ?, " +
                    "  email = ?, " +
                    "  password = ?, " +
                    "  address = ?, " +
                    "  phone = ?, " +
                    "  title = ?, " +
                    "  bio = ?, " +
                    "  image_url = ?, " +
                    "  enabled = ?, " +
                    "  is_not_locked = ?, " +
                    "  is_using_mfa = ?, " +
                    "  created_at = ? " +
                    "WHERE " +
                    "  id = ?";

    public static final String
            DELETE_USER_BY_ID_QUERY = "DELETE FROM Users WHERE id = :id";

    public static final String
            SELECT_USER_BY_EMAIL_QUERY = "SELECT * FROM Users WHERE email = :email";

}
