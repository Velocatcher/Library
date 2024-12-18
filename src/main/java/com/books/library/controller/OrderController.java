package com.books.library.controller;

import com.books.library.model.Book;
import com.books.library.model.BookOrder;
import com.books.library.model.User;
import com.books.library.service.BookService;
import com.books.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final BookService bookService;

    @Autowired
    public OrderController(OrderService orderService, BookService bookService) {
        this.orderService = orderService;
        this.bookService = bookService;
    }

    // Главная страница пользователя
    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal User user, Model model) {
        List<BookOrder> userOrders = orderService.getOrdersByUser(user);
        List<Book> availableBooks = bookService.getAllAvailableBooks();

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
    public String orderBook(@RequestParam("bookId") Long bookId, @AuthenticationPrincipal User user) {
        orderService.createOrder(user, bookId);
        return "redirect:/order/home";
    }

    // Возврат книги
    @PostMapping("/returnBook")
    public String returnBook(@RequestParam("orderId") Long orderId) {
        orderService.returnBook(orderId);
        return "redirect:/order/home";
    }

    // Продление аренды книги
    @PostMapping("/extendLoan")
    public String extendLoan(@RequestParam("orderId") Long orderId) {
        orderService.extendLoan(orderId);
        return "redirect:/order/home";
    }
}
