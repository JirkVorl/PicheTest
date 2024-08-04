package org.example.pichetest.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.example.pichetest.model.BankAccount;
import org.example.pichetest.model.User;

public interface BankAccountService {

    List<BankAccount> findByUser(User user);

    BankAccount createNew(BankAccount bankAccount);

    BankAccount getDetails(User user, Long accountNumber);

    Optional<BankAccount> findAccountByAccountNumber(Long accountNumber);
}
