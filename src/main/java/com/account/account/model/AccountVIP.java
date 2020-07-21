package com.account.account.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@Document(collection = "accountsVIP")
public class AccountVIP extends Account {

    public double initialAmount;
    public double finalAmountMonth;

    public AccountVIP(double initialAmount, double finalAmountMonth){
        super();
        this.initialAmount=initialAmount;
        this.finalAmountMonth=finalAmountMonth;
    }

}
