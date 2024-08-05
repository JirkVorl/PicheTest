package org.example.pichetest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import org.example.pichetest.dto.request.BankAccountRequestDto;
import org.example.pichetest.dto.response.BankAccountResponseDto;
import org.example.pichetest.model.BankAccount;
import org.example.pichetest.model.User;
import org.example.pichetest.service.BankAccountService;
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

@WebMvcTest(BankAccountController.class)
@AutoConfigureMockMvc
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private ResponseDtoMapper<BankAccountResponseDto, BankAccount> bankAccountMapper;

    @MockBean
    private UserService userService;

    @InjectMocks
    private BankAccountController bankAccountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testCreateAccount() throws Exception {
        BankAccountRequestDto requestDto = new BankAccountRequestDto();
        requestDto.setBalance(new BigDecimal("100.0"));
        requestDto.setAccountName("Test Account");
        requestDto.setAccountDetails("Test Details");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setBalance(new BigDecimal("100.0"));
        bankAccount.setName("Test Account");
        bankAccount.setAccountDetails("Test Details");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        BankAccountResponseDto responseDto = new BankAccountResponseDto();
        responseDto.setId(1L);
        responseDto.setBalance(new BigDecimal("100.0"));
        responseDto.setAccountName("Test Account");
        responseDto.setUserEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(bankAccountService.createNew(any(BankAccount.class))).thenReturn(bankAccount);
        when(bankAccountMapper.mapToDto(bankAccount)).thenReturn(responseDto);

        mockMvc.perform(post("/accounts")
                .with(csrf())
                .contentType("application/json")
                .content("{\"balance\":100.0,\"accountName\":\"Test Account\",\"accountDetails\":\"Test Details\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.balance").value(100.0))
            .andExpect(jsonPath("$.accountName").value("Test Account"))
            .andExpect(jsonPath("$.userEmail").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testGetAccounts() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setBalance(new BigDecimal("100.0"));
        bankAccount.setName("Test Account");
        bankAccount.setAccountDetails("Test Details");

        BankAccountResponseDto responseDto = new BankAccountResponseDto();
        responseDto.setId(1L);
        responseDto.setBalance(new BigDecimal("100.0"));
        responseDto.setAccountName("Test Account");
        responseDto.setUserEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(bankAccountService.findByUser(user)).thenReturn(Collections.singletonList(bankAccount));
        when(bankAccountMapper.mapToDto(bankAccount)).thenReturn(responseDto);

        mockMvc.perform(get("/accounts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].balance").value(100.0))
            .andExpect(jsonPath("$[0].accountName").value("Test Account"))
            .andExpect(jsonPath("$[0].userEmail").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testGetAccountDetails() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setBalance(new BigDecimal("100.0"));
        bankAccount.setName("Test Account");
        bankAccount.setAccountDetails("Test Details");

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(bankAccountService.getDetails(user, 1L)).thenReturn(bankAccount);

        mockMvc.perform(get("/accounts/details")
                .param("accountNumber", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.balance").value(100.0))
            .andExpect(jsonPath("$.name").value("Test Account"))
            .andExpect(jsonPath("$.accountDetails").value("Test Details"));
    }
}
