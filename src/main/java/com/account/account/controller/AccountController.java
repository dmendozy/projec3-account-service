package com.account.account.controller;

import com.account.account.adds.Credit;
import com.account.account.adds.Transaction;
import com.account.account.model.Account;
import com.account.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    WebClient.Builder webClientBuilder;

    @GetMapping
    public Flux<Account> getAllAccounts() {
        return accountService.getAll();
    }

    @GetMapping("{id}")
    public Mono<Account> getAccountById(@PathVariable("id") String accountId) {
        return accountService.getById(accountId);
    }

    @PostMapping
    public Mono createAccount(@Validated @RequestBody Account account) {
        return accountService.save(account);
    }

    @PutMapping("{id}")
    public Mono<Account> updateAccount(@PathVariable("id") String accountId,
                                       @Validated @RequestBody Account account) {
        return accountService.update(accountId, account);
    }

    @DeleteMapping("{id}")
    public Mono<Account> deleteAccount(@PathVariable("id") String accountId) {
        return accountService.delete(accountId);
    }

    //Check balance
    @GetMapping("/balance/{accountId}")
    public Mono checkCurrentBalance(@PathVariable("accountId") String accountId) {
        return accountService.getById(accountId)
                .map(Account::getCurrentBalance);
    }

    //Find transactions
    @GetMapping("/transactions/{accountId}")
    public Flux<Transaction> checkTransactions(@PathVariable("accountId") String accountId) {
        return webClientBuilder
                .build()
                .get()
                .uri("http://transaction-service/transactions/account/{accountId}", accountId)
                .retrieve()
                .bodyToFlux(Transaction.class);
    }

    //Deposit money
    @PutMapping("/deposit/{accountId}/{amount}")
    public Mono deposit(@PathVariable("accountId") String accountId,
                        @PathVariable("amount") double amount) {
        return accountService.getById(accountId)
                .flatMap(account -> {
                    int transactionsBank = account.getTransactionsBank();
                    String accountTypeId = account.getTypeAccount();

                    return accountService.getFreeTransactions(accountTypeId)
                            .flatMap(accountType -> {
                                double balance = account.getCurrentBalance();
                                double commission = 0;
                                if (transactionsBank >= accountType.getFreeBankTransactions()) {
                                    balance -= account.getCommission();
                                    commission = account.getCommission();
                                }
                                Transaction transaction = new Transaction("Deposit from Bank", amount, commission, LocalDateTime.now(), accountId);
                                System.out.println(LocalDateTime.now() + "test");
                                Mono<Transaction> transactionMono = webClientBuilder
                                        .build()
                                        .post()
                                        .uri("http://transaction-service/transactions/")
                                        .body(Mono.just(transaction), Transaction.class)
                                        .retrieve()
                                        .bodyToMono(Transaction.class);
                                account.setTransactionsBank(transactionsBank + 1);
                                account.setCurrentBalance(balance + amount);
                                return transactionMono.flatMap(t -> {
                                    return accountService.save(account);
                                });
                            });
                });
    }

    //Withdraw money
    @PutMapping("/withdraw/{accountId}/{amount}")
    public Mono withdraw(@PathVariable("accountId") String accountId,
                         @PathVariable("amount") double amount) {
        return accountService.getById(accountId)
                .flatMap(account -> {
                    int transactionsBank = account.getTransactionsBank();
                    String accountTypeId = account.getTypeAccount();

                    return accountService.getFreeTransactions(accountTypeId)
                            .flatMap(accountType -> {
                                double balance = account.getCurrentBalance();
                                double commission = 0;
                                if (transactionsBank >= accountType.getFreeBankTransactions()) {
                                    balance -= account.getCommission();
                                    commission = account.getCommission();
                                }
                                Transaction transaction = new Transaction("Withdraw from Bank", amount, commission, LocalDateTime.now(), accountId);
                                Mono<Transaction> transactionMono = webClientBuilder
                                        .build()
                                        .post()
                                        .uri("http://transaction-service/transactions/")
                                        .body(Mono.just(transaction), Transaction.class)
                                        .retrieve()
                                        .bodyToMono(Transaction.class);
                                account.setTransactionsBank(transactionsBank + 1);
                                account.setCurrentBalance(balance - amount);
                                return transactionMono.flatMap(t -> {
                                    return accountService.save(account);
                                });
                            });
                });
    }

    //Withdraw money interbank
    @PutMapping("/withdraw/{accountId}/{amount}/{bankId}")
    public Mono withdrawInterbank(@PathVariable("accountId") String accountId,
                                  @PathVariable("amount") double amount,
                                  @PathVariable("bankId") String bankId) {
        return accountService.getById(accountId)
                .filter(account -> account.getBankId().equals(bankId))
                .flatMap(account -> {
                    int transactionsBank = account.getTransactionsBank();
                    String accountTypeId = account.getTypeAccount();

                    return accountService.getFreeTransactions(accountTypeId)
                            .flatMap(accountType -> {
                                double balance = account.getCurrentBalance();
                                double commission = 0;
                                if (transactionsBank >= accountType.getFreeBankTransactions()) {
                                    balance -= account.getCommission();
                                    commission = account.getCommission();
                                }
                                Transaction transaction = new Transaction("Withdraw interbank", amount, commission, LocalDateTime.now(), accountId);
                                Mono<Transaction> transactionMono = webClientBuilder
                                        .build()
                                        .post()
                                        .uri("http://transaction-service/transactions/")
                                        .body(Mono.just(transaction), Transaction.class)
                                        .retrieve()
                                        .bodyToMono(Transaction.class);
                                account.setTransactionsBank(transactionsBank + 1);
                                account.setCurrentBalance(balance - amount);
                                return transactionMono.flatMap(t -> {
                                    return accountService.save(account);
                                });
                            });
                });
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
                .filter(account -> account.getBankId().equals(bankId) &&
                        account.getCreationDate().compareTo(date1) >= 0 &&
                        account.getCreationDate().compareTo(date2) <= 0)
                .flatMap(account -> {
                    return accountService.getById(account.getAccountId());
                });
    }

    //Account Transfer
    @GetMapping("/transfer/{account1}/{account2}/{amount}")
    public Mono accountTransfer(@PathVariable("account1") String account1,
                                @PathVariable("account2") String account2,
                                @PathVariable("amount") double amount) {
        return accountService.getById(account1)
                .filter(origin -> origin.getCurrentBalance() >= amount)
                .flatMap(origin -> {
                    origin.setCurrentBalance(origin.getCurrentBalance() - amount);

                    return accountService.getById(account2)
                            .filter(destiny -> {
                                destiny.setCurrentBalance(destiny.getCurrentBalance() + amount);

                                accountService.save(origin).subscribe();
                                accountService.save(destiny).subscribe();
                                return true;
                            });

                });
    }

    //Pay credit from account
    @GetMapping("/pay/credit/{accountId}/{creditId}/{amount}")
    public Mono payCreditFromAccount(@PathVariable("accountId") String accountId,
                                     @PathVariable("creditId") String creditId,
                                     @PathVariable("amount") double amount) {
        return accountService.getById(accountId)
                .filter(account -> account.getCurrentBalance() >= amount)
                .flatMap(account -> {
                    Transaction transaction = new Transaction("Pay credit from account", amount, LocalDateTime.now(), accountId, creditId);
                    return webClientBuilder
                            .build()
                            .post()
                            .uri("http://transaction-service/transactions/")
                            .body(Mono.just(transaction), Transaction.class)
                            .retrieve()
                            .bodyToMono(Transaction.class);
                }).flatMap(transaction -> {
                    return webClientBuilder
                            .build()
                            .put()
                            .uri("http://account-service/accounts/withdraw/" + accountId + "/" + amount)
                            .retrieve()
                            .bodyToMono(Account.class);
                }).flatMap(credit -> {
                    return webClientBuilder
                            .build()
                            .put()
                            .uri("http://credit-service/credits/pay/" + creditId + "/" + amount)
                            .retrieve()
                            .bodyToMono(Credit.class);
                });
    }

    //Pay credit from account interbank
    @GetMapping("/pay/credit/{bankAccountId}/{accountId}/{bankCreditId}/{creditId}/{amount}")
    public Mono payCreditFromAccountInterbank(@PathVariable("bankAccountId") String bankAccountId,
                                              @PathVariable("accountId") String accountId,
                                              @PathVariable("bankCreditId") String bankCreditId,
                                              @PathVariable("creditId") String creditId,
                                              @PathVariable("amount") double amount) {
        return accountService.getById(accountId)
                .filter(account -> account.getCurrentBalance() >= amount)
                .flatMap(account -> {
                    Transaction transaction = new Transaction("Pay credit from account interbank", amount, LocalDateTime.now(), accountId, creditId);
                    return webClientBuilder
                            .build()
                            .post()
                            .uri("http://transaction-service/transactions/")
                            .body(Mono.just(transaction), Transaction.class)
                            .retrieve()
                            .bodyToMono(Transaction.class);
                }).flatMap(transaction -> {
                    return webClientBuilder
                            .build()
                            .put()
                            .uri("http://credit-service/credits/pay/" + creditId + "/" + amount + "/" + bankCreditId)
                            .retrieve()
                            .bodyToMono(Credit.class);
                }).flatMap(credit -> {
                    return webClientBuilder
                            .build()
                            .put()
                            .uri("http://account-service/accounts/withdraw/" + accountId + "/" + amount + "/" + bankAccountId)
                            .retrieve()
                            .bodyToMono(Account.class);
                }).flatMap(credit -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.OK).body(credit));
                });
    }

    //Deposit ATM
    @PutMapping("/atm/deposit/{accountId}/{amount}")
    public Mono depositByAtm(@PathVariable("accountId") String accountId,
                             @PathVariable("amount") double amount) {
        return accountService.getById(accountId)
                .flatMap(account -> {

                    int transactionsAtm = account.getTransactionsAtm();
                    String accountTypeId = account.getTypeAccount();

                    return accountService.getFreeTransactions(accountTypeId)
                            .flatMap(accountType -> {
                                double balance = account.getCurrentBalance();
                                double commission = 0;
                                if (transactionsAtm >= accountType.getFreeAtmTransactions()) {
                                    balance -= account.getCommission();
                                    commission = account.getCommission();
                                }
                                Transaction transaction = new Transaction("Deposit from ATM", amount, commission, LocalDateTime.now(), accountId);
                                Mono<Transaction> transactionMono = webClientBuilder
                                        .build()
                                        .post()
                                        .uri("http://transaction-service/transactions/")
                                        .body(Mono.just(transaction), Transaction.class)
                                        .retrieve()
                                        .bodyToMono(Transaction.class);

                                account.setTransactionsAtm(transactionsAtm + 1);
                                account.setCurrentBalance(balance + amount);

                                return transactionMono.flatMap(t -> {
                                    return accountService.save(account);
                                });
                            });
                });
    }

    //Withdraw ATM
    @PutMapping("/atm/withdraw/{accountId}/{amount}")
    public Mono withdrawByAtm(@PathVariable("accountId") String accountId,
                              @PathVariable("amount") double amount) {
        return accountService.getById(accountId).flatMap(account -> {

            int transactionsAtm = account.getTransactionsAtm();
            String accountTypeId = account.getTypeAccount();

            return accountService.getFreeTransactions(accountTypeId).flatMap(accountType -> {
                double commission = 0;
                double balance = account.getCurrentBalance();
                if (transactionsAtm >= accountType.getFreeAtmTransactions()) {
                    balance -= account.getCommission();
                    commission = account.getCommission();
                }
                Transaction transaction = new Transaction("Withdraw from ATM", amount, commission, LocalDateTime.now(), accountId);
                Mono<Transaction> transactionMono = webClientBuilder
                        .build()
                        .post()
                        .uri("http://transaction-service/transactions/")
                        .body(Mono.just(transaction), Transaction.class)
                        .retrieve()
                        .bodyToMono(Transaction.class);
                if (account.getCurrentBalance() >= amount) {
                    account.setTransactionsAtm(transactionsAtm + 1);
                    account.setCurrentBalance(balance - amount);
                }
                return accountService.save(account);
            });
        });
    }

    //Deposit ATM to other bank
    @PutMapping("/bank/atm/deposit/{accountId}/{amount}/{bankId}")
    public Mono depositByAtmToAnotherBank(@PathVariable("accountId") String accountId,
                                          @PathVariable("amount") double amount,
                                          @PathVariable("bankId") String bankId) {
        return accountService.getById(accountId).flatMap(account -> {

            int transactionsAtm = account.getTransactionsAtm();
            String accountTypeId = account.getTypeAccount();

            return accountService.getFreeTransactions(accountTypeId).flatMap(accountType -> {
                double balance = account.getCurrentBalance();
                double commission = 0;
                if (transactionsAtm >= accountType.getFreeAtmTransactions() &&
                        !account.getBankId().equals(bankId)) {
                    balance -= account.getCommissionInterBank();
                    commission = account.getCommissionInterBank();
                }
                Transaction transaction = new Transaction("Deposit from ATM interbank", amount, commission, LocalDateTime.now(), accountId);
                Mono<Transaction> transactionMono = webClientBuilder
                        .build()
                        .post()
                        .uri("http://transaction-service/transactions/")
                        .body(Mono.just(transaction), Transaction.class)
                        .retrieve()
                        .bodyToMono(Transaction.class);

                account.setTransactionsAtm(transactionsAtm + 1);
                account.setCurrentBalance(balance + amount);

                return transactionMono.flatMap(t -> {
                    return accountService.save(account);
                });
            });
        });
    }

    //Withdraw ATM to other bank
    @PutMapping("/bank/atm/withdraw/{accountId}/{amount}/{bankId}")
    public Mono withdrawByAtm(@PathVariable("accountId") String accountId,
                              @PathVariable("amount") double amount,
                              @PathVariable("bankId") String bankId) {
        return accountService.getById(accountId).flatMap(account -> {

            int transactionsAtm = account.getTransactionsAtm();
            String accountTypeId = account.getTypeAccount();

            return accountService.getFreeTransactions(accountTypeId).flatMap(accountType -> {
                double commission = 0;
                double balance = account.getCurrentBalance();
                if (transactionsAtm >= accountType.getFreeAtmTransactions() &&
                        !account.getBankId().equals(bankId)) {
                    balance -= account.getCommissionInterBank();
                    commission = account.getCommissionInterBank();
                }
                Transaction transaction = new Transaction("Withdraw from ATM interbank", amount, commission, LocalDateTime.now(), accountId);
                Mono<Transaction> transactionMono = webClientBuilder
                        .build()
                        .post()
                        .uri("http://transaction-service/transactions/")
                        .body(Mono.just(transaction), Transaction.class)
                        .retrieve()
                        .bodyToMono(Transaction.class);
                if (account.getCurrentBalance() >= amount) {
                    account.setTransactionsAtm(transactionsAtm + 1);
                    account.setCurrentBalance(balance - amount);
                }
                return accountService.save(account);
            });
        });
    }

}
