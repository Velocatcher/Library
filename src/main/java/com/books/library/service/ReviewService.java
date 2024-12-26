package com.books.library.service;

import com.books.library.model.Book;
import com.books.library.model.Review;
import com.books.library.repository.BookRepository;
import com.books.library.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository; // Репозиторий для работы с книгами
    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Добавить новый отзыв.
     *
     * @param userEmail     электронная почта пользователя
     * @param bookId        идентификатор книги
     * @param rating        оценка
     * @param reviewText    текст отзыва
     */
    public void addReview(String userEmail, Long bookId, double rating, String reviewText) throws Exception {
        if (reviewRepository.findByUserEmailAndBookId(userEmail, bookId) != null) {
            throw new Exception("Повторная попытка оставить отзыв на эту книгу.");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new Exception("Книга не найдена."));

        Review review = new Review();
        review.setBook(book); // Устанавливаем объект Book
        review.setUserEmail(userEmail);
        review.setRating(rating);
        review.setReviewDescription(reviewText);
        review.setDate(new Date());
        reviewRepository.save(review);
    }

    /**
     * Проверяет, оставил ли пользователь отзыв для указанной книги.
     *
     * @param userEmail адрес электронной почты пользователя
     * @param bookId    идентификатор книги
     * @return true, если отзыв существует, иначе false
     */
    public boolean userReviewListed(String userEmail, Long bookId) {
        return reviewRepository.findByUserEmailAndBookId(userEmail, bookId) != null;
    }


    /**
     * Получить список отзывов для книги.
     *
     * @param bookId идентификатор книги
     * @return список отзывов
     */
    public List<Review> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    /**
     * Удалить все отзывы для книги.
     *
     * @param bookId идентификатор книги
     */
    public void deleteReviewsByBookId(Long bookId) {
        reviewRepository.deleteAllByBookId(bookId);
    }
}
