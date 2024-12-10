package com.books.library.service;

import com.books.library.model.BookOrder;
import com.books.library.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Метод для заказа книги.
     * @param bookId Идентификатор книги.
     * @param userId Идентификатор пользователя.
     */
    public void orderBook(Long bookId, Long userId) {
        BookOrder order = new BookOrder();
        order.setBookId(bookId);
        order.setUserId(userId);
        order.setDueDate(LocalDate.now().plusDays(14)); // Срок аренды 14 дней
        order.setReturned(false);
        orderRepository.save(order);
    }

    /**
     * Метод для возврата книги.
     * @param orderId Идентификатор заказа.
     */
    public void returnBook(Long orderId) {
        BookOrder order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setReturned(true);
        orderRepository.save(order);
    }

    /**
     * Метод для продления срока аренды книги.
     * @param orderId Идентификатор заказа.
     */
    public void extendRental(Long orderId) {
        BookOrder order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setDueDate(order.getDueDate().plusDays(7)); // Продление срока на 7 дней
        orderRepository.save(order);
    }
}
