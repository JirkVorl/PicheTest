package org.example.pichetest.service.mapper;

import org.example.pichetest.dto.response.BankAccountResponseDto;
import org.example.pichetest.model.BankAccount;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapper implements ResponseDtoMapper<BankAccountResponseDto, BankAccount> {

    @Override
    public BankAccountResponseDto mapToDto(BankAccount bankAccount) {
        BankAccountResponseDto dto = new BankAccountResponseDto();
        dto.setAccountName(bankAccount.getName());
        dto.setBalance(bankAccount.getBalance());
        dto.setId(bankAccount.getId());
        dto.setAccountNumber(bankAccount.getAccountNumber());
        dto.setUserEmail(bankAccount.getUser().getEmail());
        return dto;
    }
}
