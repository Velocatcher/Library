package com.books.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Отображает главную страницу.
     */
    @GetMapping("/")
    public String home() {
        return "index"; // Загрузится файл src/main/resources/templates/index.html
    }
}
