package com.books.library.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "review")
@Data
public class Review {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "date")
    @CreationTimestamp
    private Date date;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "review_text", nullable = false)
    private String reviewDescription; // Поле для хранения текста отзыва

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book; // Связь с книгой
}






//
//package com.books.library.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.util.Date;
//
//@Entity
//@Table(name = "review")
//@Data
//public class Review {
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUserEmail() {
//        return userEmail;
//    }
//
//    public void setUserEmail(String userEmail) {
//        this.userEmail = userEmail;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public double getRating() {
//        return rating;
//    }
//
//    public void setRating(double rating) {
//        this.rating = rating;
//    }
//
//    public String getReviewDescription() {
//        return reviewDescription;
//    }
//
//    public void setReviewDescription(String reviewDescription) {
//        this.reviewDescription = reviewDescription;
//    }
//
//    public Book getBook() {
//        return book;
//    }
//
//    public void setBook(Book book) {
//        this.book = book;
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "user_email", nullable = false)
//    private String userEmail;
//
//    @Column(name = "date")
//    @CreationTimestamp
//    private Date date;
//
//    @Column(name = "rating", nullable = false)
//    private double rating;
//
//    @Column(name = "review_text")
//    private String reviewDescription;
//
//    @ManyToOne
//    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
//    private Book book; // Заменяем Long bookId на объект Book
//}
