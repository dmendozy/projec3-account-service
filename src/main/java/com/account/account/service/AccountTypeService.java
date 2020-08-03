package com.account.account.service;

import com.account.account.model.Account;
import com.account.account.model.AccountType;
import com.account.account.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountTypeService {
    @Autowired
    private AccountTypeRepository accountTypeRepository;

    public Flux<AccountType> getAll() {
        return accountTypeRepository.findAll();
    }

    public Mono<AccountType> save(AccountType accountType) {
        return accountTypeRepository.save(accountType);
    }

    public Mono<AccountType> getById(String typeId) {
        return accountTypeRepository.findById(typeId);
    }

    public Mono update(String id, AccountType accountType){
        return accountTypeRepository.save(accountType);
    }


    public Mono delete(String accountId) {
        return accountTypeRepository.deleteById(accountId);
    }
}
