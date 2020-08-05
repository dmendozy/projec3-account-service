package com.account.account.controller;

import com.account.account.model.AccountType;
import com.account.account.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("accounts/type")
public class AccountTypeController {
    @Autowired
    AccountTypeService accountTypeService;

    @GetMapping
    public Flux<AccountType> getAllAccounts() {
        return accountTypeService.getAll();
    }

    @GetMapping("{id}")
    public Mono<AccountType> getAccountById(@PathVariable("id") String accountId) {
        return accountTypeService.getById(accountId);
    }

    @PostMapping
    public Mono<AccountType> createAccount(@Validated @RequestBody AccountType accountType) {
        return accountTypeService.save(accountType);
    }

    @PutMapping("{id}")
    public Mono<AccountType> updateAccount(@PathVariable("id") String accountId,
                                           @Validated @RequestBody AccountType accountType) {
        return accountTypeService.update(accountId, accountType);
    }

    @DeleteMapping("{id}")
    public Mono<AccountType> deleteAccount(@PathVariable("id") String accountId) {
        return accountTypeService.delete(accountId);
    }
}
