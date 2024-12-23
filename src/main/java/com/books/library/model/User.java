
package com.books.library.model;

import com.books.library.config.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String username;
    private String password;


    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    @Enumerated(EnumType.STRING) // Указывает, что роль должна быть сохранена как строка
    private Role role;
}
    