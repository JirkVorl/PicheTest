package org.example.pichetest.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequestDto {

    private FromAccountRequestDto fromAccount;
    private ToAccountRequestDto toAccount;
}
