package com.account.account.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    public String numberAccount;
    public double balance;
    public List<String> customerId;
    public List<String> signatories;

    public Account(){
        super();
    }
}
