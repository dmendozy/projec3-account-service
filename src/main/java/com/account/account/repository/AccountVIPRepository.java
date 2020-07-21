package com.account.account.repository;

import com.account.account.model.AccountVIP;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountVIPRepository extends ReactiveMongoRepository<AccountVIP,String> {
}
