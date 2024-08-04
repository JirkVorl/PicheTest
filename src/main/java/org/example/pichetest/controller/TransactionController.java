package org.example.pichetest.controller;

import static java.lang.String.format;

import java.math.BigDecimal;
import java.util.List;
import org.example.pichetest.dto.request.TransferRequestDto;
import org.example.pichetest.dto.request.WithdrawRequestDto;
import org.example.pichetest.dto.response.BankAccountResponseDto;
import org.example.pichetest.exception.BankAccountNotFoundException;
import org.example.pichetest.model.BankAccount;
import org.example.pichetest.model.User;
import org.example.pichetest.service.BankAccountService;
import org.example.pichetest.service.TransactionService;
import org.example.pichetest.service.UserService;
import org.example.pichetest.service.mapper.ResponseDtoMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final BankAccountService bankAccountService;
    private final ResponseDtoMapper<BankAccountResponseDto, BankAccount> bankAccountMapper;
    private final UserService userService;
    private final TransactionService transactionService;

    public TransactionController(BankAccountService bankAccountService,
                                 ResponseDtoMapper<BankAccountResponseDto, BankAccount> bankAccountMapper,
                                 UserService userService, TransactionService transactionService) {
        this.bankAccountService = bankAccountService;
        this.bankAccountMapper = bankAccountMapper;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @PostMapping("/withdraw")
    public BankAccountResponseDto withdraw(Authentication auth, @RequestBody WithdrawRequestDto requestDto) {
        BankAccount userAccount = getAccountsByAuth(auth).stream()
            .filter(bankAccount -> bankAccount.getAccountNumber().equals(requestDto.getAccountNumber()))
            .findFirst()
            .orElseThrow(() -> new BankAccountNotFoundException(format("Bank account with %d number was not found",
                requestDto.getAccountNumber())));

        return bankAccountMapper.mapToDto(transactionService.withdraw(userAccount, requestDto.getAmount()));
    }

    @PostMapping("/deposit")
    public Boolean deposit(@RequestParam(value = "accountNumber") Long accountNumber,
                           @RequestParam(value = "value") BigDecimal value) {
        BankAccount account = bankAccountService
            .findAccountByAccountNumber(accountNumber).orElseThrow(() ->
                new BankAccountNotFoundException(format("Bank account with %d number was not found", accountNumber)));

        return account.getBalance().add(value).equals(transactionService.deposit(value, account).getBalance());
    }

    @PostMapping("/transfer")
    public BankAccountResponseDto transfer(Authentication auth, @RequestBody TransferRequestDto transferRequestDto) {

        BankAccount fromAccount = getAccountsByAuth(auth).stream()
            .filter(bankAccount -> bankAccount.getAccountNumber()
                .equals(transferRequestDto.getFromAccount().getAccountNumber()))
            .findFirst()
            .orElseThrow(() -> new BankAccountNotFoundException(format("Bank account with %d number was not found",
                transferRequestDto.getFromAccount().getAccountNumber())));

        BankAccount toAccount =
            bankAccountService.findAccountByAccountNumber(transferRequestDto.getToAccount().getAccountNumber())
                .orElseThrow(() -> new BankAccountNotFoundException(format("Bank account with %d number was not found",
                    transferRequestDto.getFromAccount().getAccountNumber())));

        return bankAccountMapper.mapToDto(transactionService.transfer(toAccount, fromAccount,
                transferRequestDto.getFromAccount().getAmount())
            .get());
    }

    private List<BankAccount> getAccountsByAuth(Authentication authentication) {
        String email = authentication.getName();

        User user = userService.findByEmail(email).orElseThrow(() ->
            new UsernameNotFoundException("User with email " + email + " not found"));

        return bankAccountService.findByUser(user);
    }
}
