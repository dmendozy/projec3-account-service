package com.account.account.controller;

import com.account.account.model.AccountVIP;
import com.account.account.service.AccountVIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts/vip")
public class AccountVIPController {
    @Autowired
    AccountVIPService accountVIPService;

    @GetMapping
    public Flux<AccountVIP> getAllAccounts(){
        return accountVIPService.getAll();
    }
    @GetMapping("{id}")
    public Mono<AccountVIP> getAccountById(@PathVariable("id") String accountId){
        return accountVIPService.getById(accountId);
    }

    @PostMapping
    public Mono<AccountVIP> createAccount(@Validated @RequestBody AccountVIP account){
        return accountVIPService.save(account);
    }

    @PutMapping("{id}")
    public Mono<AccountVIP> updateAccount(@PathVariable("id") String accountId,
                                       @Validated @RequestBody AccountVIP account){
        return accountVIPService.update(accountId, account);
    }

    @DeleteMapping("{id}")
    public Mono<AccountVIP> deleteAccount(@PathVariable("id") String accountId){
        return accountVIPService.delete(accountId);
    }
}