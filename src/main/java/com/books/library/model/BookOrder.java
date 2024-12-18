package com.books.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "book_order") // Указываем явное имя таблицы
public class BookOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Устанавливаем FetchType.LAZY для оптимизации запросов
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY) // Устанавливаем FetchType.LAZY для оптимизации запросов
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @Column(name = "due_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @Column(name = "returned", nullable = false)
    private boolean returned;


    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }



    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


    // Конструктор без параметров (по умолчанию)
    public BookOrder() {}

    // Конструктор с параметрами
    public BookOrder(Book book, User user, LocalDate dueDate) {
        this.book = book;
        this.user = user;
        this.dueDate = dueDate;
        this.returned = false; // По умолчанию книга считается невозвращенной
    }

    /**
     * Метод для установки ID книги.
     * Используется, когда необходимо указать только ID книги, а не сам объект.
     */
    public void setBookId(Long bookId) {
        this.book = new Book();
        this.book.setId(bookId);
    }

    /**
     * Метод для установки ID пользователя.
     * Используется, когда необходимо указать только ID пользователя, а не сам объект.
     */
    public void setUserId(Long userId) {
        this.user = new User();
        this.user.setId(userId);
    }
}
