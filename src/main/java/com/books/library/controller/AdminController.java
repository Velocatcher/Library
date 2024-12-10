package com.books.library.controller;

import com.books.library.model.Book;
import com.books.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private BookService bookService;

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "admin";
    }

    @PostMapping("/addBook")
    public String addBook(Book book) {
        bookService.saveBook(book);
        return "redirect:/admin";
    }

    @PostMapping("/editBook")
    public String editBook(Book book) {
        bookService.updateBook(book);
        return "redirect:/admin";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam Long bookId) {
        bookService.deleteBook(bookId);
        return "redirect:/admin";
    }
}
