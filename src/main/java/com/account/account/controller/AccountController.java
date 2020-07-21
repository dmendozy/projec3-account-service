package com.account.account.controller;

import com.account.account.model.Account;
import com.account.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping
    public Flux<Account> getAllAccounts(){
        return accountService.getAll();
    }

    @GetMapping("{id}")
    public Mono<Account> getAccountById(@PathVariable("id") String accountId){
        return accountService.getById(accountId);
    }

    @PostMapping
    public Mono<Account> createAccount(@Validated @RequestBody Account account){
        return accountService.save(account);
    }

    @PutMapping("{id}")
    public Mono<Account> updateAccount(@PathVariable("id") String accountId,
                                         @Validated @RequestBody Account account){
        return accountService.update(accountId, account);
    }

    @DeleteMapping("{id}")
    public Mono<Account> deleteAccount(@PathVariable("id") String accountId){
        return accountService.delete(accountId);
    }

}
