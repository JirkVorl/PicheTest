package org.example.pichetest.service;

import java.util.Optional;
import org.example.pichetest.model.User;

public interface UserService {

    User add(User user);

    Optional<User> get(Long id);

    Optional<User> findByEmail(String email);
}
