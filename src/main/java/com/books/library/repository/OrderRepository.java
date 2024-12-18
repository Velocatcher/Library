
package com.books.library.repository;

import com.books.library.model.BookOrder;
import com.books.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<BookOrder, Long> {
    // Используем user вместо userId, т.к. это объект
    List<BookOrder> findByUser(User user);
    List<BookOrder> findByUserAndReturnedFalse(User user);
}