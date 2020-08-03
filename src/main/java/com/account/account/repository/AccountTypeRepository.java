package com.account.account.repository;

import com.account.account.model.AccountType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends ReactiveMongoRepository<AccountType, String> {
}
