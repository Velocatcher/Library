package com.books.library.controller;

import com.books.library.model.Book;
import com.books.library.model.Review;
import com.books.library.service.BookService;
import com.books.library.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;
    private final BookService bookService;

    public ReviewController(ReviewService reviewService, BookService bookService) {
        this.reviewService = reviewService;
        this.bookService = bookService;
    }


    @GetMapping("/bookReviews")
    public String getBookReviews(@RequestParam("bookId") Long bookId, Model model) {
        logger.info("Получение отзывов для книги с ID {}", bookId);

        Book book = bookService.getBookById(bookId);
        if (book == null) {
            logger.error("Книга с ID {} не найдена", bookId);
            throw new IllegalArgumentException("Книга с ID " + bookId + " не найдена.");
        }

        List<Review> reviews = reviewService.getReviewsByBookId(bookId);
        model.addAttribute("book", book);
        model.addAttribute("reviews", reviews);

        return "reviews"; // Название файла шаблона Thymeleaf
    }


    /**
     * Получение всех отзывов по книге.
     *
     * @param bookId идентификатор книги
     * @return список отзывов
     */
    @GetMapping("/get")
    public ResponseEntity<?> getReviews(@RequestParam Long bookId) {
        try {
            return new ResponseEntity<>(reviewService.getReviewsByBookId(bookId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Ошибка при получении отзывов: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Добавление нового отзыва.
     *
     *
     * @param bookId     идентификатор книги
     * @param rating     рейтинг книги
     * @param reviewText текст отзыва (опционально)
     * @return сообщение об успешном добавлении отзыва
     */
    @PostMapping("/add")
    public String addReview(@RequestParam("bookId") Long bookId,
                            @RequestParam(value = "rating", defaultValue = "5") double rating,
                            @RequestParam("reviewText") String reviewText) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be authenticated to add a review.");
        }

        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String userEmail = principal.getUsername();

        try {
            reviewService.addReview(userEmail, bookId, rating, reviewText);
        } catch (Exception e) {
            return "redirect:/error?message=" + e.getMessage();
        }

        return "redirect:/order/home";
    }

    /**
     * Удаление всех отзывов по книге.
     *
     * @param bookId идентификатор книги
     * @return сообщение об успешном удалении отзывов
     */


    @DeleteMapping("/delete/{bookId}")
    public void deleteReviews(@PathVariable Long bookId) {
        reviewService.deleteReviewsByBookId(bookId);
    }

//    @GetMapping("/book_list")
//    public String reviewForm(Model model) {
//        Book book = new Book();
//        book.setTitle("Тестовая книга");
//        model.addAttribute("book", book);
//        return "book_list";
//    }

    @GetMapping("/book_list")
    public String reviewForm(@RequestParam("bookId") Long bookId, Model model) {
        logger.info("Вызов метода reviewForm с параметром bookId = {}", bookId);

        Book book = bookService.getBookById(bookId);
        if (book == null) {
            logger.error("Книга с ID {} не найдена", bookId);
            throw new IllegalArgumentException("Книга с ID " + bookId + " не найдена.");
        }
        logger.info("Книга с ID {} найдена: {}", bookId, book.getTitle());
        model.addAttribute("book", book);
        return "book_list";
    }


}
