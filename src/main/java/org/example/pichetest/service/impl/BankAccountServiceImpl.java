package org.example.pichetest.service.impl;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.example.pichetest.exception.BankAccountNotFoundException;
import org.example.pichetest.model.BankAccount;
import org.example.pichetest.model.User;
import org.example.pichetest.repository.BankAccountRepository;
import org.example.pichetest.service.BankAccountService;
import org.springframework.stereotype.Service;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public List<BankAccount> findByUser(User user) {
        return bankAccountRepository.getBankAccountByUser(user);
    }

    @Override
    public BankAccount createNew(BankAccount bankAccount) {
        Random random = new Random();
        bankAccount.setAccountNumber(random.nextLong() & Long.MAX_VALUE);

        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount getDetails(User user, Long accountNumber) {
        return findByUser(user).stream()
            .filter(bankAccount -> bankAccount.getAccountNumber().equals(accountNumber))
            .findFirst()
            .orElseThrow(() -> new BankAccountNotFoundException(format("Bank account with %d number was not found",
                accountNumber)));
    }

    @Override
    public Optional<BankAccount> findAccountByAccountNumber(Long accountNumber) {
        return bankAccountRepository.getBankAccountByAccountNumber(accountNumber);
    }
}
