package com.books.library.service;

import com.books.library.model.Book;
import com.books.library.model.BookOrder;
import com.books.library.model.User;
import com.books.library.repository.BookRepository;
import com.books.library.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Получение всех заказов пользователя.
     * @param user Пользователь.
     * @return Список заказов пользователя.
     */
    public List<BookOrder> getOrdersByUser(User user) {
        // Получаем список заказов пользователя
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null.");
        }

        List<BookOrder> orders = orderRepository.findByUser(user);

        // Логируем размер списка и детали пользователя
        if (orders != null) {
            System.out.println("Найдено заказов для пользователя " + user.getUsername() + ": " + orders.size());
        } else {
            System.out.println("Для пользователя " + user.getUsername() + " не найдено заказов.");
        }

        return orders; // Возвращаем список
    }


    /**
     * Метод для создания нового заказа на книгу.
     * @param user Пользователь, который заказывает книгу.
     * @param book Идентификатор книги.
     */
    public void createOrder(User user, Book book) {
        if (user == null || book == null) {
            throw new IllegalArgumentException("Пользователь и книга не могут быть null.");
        }

//         Создаем новый заказ
        BookOrder order = new BookOrder();
        order.setUser(user); // Устанавливаем пользователя (убран Optional)
        order.setBook(book); // Устанавливаем книгу
        order.setOrderDate(LocalDate.now()); // Устанавливаем дату заказа
        order.setDueDate(LocalDate.now().plusDays(14)); // Устанавливаем срок аренды на 14 дней
        order.setReturned(false); // Заказ не возвращен
        // Логирование
        System.out.println("Создание заказа для пользователя: " + user.getUsername() + ", Книга: " + book.getTitle() + book.getId());
        orderRepository.save(order); // Сохраняем заказ в базе данных
        book.setAvailable(false);
        bookRepository.save(book);
    }

    /**
     * Метод для возврата книги.
     * @param orderId Идентификатор заказа.
     */
    public void returnBook(Long orderId) {
        BookOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден с идентификатором: " + orderId));
        order.setReturned(true);
        orderRepository.save(order);

        // Обновление статуса книги
        Book book = order.getBook();
        if (book != null) {
            book.setAvailable(true);
            bookRepository.save(book);
        }
    }


    /**
     * Метод для продления срока аренды книги.
     * @param orderId Идентификатор заказа.
     */
    public void extendLoan(Long orderId) {
        BookOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден с идентификатором: " + orderId));
        order.setDueDate(order.getDueDate().plusDays(7)); // Продлить аренду на 7 дней
        orderRepository.save(order);
    }

    public BookOrder getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден с идентификатором: " + orderId));
    }



    /**
     * Получить список всех активных заказов для пользователя.
     * @param user Пользователь.
     * @return Список активных заказов пользователя.
     */
    public List<BookOrder> getActiveOrders(User user) {
        return orderRepository.findByUserAndReturnedFalse(user);
    }

    /**
     * Получить список всех заказов (для администратора).
     * @return Список всех заказов.
     */
    public List<BookOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Метод для отмены заказа.
     * @param orderId Идентификатор заказа.
     */
    public void cancelOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}

