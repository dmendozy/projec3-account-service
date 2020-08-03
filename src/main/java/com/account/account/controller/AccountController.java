package com.account.account.controller;

import com.account.account.adds.Transaction;
import com.account.account.model.Account;
import com.account.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    WebClient.Builder webClientBuilder;

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

    //Get customer by id
    @GetMapping("/customer/{customerId}")
    public Flux<Account> findByCustomer(@PathVariable("customerId") String customerId) {
        return accountService.getByCustomerId(customerId);
    }

    //Get customers in date range
    @GetMapping("/search/{bankId}/{firstDate}/{lastDate}")
    public Flux<Account> getAccountsBetweenDates(@PathVariable("bankId") String bankId,
                                                 @PathVariable("firstDate") String firstDate,
                                                 @PathVariable("lastDate") String lastDate) {
        LocalDate date1 = LocalDate.parse(firstDate);
        LocalDate date2 = LocalDate.parse(lastDate);
        return accountService.getAll()
                .filter(account -> account.getBankId()!=null&&
                        account.getCreationDate().compareTo(date1)>=0&&
                        account.getCreationDate().compareTo(date2)<=0)
                .flatMap(account -> {
                    return accountService.getById(account.getAccountId());
                });
    }

    @PutMapping("/atm/deposit/{accountId}/{amount}")
    public Mono depositByAtm(@PathVariable("accountId") String accountId,
                             @PathVariable("amount") double amount){
        return accountService.getById(accountId).flatMap(account -> {

            int transactionsAtm = account.getTransactionsAtm();
            String accountTypeId = account.getTypeAccount();

            return accountService.getFreeTransactions(accountTypeId).flatMap(accountType->{
                        double balance = account.getCurrentBalance();
                        double commission= 0;
                        if (transactionsAtm>=accountType.getFreeAtmTransactions()){
                            balance-=account.getCommission();
                            commission =account.getCommission();
                        }
                Transaction transaction = new Transaction("Deposit",amount,commission,LocalDate.now(),accountId);
                Mono<Transaction> transactionMono= webClientBuilder
                        .build()
                        .post()
                        .uri("http://localhost:8084/transactions/")
                        .body(Mono.just(transaction),Transaction.class)
                        .retrieve()
                        .bodyToMono(Transaction.class);

                account.setTransactionsAtm(transactionsAtm+1);
                account.setCurrentBalance(balance+amount);

                        return transactionMono.flatMap(t->{
                            return accountService.save(account);
                        });
                    });
        });
    }

    @PutMapping("/atm/withdraw/{accountId}/{amount}")
    public Mono withdrawByAtm(@PathVariable("accountId") String accountId,
                             @PathVariable("amount") double amount){
        return accountService.getById(accountId).flatMap(account -> {

            int transactionsAtm = account.getTransactionsAtm();
            String accountTypeId = account.getTypeAccount();

            return accountService.getFreeTransactions(accountTypeId).flatMap(accountType->{
                double commission = 0;
                double balance = account.getCurrentBalance();
                if (transactionsAtm>=accountType.getFreeAtmTransactions()){
                    balance-=account.getCommission();
                    commission= account.getCommission();
                }
                Transaction transaction = new Transaction("Withdraw",amount,commission,LocalDate.now(),accountId);
                Mono<Transaction> transactionMono= webClientBuilder
                        .build()
                        .post()
                        .uri("http://localhost:8084/transactions/")
                        .body(Mono.just(transaction),Transaction.class)
                        .retrieve()
                        .bodyToMono(Transaction.class);
                if (account.getCurrentBalance()>=amount){
                    account.setTransactionsAtm(transactionsAtm+1);
                    account.setCurrentBalance(balance-amount);
                }
                return accountService.save(account);
            });
        });
    }

}
