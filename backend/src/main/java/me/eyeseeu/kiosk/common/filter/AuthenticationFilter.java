package me.eyeseeu.kiosk.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpMethod;

@WebFilter(filterName = "authenticationFilter", urlPatterns = "/api/*")
public class AuthenticationFilter implements Filter {

    private static final List<String> WHITE_LIST = Arrays.asList(
        "/api/user/login",
        "/api/user/signup",
        "/api/user/logout"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (HttpMethod.OPTIONS.matches(httpRequest.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = httpRequest.getRequestURI();
        if (WHITE_LIST.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        Object memberId = httpRequest.getSession().getAttribute("memberId");

        if (memberId == null) {
            httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");

            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"message\": \"세션이 유효하지 않습니다.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
