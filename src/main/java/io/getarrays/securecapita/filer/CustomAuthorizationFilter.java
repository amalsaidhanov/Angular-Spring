//package io.getarrays.securecapita.filer;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Map;
//
///**
// * Description of CustomAuthorizationFilter
// *
// * @author mac
// * @version 1.0
// * @since 2/18/24
// **/
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class CustomAuthorizationFilter extends OncePerRequestFilter {
//    private final TokenProvider tokenProvideR;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            Map<String, String> values = getRequestValue(request);
//            String token =getToken(request);
//            if (tokenProvideR.)
//
//
//        } catch (Exception e) {
//
//        }
//    }
//
//    private String getToken(HttpServletRequest request) {
//    }
//
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return false;
//    }
//
//    protected Map<String, String> getRequestValue(HttpServletRequest Request) {
//        return null;
//    }
//}
