package com.account.account.adds;

import lombok.Data;

@Data
public class Transaction {
    public String transactionId;
    public String transactionName;
    public double output;
    public double input;
    public String datetime;
    public String accountId;
}
