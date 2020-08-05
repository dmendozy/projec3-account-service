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
    public String accountTypeId;
    public String typeName;
    public double minAmount;
    public double minBalance;
    public int freeBankTransactions;
    public int freeAtmTransactions;

    public AccountType() {
        super();
    }

}
