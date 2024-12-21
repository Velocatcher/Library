package com.books.library.controller;

import com.books.library.model.Book;
import com.books.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookService bookService;

    /**
     * Маршрут для отображения страницы admin.html
     */
    @GetMapping
    public String showAdminPage() {
        return "admin"; // без .html, Spring ищет файл admin.html в папке /templates/
    }

    /**
     * Получение списка всех книг
     * Эндпоинт: /admin/book [GET]
     */
    @GetMapping("/book")
    @ResponseBody
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Добавление новой книги
     * Эндпоинт: /admin/book/add [POST]
     */
    @PostMapping("/book/add")
    @ResponseBody
    public Book addBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    /**
     * Удаление книги по ID
     * Эндпоинт: /admin/book/delete/{id} [DELETE]
     */
    @DeleteMapping("/book/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Книга успешно удалена");
    }

    /**
     * Обновление информации о книге
     * Эндпоинт: /admin/book/update/{id} [PUT]
     */
    @PutMapping("/book/update/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        try {
            Book savedBook = bookService.updateBook(id, updatedBook);
            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Ошибка: Книга не найдена");
        }
    }
}
