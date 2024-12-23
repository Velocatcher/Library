package com.books.library.controller;

import com.books.library.model.Book;
import com.books.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;


    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/add")
    public Book addBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Книга успешно удалена");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book savedBook = bookService.updateBook(id, updatedBook);
        return ResponseEntity.ok(savedBook);
    }
}
