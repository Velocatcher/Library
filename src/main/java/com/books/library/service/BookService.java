package com.books.library.service;

import com.books.library.model.Book;
import com.books.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Получение всех доступных для заказа книг.
     * Предполагается, что у Book есть поле "available" (boolean), которое указывает, доступна ли книга.
     *
     * @return Список доступных книг.
     */
    public List<Book> getAllAvailableBooks() {
        return bookRepository.findByAvailableTrue();
    }

    /**
     * Получение книги по идентификатору.
     * @param id Идентификатор книги.
     * @return Книга.
     */
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Книга с идентификатором " + id + " не найдена."));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book saveBook(Book book) {
        bookRepository.save(book);
        return book;
    }

    public Book updateBook(Long id, Book book) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setDescription(book.getDescription());
        bookRepository.save(existingBook);
        return existingBook;
    }



//    public Book updateBook(Long id, Book book) {
//        bookRepository.save(book);
//        return book;
//    }

    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }
}
