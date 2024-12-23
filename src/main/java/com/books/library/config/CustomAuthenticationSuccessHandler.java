package com.books.library.config;

import com.books.library.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // Убедитесь, что объект пользователя правильно установлен
        User user = (User) authentication.getPrincipal();
        if (user.getRole().equals("ROLE_ADMIN")) {
            response.sendRedirect("/admin");
        } else if (user.getRole().equals("ROLE_USER")) {
            response.sendRedirect("/order/home");
        } else {
            response.sendRedirect("/index");
        }
    }
}
