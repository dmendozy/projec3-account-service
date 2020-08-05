package com.account.account.adds;

import lombok.Data;

import java.util.List;

@Data
public class Bank {
    public String bankId;
    public String name;
    public List<String> customerId;
}
