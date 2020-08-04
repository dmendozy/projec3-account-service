package com.account.account.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
@Document(collection = "accountsType")
public class AccountType {
    @Id
    private String accountTypeId;
    private String typeName;
    private double minAmount;
    private double minBalance;
    private int freeBankTransactions;
    private int freeAtmTransactions;

    public AccountType(){
        super();
    }
}
