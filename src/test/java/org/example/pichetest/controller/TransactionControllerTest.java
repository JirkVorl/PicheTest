package org.example.pichetest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import org.example.pichetest.dto.request.FromAccountRequestDto;
import org.example.pichetest.dto.request.ToAccountRequestDto;
import org.example.pichetest.dto.request.TransferRequestDto;
import org.example.pichetest.dto.request.WithdrawRequestDto;
import org.example.pichetest.dto.response.BankAccountResponseDto;
import org.example.pichetest.model.BankAccount;
import org.example.pichetest.model.User;
import org.example.pichetest.service.BankAccountService;
import org.example.pichetest.service.TransactionService;
import org.example.pichetest.service.UserService;
import org.example.pichetest.service.mapper.ResponseDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private ResponseDtoMapper<BankAccountResponseDto, BankAccount> bankAccountMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testWithdraw() throws Exception {
        WithdrawRequestDto requestDto = new WithdrawRequestDto();
        requestDto.setAccountNumber(1L);
        requestDto.setAmount(new BigDecimal("50.0"));

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setAccountNumber(1L);
        bankAccount.setBalance(new BigDecimal("100.0"));

        BankAccount updatedBankAccount = new BankAccount();
        updatedBankAccount.setId(1L);
        updatedBankAccount.setAccountNumber(1L);
        updatedBankAccount.setBalance(new BigDecimal("50.0"));

        BankAccountResponseDto responseDto = new BankAccountResponseDto();
        responseDto.setId(1L);
        responseDto.setAccountNumber(1L);
        responseDto.setBalance(new BigDecimal("50.0"));

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(bankAccountService.findByUser(user)).thenReturn(Collections.singletonList(bankAccount));
        when(transactionService.withdraw(bankAccount, requestDto.getAmount())).thenReturn(updatedBankAccount);
        when(bankAccountMapper.mapToDto(updatedBankAccount)).thenReturn(responseDto);

        mockMvc.perform(post("/transactions/withdraw")
                .with(csrf())
                .contentType("application/json")
                .content("{\"accountNumber\":1,\"amount\":50.0}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.accountNumber").value(1L))
            .andExpect(jsonPath("$.balance").value(50.0));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testDeposit() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setAccountNumber(1L);
        bankAccount.setBalance(new BigDecimal("100.0"));

        BankAccount updatedBankAccount = new BankAccount();
        updatedBankAccount.setId(1L);
        updatedBankAccount.setAccountNumber(1L);
        updatedBankAccount.setBalance(new BigDecimal("150.0"));

        when(bankAccountService.findAccountByAccountNumber(1L)).thenReturn(Optional.of(bankAccount));
        when(transactionService.deposit(new BigDecimal("50.0"), bankAccount)).thenReturn(updatedBankAccount);

        mockMvc.perform(post("/transactions/deposit")
                .with(csrf())
                .param("accountNumber", "1")
                .param("value", "50.0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(true));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testTransfer() throws Exception {
        TransferRequestDto transferRequestDto = new TransferRequestDto();
        FromAccountRequestDto fromAccount = new FromAccountRequestDto();
        fromAccount.setAccountNumber(1L);
        fromAccount.setAmount(new BigDecimal("50.0"));
        transferRequestDto.setFromAccount(fromAccount);

        ToAccountRequestDto toAccount = new ToAccountRequestDto();
        toAccount.setAccountNumber(2L);
        transferRequestDto.setToAccount(toAccount);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        BankAccount fromBankAccount = new BankAccount();
        fromBankAccount.setId(1L);
        fromBankAccount.setAccountNumber(1L);
        fromBankAccount.setBalance(new BigDecimal("100.0"));

        BankAccount toBankAccount = new BankAccount();
        toBankAccount.setId(2L);
        toBankAccount.setAccountNumber(2L);
        toBankAccount.setBalance(new BigDecimal("50.0"));

        BankAccount updatedFromAccount = new BankAccount();
        updatedFromAccount.setId(1L);
        updatedFromAccount.setAccountNumber(1L);
        updatedFromAccount.setBalance(new BigDecimal("50.0"));

        BankAccount updatedToAccount = new BankAccount();
        updatedToAccount.setId(2L);
        updatedToAccount.setAccountNumber(2L);
        updatedToAccount.setBalance(new BigDecimal("100.0"));

        BankAccountResponseDto responseDto = new BankAccountResponseDto();
        responseDto.setId(1L);
        responseDto.setAccountNumber(1L);
        responseDto.setBalance(new BigDecimal("50.0"));

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(bankAccountService.findByUser(user)).thenReturn(Collections.singletonList(fromBankAccount));
        when(bankAccountService.findAccountByAccountNumber(2L)).thenReturn(Optional.of(toBankAccount));
        when(transactionService.transfer(toBankAccount, fromBankAccount, fromAccount.getAmount())).thenReturn(
            Optional.of(updatedFromAccount));
        when(bankAccountMapper.mapToDto(updatedFromAccount)).thenReturn(responseDto);

        mockMvc.perform(post("/transactions/transfer")
                .with(csrf())
                .contentType("application/json")
                .content("{\"fromAccount\":{\"accountNumber\":1,\"amount\":50.0},\"toAccount\":{\"accountNumber\":2}}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.accountNumber").value(1L))
            .andExpect(jsonPath("$.balance").value(50.0));
    }
}
