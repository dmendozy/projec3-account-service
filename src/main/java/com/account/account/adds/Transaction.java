package com.account.account.adds;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Transaction {
    public String transactionId;
    public String transactionName;
    public double output;
    public double input;
    public double commission;
    public LocalDate datetime;
    public String accountId;
    public String creditId;
}
