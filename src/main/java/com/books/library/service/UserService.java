
package com.books.library.service;

import com.books.library.model.User;
import com.books.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        user.setRole("USER");
        userRepository.save(user);
    }
}
    