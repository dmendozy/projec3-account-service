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

    public Account(){
        super();
    }

}
