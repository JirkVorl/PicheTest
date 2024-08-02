package org.example.pichetest.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class BankAccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fromBankAccount_id")
    private BankAccount fromAccount;

    @ManyToOne
    @JoinColumn(name = "toBankAccount_id")
    private BankAccount toAccount;

    private BigDecimal amount;

}
