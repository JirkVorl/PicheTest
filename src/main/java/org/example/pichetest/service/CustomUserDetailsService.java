package org.example.pichetest.service;

import static org.springframework.security.core.userdetails.User.withUsername;

import java.util.Optional;
import org.example.pichetest.model.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserBuilder userBuilder = withUsername(email);
            userBuilder.password(user.getPassword());
            userBuilder.roles("USER");
            return userBuilder.build();
        }
        throw new UsernameNotFoundException("User with email " + email + " was not found");
    }
}
