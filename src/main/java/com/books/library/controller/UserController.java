package com.books.library.controller;

import com.books.library.model.User;
import com.books.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Отображает страницу регистрации.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // передаем пустого пользователя на форму
        return "register";
    }

    /**
     * Обрабатывает форму регистрации нового пользователя.
     */
    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        try {
            userService.saveUser(user);
            model.addAttribute("successMessage", "User registered successfully!");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error during registration: " + e.getMessage());
            return "register";
        }
    }
}
