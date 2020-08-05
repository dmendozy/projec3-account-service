package com.account.account.adds;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Credit {
    public String creditId;
    public String bankId;
    public String numberCredit;
    public String typeAccount;
    public double creditAvailable;
    public double creditConsumed;
    public LocalDate creationDate;
    public LocalDate expirationPayment;
    public String customerId;
}
