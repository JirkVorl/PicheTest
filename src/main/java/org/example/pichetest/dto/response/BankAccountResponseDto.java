package org.example.pichetest.dto.response;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountResponseDto {

    private Long id;
    private String userEmail;
    private String accountName;
    private Long accountNumber;
    private BigDecimal balance;
}
