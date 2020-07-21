package com.account.account.service;

import com.account.account.model.AccountVIP;
import com.account.account.repository.AccountVIPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountVIPService {
    @Autowired
    private AccountVIPRepository accountVIPRepository;

    public Flux<AccountVIP> getAll(){
        return accountVIPRepository.findAll();
    }
    public Mono<AccountVIP> getById(String id){
        return accountVIPRepository.findById(id);
    }
    public Mono update(String id, AccountVIP accountVIP){
        return accountVIPRepository.save(accountVIP);
    }
    public Mono save(AccountVIP account){
        return accountVIPRepository.save(account);
    }
    public Mono delete(String id){
        return accountVIPRepository.deleteById(id);
    }
}
