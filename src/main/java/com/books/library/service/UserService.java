package com.books.library.service;

import com.books.library.config.Role;
import com.books.library.model.User;
import com.books.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class UserService {

//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Сохраняет нового пользователя.
     */
    public void saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with this username already exists.");
        }

        if (!"admin".equals(user.getUsername())) {
            user.setRole(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

//    public void saveUser(User user) {
//        if (userRepository.existsByUsername(user.getUsername())) {
//            throw new IllegalArgumentException("User with this username already exists.");
//        }
//
//        if (user.getRole() == null) {
//            user.setRole(Role.USER); // Роль по умолчанию — USER
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword())); // Шифрование пароля
//        userRepository.save(user);
//    }

    /**
     * Получает пользователя по его id.
     * @param id Идентификатор пользователя.
     * @return Найденный пользователь или исключение, если пользователь не найден.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
    }

    /**
     * Получает всех пользователей из базы данных.
     * @return Список всех пользователей.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Обновляет существующего пользователя по id.
     * @param id Идентификатор пользователя, которого нужно обновить.
     * @param updatedUser Объект с обновленными данными.
     * @return Обновленный пользователь.
     */
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        return userRepository.save(existingUser);
    }

    /**
     * Удаляет пользователя по его id.
     * @param id Идентификатор пользователя, которого нужно удалить.
     */
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.deleteById(id);
    }
}
