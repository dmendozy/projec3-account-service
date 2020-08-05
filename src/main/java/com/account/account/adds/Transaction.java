package com.account.account.adds;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Transaction {
    public String transactionId;
    public String transactionName;
    public double amount;
    public double commission;
    public LocalDateTime dateTime;
    public String accountId;
    public String creditId;

    public Transaction(String transactionName, double amount, double commission, LocalDateTime dateTime, String accountId) {
        this.transactionName = transactionName;
        this.amount = amount;
        this.commission = commission;
        this.dateTime = dateTime;
        this.accountId = accountId;
    }

    public Transaction(String transactionName, double amount, LocalDateTime dateTime, String accountId, String creditId) {
        this.transactionName = transactionName;
        this.amount = amount;
        this.dateTime = dateTime;
        this.accountId = accountId;
        this.creditId = creditId;
    }

    public Transaction() {
        super();
    }
}
