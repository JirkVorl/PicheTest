package org.example.pichetest.repository;

import java.util.List;
import java.util.Optional;
import org.example.pichetest.model.BankAccount;
import org.example.pichetest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> getBankAccountByUser(User user);

    Optional<BankAccount> getBankAccountByAccountNumber(Long accountNumber);
}
