package org.example.pichetest.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountRequestDto {

    private String accountName;

    private BigDecimal balance;

    private String accountDetails;
}
