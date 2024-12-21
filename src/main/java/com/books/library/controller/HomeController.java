package com.books.library.controller;

import com.books.library.model.Book;
import com.books.library.service.BookService;
import com.books.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private BookService bookService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/home")
    public String showHomePage(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "home";
    }

    @PostMapping("/orderBook")
    public String orderBook(@RequestParam Long bookId, @RequestParam Long userId) {
        orderService.orderBook(bookId, userId);
        return "redirect:/home";
    }

    @PostMapping("/returnBook")
    public String returnBook(@RequestParam Long orderId) {
        orderService.returnBook(orderId);
        return "redirect:/home";
    }

    @PostMapping("/extendRental")
    public String extendRental(@RequestParam Long orderId) {
        orderService.extendRental(orderId);
        return "redirect:/home";
    }
}
