package org.example.pichetest.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.example.pichetest.dto.request.BankAccountRequestDto;
import org.example.pichetest.dto.response.BankAccountResponseDto;
import org.example.pichetest.model.BankAccount;
import org.example.pichetest.model.User;
import org.example.pichetest.service.BankAccountService;
import org.example.pichetest.service.UserService;
import org.example.pichetest.service.mapper.BankAccountMapper;
import org.example.pichetest.service.mapper.ResponseDtoMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final ResponseDtoMapper<BankAccountResponseDto, BankAccount> bankAccountMapper;
    private final UserService userService;

    public BankAccountController(BankAccountService bankAccountService, BankAccountMapper bankAccountMapper,
                                 UserService userService) {
        this.bankAccountService = bankAccountService;
        this.bankAccountMapper = bankAccountMapper;
        this.userService = userService;
    }

    @PostMapping
    public BankAccountResponseDto createAccount(Authentication auth, @RequestBody BankAccountRequestDto requestDto) {
        User user = getUserByAuthentication(auth);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(requestDto.getBalance());
        bankAccount.setName(requestDto.getAccountName());
        bankAccount.setUser(user);
        bankAccount.setAccountDetails(requestDto.getAccountDetails());

        return bankAccountMapper.mapToDto(bankAccountService.createNew(bankAccount));
    }

    @GetMapping
    public List<BankAccountResponseDto> getAccounts(Authentication auth) {
        User user = getUserByAuthentication(auth);

        return bankAccountService.findByUser(user).stream()
            .map(bankAccountMapper::mapToDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/details")
    public BankAccount getAccountDetails(Authentication auth,
                                         @RequestParam(value = "accountNumber") Long accountNumber) {
        User user = getUserByAuthentication(auth);
        return bankAccountService.getDetails(user, accountNumber);
    }

    private User getUserByAuthentication(Authentication authentication) {
        String email = authentication.getName();

        return userService.findByEmail(email).orElseThrow(() ->
            new UsernameNotFoundException("User with email " + email + " not found"));
    }
}
