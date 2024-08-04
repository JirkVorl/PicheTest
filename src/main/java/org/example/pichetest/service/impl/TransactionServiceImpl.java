package org.example.pichetest.service.impl;

import java.math.BigDecimal;
import java.util.Optional;
import org.example.pichetest.exception.NotEnoughFoundsException;
import org.example.pichetest.model.BankAccount;
import org.example.pichetest.repository.BankAccountRepository;
import org.example.pichetest.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final BankAccountRepository bankAccountRepository;

    public TransactionServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public BankAccount deposit(BigDecimal amount, BankAccount bankAccount) {

        BigDecimal newBalance = bankAccount.getBalance().add(amount);
        bankAccount.setBalance(newBalance);
        return bankAccountRepository.save(bankAccount);

    }

    @Override
    public Optional<BankAccount> transfer(BankAccount toAccount, BankAccount fromAccount, BigDecimal amount) {
        withdraw(fromAccount, amount);
        deposit(amount, toAccount);
        return bankAccountRepository.getBankAccountByAccountNumber(fromAccount.getAccountNumber());
    }

    @Override
    public BankAccount withdraw(BankAccount bankAccount, BigDecimal amount) {
        if (bankAccount.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughFoundsException("Not enough funds");
        }
        BigDecimal newAmount = bankAccount.getBalance().subtract(amount);

        bankAccount.setBalance(newAmount);
        return bankAccountRepository.save(bankAccount);
    }
}
