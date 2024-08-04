package org.example.pichetest.service;

import java.math.BigDecimal;
import java.util.Optional;
import org.example.pichetest.model.BankAccount;

public interface TransactionService {

    BankAccount deposit(BigDecimal amount, BankAccount bankAccount);

    Optional<BankAccount> transfer(BankAccount toAccount, BankAccount fromAccount, BigDecimal amount);

    BankAccount withdraw(BankAccount bankAccount, BigDecimal amount);
}
