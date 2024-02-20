package io.getarrays.securecapita.filter;

import jakarta.servlet.*;

import java.io.IOException;

/**
 * Description of CustomerFilter
 *
 * @author mac
 * @version 1.0
 * @since 2/19/24
 **/
public class CustomerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("asdasdasd");
    }
}
