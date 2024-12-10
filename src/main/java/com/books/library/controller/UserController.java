package com.books.library.controller;

import com.books.library.model.User;
import com.books.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        try {
            userService.saveUser(user);
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

//    @GetMapping("/home")
//    public String showHomePage() {
//        return "home";
//    }

//    @GetMapping("/admin")
//    public String showAdminPage() {
//        return "admin";
//    }

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
//    /**
//     * Отображает страницу регистрации.
//     */
//    @GetMapping("/register")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new User()); // передаем пустого пользователя на форму
//        return "register";
//    }
//
//    /**
//     * Обрабатывает форму регистрации нового пользователя.
//     */
//    @PostMapping("/register")
//    public String registerUser(User user, Model model) {
//        try {
//            userService.saveUser(user);
//            model.addAttribute("successMessage", "User registered successfully!");
//            return "redirect:/home";
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Error during registration: " + e.getMessage());
//            return "register";
//        }
//    }
//
//    /**
//     * Отображает страницу авторизации.
//     */
//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "login";
//    }
//
//    /**
//     * Получает информацию о пользователе по ID.
//     */
//    @GetMapping("/user/{id}")
//    @ResponseBody
//    public User getUserById(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }
//
//    /**
//     * Получает список всех пользователей.
//     */
//    @GetMapping("/users")
//    @ResponseBody
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    /**
//     * Обновляет информацию о пользователе по ID.
//     */
//    @PutMapping("/user/{id}")
//    @ResponseBody
//    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
//        return userService.updateUser(id, updatedUser);
//    }
//
//    /**
//     * Удаляет пользователя по его ID.
//     */
//    @DeleteMapping("/user/{id}")
//    @ResponseBody
//    public String deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return "User with ID " + id + " has been deleted successfully";
//    }
//}
