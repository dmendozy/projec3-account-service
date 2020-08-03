package com.account.account.adds;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Transaction {
    public String transactionId;
    public String transactionName;
    public double amount;
    public double commission;
    public LocalDate datetime;
    public String accountId;
    public String creditId;

    public Transaction(String transactionName, double amount, double commission, LocalDate datetime, String accountId) {
        this.transactionName = transactionName;
        this.amount = amount;
        this.commission = commission;
        this.datetime = datetime;
        this.accountId = accountId;
    }
}
