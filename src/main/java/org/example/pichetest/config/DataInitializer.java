package org.example.pichetest.config;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import org.example.pichetest.model.BankAccount;
import org.example.pichetest.model.User;
import org.example.pichetest.service.BankAccountService;
import org.example.pichetest.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserService userService;
    private final BankAccountService accountService;

    public DataInitializer(UserService userService, BankAccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @PostConstruct
    public void inject() {
        User user0 = new User();
        user0.setEmail("email0");
        user0.setPassword("password0");
        User user1 = new User();
        user1.setEmail("email1");
        user1.setPassword("password1");

        userService.add(user1);
        userService.add(user0);

        BankAccount account0 = new BankAccount();
        account0.setAccountDetails("details0");
        account0.setBalance(new BigDecimal(3000));
        account0.setUser(user0);
        account0.setName("account0");

        BankAccount account1 = new BankAccount();
        account1.setAccountDetails("details1");
        account1.setBalance(new BigDecimal(4000));
        account1.setUser(user1);
        account1.setName("account1");

        accountService.createNew(account0);
        accountService.createNew(account1);
    }
}
