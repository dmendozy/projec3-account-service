package com.account.account.service;

import com.account.account.adds.Transaction;
import com.account.account.model.Account;
import com.account.account.model.AccountType;
import com.account.account.repository.AccountRepository;
import com.account.account.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountTypeRepository accountTypeRepository;

    public Flux<Account> getAll(){
        return accountRepository.findAll();
    }

    public Mono<Account> getById(String id){return accountRepository.findById(id);}

    public Mono update(String id, Account account){
        return accountRepository.save(account);
    }

    public Mono save(Account account){
        return accountRepository.save(account);
    }

    public Mono delete(String id){
        return accountRepository.deleteById(id);
    }

    public Flux<Account> getByCustomerId(String customerId){return accountRepository.findByCustomerId(customerId);}

    public Mono<AccountType> getFreeTransactions(String accountType){
        return accountTypeRepository.findById(accountType);
    }
}
