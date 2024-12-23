package com.books.library.service;

import com.books.library.config.Role;
import com.books.library.model.User;
import com.books.library.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Загрузка пользователя по имени для Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Optional<User>> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
//        Optional<User> user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Преобразуем пользователя из базы данных в объект UserDetails для Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // пароль уже зашифрован
                .roles(user.getRole().name()) // преобразование роли Role.USER в "USER"
                .build();
    }

    /**
     * Метод для поиска пользователя по имени пользователя
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Пользователь с именем " + username + " не найден."));
    }

    public String saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with this username already exists.");
        }

        if (!"admin".equals(user.getUsername())) {
            user.setRole(Role.USER);
        }
        // Сохраняем исходный пароль перед шифрованием
        String rawPassword = user.getPassword();

        // Шифруем пароль перед сохранением
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);

        // Возвращаем сырой пароль, чтобы он мог быть использован в UserController
        return rawPassword;

    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(updatedUser.getPassword());
            existingUser.setPassword(encryptedPassword);
        }

        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }
}
