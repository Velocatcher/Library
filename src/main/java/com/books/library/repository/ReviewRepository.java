package com.books.library.repository;

import com.books.library.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Найти все отзывы по идентификатору книги.
     *
     * @param bookId идентификатор книги
     * @return список отзывов
     */
    List<Review> findByBookId(Long bookId);

    /**
     * Найти отзыв пользователя для определенной книги.
     *
     * @param userEmail электронная почта пользователя
     * @param bookId    идентификатор книги
     * @return отзыв
     */
    Review findByUserEmailAndBookId(String userEmail, Long bookId);

    /**
     * Удалить все отзывы по идентификатору книги.
     *
     * @param bookId идентификатор книги
     */
    @Modifying
    @Query("DELETE FROM Review r WHERE r.book.id = :bookId")
    void deleteAllByBookId(@Param("bookId") Long bookId);
}
