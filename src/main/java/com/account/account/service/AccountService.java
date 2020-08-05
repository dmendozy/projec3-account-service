package com.account.account.service;

import com.account.account.model.Account;
import com.account.account.model.AccountType;
import com.account.account.repository.AccountRepository;
import com.account.account.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    public Flux<Account> getAll() {
        return accountRepository.findAll();
    }

    public Mono<Account> getById(String id) {
        return accountRepository.findById(id);
    }

    public Mono update(String id, Account account) {
        return accountRepository.save(account);
    }

    public Mono save(Account account) {
        return accountRepository.save(account);
    }

    public Mono delete(String id) {
        return accountRepository.deleteById(id);
    }

    public Flux<Account> getByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Mono<AccountType> getFreeTransactions(String accountType) {
        return accountTypeRepository.findById(accountType);
    }
}
