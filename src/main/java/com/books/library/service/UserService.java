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
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    /**
     * Проверяет, существует ли пользователь с данным именем пользователя.
     * @param username Имя пользователя.
     * @return true, если пользователь с таким именем существует, иначе false.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Преобразуем пользователя из базы данных в UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // пароль уже зашифрован
                .roles(user.getRole().name()) // преобразование роли Role.USER в "USER"
                .build();
    }

    /**
     * Сохраняет нового пользователя с зашифрованным паролем.
     * @param user Новый пользователь.
     */
    public void saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with this username already exists.");
        }

        if (!"admin".equals(user.getUsername())) {
            user.setRole(Role.USER);
        }

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

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
     * При обновлении пароля, пароль будет зашифрован.
     * @param id Идентификатор пользователя, которого нужно обновить.
     * @param updatedUser Объект с обновленными данными.
     * @return Обновленный пользователь.
     */
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            // Шифруем пароль перед обновлением
            String encryptedPassword = passwordEncoder.encode(updatedUser.getPassword());
            existingUser.setPassword(encryptedPassword);
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
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }
}
