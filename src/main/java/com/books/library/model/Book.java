
package com.books.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String description;
    private boolean available;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public int getCopiesAvailable() {
//        return copiesAvailable;
//    }
//
//    public void setCopiesAvailable(int copiesAvailable) {
//        this.copiesAvailable = copiesAvailable;
//    }

    public Book() {
    }

    public Book(String title, String author, String description, boolean available) {

        this.title = title;
        this.author = author;
        this.description = description;
//        this.copiesAvailable = copiesAvailable;
        this.available = available;
    }



//    private int copiesAvailable;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

     // поле для отслеживания доступности книги
}
    