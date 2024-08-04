package org.example.pichetest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bankAccounts")
@Getter
@Setter
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;

    private String name;

    // should be unique, but i`m using the random generation, so cant guaranty it to be unique
    // let`s pretend it`s unique
    private Long accountNumber;

    private BigDecimal balance;

    /*some details here. GET/accounts would already give user info about his account,
     but it says "Get account details by account number.",
     so this value would be included as am implementation of this task
   */
    private String accountDetails;
}
