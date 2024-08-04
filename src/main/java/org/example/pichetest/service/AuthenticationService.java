package org.example.pichetest.service;

import org.example.pichetest.model.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    User register(String email, String password);
}
