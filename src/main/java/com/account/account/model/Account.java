package com.account.account.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
@Document(collection = "accounts")
public class Account {

    @Id
    public String accountId;
    public String bankId;
    public String numberAccount;
    public String typeAccount;
    public LocalDate creationDate;
    public double currentBalance;
    public double commission;
    public double commissionInterBank;
    public int transactionsAtm;
    public int transactionsBank;
    public List<String> customerId;
    public List<String> signatories;
    @Transient
    public List<String> transactions;

    public Account() {
        super();
    }

    public Account(String accountId, String bankId, String numberAccount, String typeAccount, LocalDate creationDate, double currentBalance, double commission, double commissionInterBank, int transactionsAtm, int transactionsBank, List<String> customerId, List<String> signatories) {
        this.accountId = accountId;
        this.bankId = bankId;
        this.numberAccount = numberAccount;
        this.typeAccount = typeAccount;
        this.creationDate = creationDate;
        this.currentBalance = currentBalance;
        this.commission = commission;
        this.commissionInterBank = commissionInterBank;
        this.transactionsAtm = transactionsAtm;
        this.transactionsBank = transactionsBank;
        this.customerId = customerId;
        this.signatories = signatories;
    }
}
