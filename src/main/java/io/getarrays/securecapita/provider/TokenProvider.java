//package io.getarrays.securecapita.provider;
//
//import io.getarrays.securecapita.domain.UserPrincipal;
//import io.getarrays.securecapita.service.UserService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//
//import static java.lang.System.currentTimeMillis;
//import static java.util.Arrays.stream;
//import static java.util.stream.Collectors.toList;
//import static org.springframework.security.config.Elements.JWT;
//
///**
// * Description of TokenProvider
// *
// * @author mac
// * @version 1.0
// * @since 2/19/24
// **/
//
//@Component
//@RequiredArgsConstructor
//public class TokenProvider {
//    @Value("${jwt.secret}")
//    private String secret;
//    private final UserService userService;
//
//    public String createAccessToken(UserPrincipal userPrincipal) {
//        return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(CUSTOMER_MANAGEMENT_SERVICE)
//                .withIssuedAt(new Date()).withSubject(String.valueOf(userPrincipal.getUser().getId())).withArrayClaim(AUTHORITIES, getClaimsFromUser(userPrincipal))
//                .withExpiresAt(new Date(currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
//                .sign(HMAC512(secret.getBytes()));
//
//    }
//
//    public String createRefreshToken(UserPrincipal userPrincipal) {
//        return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(CUSTOMER_MANAGEMENT_SERVICE)
//                .withIssuedAt(new Date()).withSubject(String.valueOf(userPrincipal.getUser().getId()))
//                .withExpiresAt(new Date(currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
//                .sign(HMAC512(secret.getBytes()));
//
//    }
//
//    public Long getSubject(String token, HttpServletRequest request) {
//        try {
//            return Long.valueOf(getJWTVerifier().verify(token).getSubject());
//        } catch (TokenExpiredException exception) {
//            request.setAttribute("expiredMessage", exception.getMessage());
//            throw exception;
//        } catch (InvalidClaimException exception) {
//            request.setAttribute("invalidClaim", exception.getMessage());
//            throw exception;
//        } catch (Exception exception) {
//            throw exception;
//        }
//    }
//
//    public List<GrantedAuthority> getAuthorities(String token) {
//        String[] claims = getClaimsFromToken(token);
//        return stream(claims).map(SimpleGrantedAuthority::new).collect(toList());
//    }
//
//    public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities, HttpServletRequest request) {
//        UsernamePasswordAuthenticationToken userPasswordAuthToken = new UsernamePasswordAuthenticationToken(userService.getUserById(userId), null, authorities);
//        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        return userPasswordAuthToken;
//    }
//
//    public boolean isTokenValid(Long userId, String token) {
//        JWTVerifier verifier = getJWTVerifier();
//        return !Objects.isNull(userId) && !isTokenExpired(verifier, token);
//    }
//
//    private boolean isTokenExpired(JWTVerifier verifier, String token) {
//        Date expiration = verifier.verify(token).getExpiresAt();
//        return expiration.before(new Date());
//    }
//
//    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
//        return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
//    }
//
//    private String[] getClaimsFromToken(String token) {
//        JWTVerifier verifier = getJWTVerifier();
//        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
//    }
//
//    private JWTVerifier getJWTVerifier() {
//        JWTVerifier verifier;
//        try {
//            Algorithm algorithm = HMAC512(secret);
//            verifier = JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
//        }catch (JWTVerificationException exception) { throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED); }
//        return verifier;
//    }
//}
