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
        return orderRepository.findByUser(user);
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







//package com.books.library.service;
//
//import com.books.library.model.Book;
//import com.books.library.model.BookOrder;
//import com.books.library.model.User;
//import com.books.library.repository.BookRepository;
//import com.books.library.repository.OrderRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class OrderService {
//
//    private OrderRepository orderRepository;
//
//    private BookRepository bookRepository;
//
//    @Autowired
//    public OrderService(OrderRepository orderRepository, BookRepository bookRepository) {
//        this.orderRepository = orderRepository;
//        this.bookRepository = bookRepository;
//    }
//
//    /**
//     * Получение всех заказов пользователя.
//     * @param user Пользователь.
//     * @return Список заказов пользователя.
//     */
//    public List<BookOrder> getOrdersByUser(User user) {
//        return orderRepository.findByUser(user);
//    }
//
//    /**
//     * Метод для создания нового заказа на книгу.
//     * @param user Пользователь, который заказывает книгу.
//     * @param bookId Идентификатор книги.
//     */
//    public void createOrder(Optional<User> user, Long bookId) {
//        if (user == null) {
//            throw new IllegalArgumentException("Пользователь не найден. Невозможно создать заказ без пользователя.");
//        }
//
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new IllegalArgumentException("Книга не найдена с идентификатором: " + bookId));
//
//        BookOrder order = new BookOrder();
//
//        order.setUser(user);
//        order.setBook(book);
//        order.setOrderDate(LocalDate.now());
//        order.setDueDate(LocalDate.now().plusDays(14));
//        order.setReturned(false);
//
//        // Логирование
//        System.out.println("Создание заказа для пользователя: " + user.getUsername() + ", Книга: " + book.getTitle());
//
//        orderRepository.save(order);
//    }
//
//
////    public void createOrder(User user, Long bookId) {
////        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Книга не найдена"));
////        BookOrder order = new BookOrder();
////        order.setBookId(bookId);
////        order.setUser(user);
////        order.setBook(book);
////        order.setOrderDate(LocalDate.now());
////        order.setDueDate(LocalDate.now().plusDays(14)); // Установить срок аренды на 14 дней
////        order.setReturned(false);
////        orderRepository.save(order);
////    }
//
//    /**
//     * Метод для возврата книги.
//     * @param orderId Идентификатор заказа.
//     */
//    public void returnBook(Long orderId) {
//        BookOrder order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
//        order.setReturned(true);
//        orderRepository.save(order);
//    }
//
//    /**
//     * Метод для продления срока аренды книги.
//     * @param orderId Идентификатор заказа.
//     */
//    public void extendLoan(Long orderId) {
//        BookOrder order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
//        order.setDueDate(order.getDueDate().plusDays(7)); // Продлить аренду на 7 дней
//        orderRepository.save(order);
//    }
//
//    /**
//     * Получить список всех активных заказов для пользователя.
//     * @param user Пользователь.
//     * @return Список активных заказов пользователя.
//     */
//    public List<BookOrder> getActiveOrders(User user) {
//        return orderRepository.findByUserAndReturnedFalse(user);
//    }
//
//    /**
//     * Получить список всех заказов (для администратора).
//     * @return Список всех заказов.
//     */
//    public List<BookOrder> getAllOrders() {
//        return orderRepository.findAll();
//    }
//
//    /**
//     * Метод для отмены заказа.
//     * @param orderId Идентификатор заказа.
//     */
//    public void cancelOrder(Long orderId) {
//        orderRepository.deleteById(orderId);
//    }
//}
//





//package com.books.library.service;
//
//import com.books.library.model.BookOrder;
//import com.books.library.repository.OrderRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.time.LocalDate;
//
//@Service
//public class OrderService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    /**
//     * Метод для заказа книги.
//     * @param bookId Идентификатор книги.
//     * @param userId Идентификатор пользователя.
//     */
//    public void orderBook(Long bookId, Long userId) {
//        BookOrder order = new BookOrder();
//        order.setBookId(bookId);
//        order.setUserId(userId);
//        order.setDueDate(LocalDate.now().plusDays(14)); // Срок аренды 14 дней
//        order.setReturned(false);
//        orderRepository.save(order);
//    }
//
//    /**
//     * Метод для возврата книги.
//     * @param orderId Идентификатор заказа.
//     */
//    public void returnBook(Long orderId) {
//        BookOrder order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
//        order.setReturned(true);
//        orderRepository.save(order);
//    }
//
//    /**
//     * Метод для продления срока аренды книги.
//     * @param orderId Идентификатор заказа.
//     */
//    public void extendRental(Long orderId) {
//        BookOrder order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
//        order.setDueDate(order.getDueDate().plusDays(7)); // Продление срока на 7 дней
//        orderRepository.save(order);
//    }
//}
