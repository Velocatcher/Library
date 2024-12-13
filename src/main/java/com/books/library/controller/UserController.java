package com.books.library.controller;

import com.books.library.model.User;
import com.books.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        try {
            // Получаем сырой пароль до шифрования
            String rawPassword = user.getPassword();

            // Сохраняем пользователя (пароль будет зашифрован внутри UserService)
            userService.saveUser(user);

            // Аутентификация после регистрации
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), rawPassword);

            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authToken));

            model.addAttribute("successMessage", "User registered successfully!");
            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error during registration: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }


    @GetMapping("/user/{id}")
    @ResponseBody
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/user/{id}")
    @ResponseBody
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/user/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User with ID " + id + " has been deleted successfully";
    }
}




//package com.books.library.controller;
//
//import com.books.library.model.User;
//import com.books.library.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @GetMapping("/register")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new User());
//        return "register";
//    }
//
//    @PostMapping("/register")
//    public String registerUser(User user, Model model) {
//        try {
//            userService.saveUser(user);
//
//            // Автоматическая аутентификация после регистрации
////            UsernamePasswordAuthenticationToken authToken =
////                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//            UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//
//            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authToken));
//
//            model.addAttribute("successMessage", "User registered successfully!");
//            return "redirect:/home";
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Error during registration: " + e.getMessage());
//            return "register";
//        }
//    }
//
//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "login";
//    }
//
//
//    @GetMapping("/user/{id}")
//    @ResponseBody
//    public User getUserById(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }
//
//    @GetMapping("/users")
//    @ResponseBody
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @PutMapping("/user/{id}")
//    @ResponseBody
//    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
//        return userService.updateUser(id, updatedUser);
//    }
//
//    @DeleteMapping("/user/{id}")
//    @ResponseBody
//    public String deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return "User with ID " + id + " has been deleted successfully";
//    }
//}
//
