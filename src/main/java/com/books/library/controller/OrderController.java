package com.books.library.controller;

import com.books.library.model.Book;
import com.books.library.model.BookOrder;
import com.books.library.model.User;
import com.books.library.service.BookService;
import com.books.library.service.OrderService;
import com.books.library.service.ReviewService;
import com.books.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final BookService bookService;
    private final UserService userService;
    private final ReviewService reviewService;
    @Autowired
    public OrderController(OrderService orderService, BookService bookService, UserService userService, ReviewService reviewService) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    // Главная страница пользователя
    @GetMapping("/home")
    public String homePage(Model model) {
        // Получаем текущего авторизованного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Пользователь должен быть авторизован.");
        }

        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User user = userService.findByUsername(principal.getUsername());
        if (user == null) {
            throw new IllegalStateException("Пользователь не найден в системе.");
        }

        // Получение заказов и доступных книг
        List<BookOrder> userOrders = orderService.getOrdersByUser(user);
        for (BookOrder order : userOrders) {
            // Проверяем, оставил ли пользователь отзыв на книгу
            boolean reviewExists = reviewService.userReviewListed(user.getUsername(), order.getBook().getId());
            order.setHasReview(reviewExists); // Устанавливаем свойство hasReview
        }
        List<Book> availableBooks = bookService.getAllAvailableBooks();

        // Логирование
        System.out.println("Количество доступных книг: " + availableBooks.size());

        model.addAttribute("userOrders", userOrders);
        model.addAttribute("availableBooks", availableBooks);
        return "home";
    }


    // Получение списка всех заказов пользователя
    @GetMapping("/user-orders")
    @ResponseBody
    public List<BookOrder> getUserOrders(@AuthenticationPrincipal User user) {
        return orderService.getOrdersByUser(user);
    }

    // Заказ книги

    @PostMapping("/orderBook")
    public String orderBook(@RequestParam("bookId") Long bookId, @AuthenticationPrincipal org.springframework.security.core.userdetails.User currentUser) {
        if (currentUser == null) {
            throw new IllegalStateException("Пользователь должен быть авторизован, чтобы заказать книгу.");
        }
        // Используем метод userService для получения пользователя
        User user = userService.findByUsername(currentUser.getUsername());
        if (user == null) {
            throw new IllegalStateException("Пользователь не найден в системе.");
        }

        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new IllegalStateException("Книга с ID " + bookId + " не найдена.");
        }

//        // Ищем реального пользователя через UserService
//        Optional<User> user = Optional.ofNullable(userService.findByUsername(currentUser.getUsername()));
        orderService.createOrder(user, book);
        return "redirect:/order/home";
    }


    // Возврат книги
    @PostMapping("/returnBook")
    public String returnBook(@RequestParam("orderId") Long orderId, Principal principal, Model model) {
        orderService.returnBook(orderId);

        // Обновление данных для отображения
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("userOrders", orderService.getOrdersByUser(user));
        model.addAttribute("availableBooks", bookService.getAllAvailableBooks());

        return "home"; // Обновляем представление
    }

    // Продление аренды книги
    @PostMapping("/extendLoan")
    public String extendLoan(@RequestParam Long orderId, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        if (user == null) {
            throw new IllegalStateException("Пользователь не найден.");
        }

        BookOrder order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Заказ не найден.");
        }

        if (order.isReturned()) {
            model.addAttribute("errorMessage", "Нельзя продлить аренду для возвращенного заказа.");
        } else {
            orderService.extendLoan(orderId);
        }

        // Обновить данные для представления
        model.addAttribute("userOrders", orderService.getOrdersByUser(user));
        model.addAttribute("availableBooks", bookService.getAllAvailableBooks());

        return "home";
    }

}
