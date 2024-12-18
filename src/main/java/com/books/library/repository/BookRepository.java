
package com.books.library.repository;

import com.books.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Метод для получения всех доступных книг.
     * Предполагается, что у модели Book есть поле "available" (boolean).
     *
     * @return Список доступных книг.
     */
    List<Book> findByAvailableTrue();
}

    