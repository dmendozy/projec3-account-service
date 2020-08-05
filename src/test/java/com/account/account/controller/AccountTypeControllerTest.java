package com.account.account.controller;


import com.account.account.model.AccountType;
import com.account.account.repository.AccountTypeRepository;
import com.account.account.service.AccountTypeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AccountTypeController.class)
@Import(AccountTypeService.class)
public class AccountTypeControllerTest {
    @MockBean
    AccountTypeRepository repository;

    @Autowired
    private WebTestClient webClient;

    final private static Map<String, AccountType> accountTypeMap = new HashMap<>();

    @BeforeAll
    public static void setup(){
        accountTypeMap.put("test",new AccountType("1","Personal",0,0,10,10));
    }

    @Test
    public void testCreateAccountType(){
        Mockito
                .when(repository.save(accountTypeMap.get("test"))).thenReturn(Mono.just(accountTypeMap.get("test")));

        webClient.post()
                .uri("/accounts/type")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(accountTypeMap.get("test")))
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(repository,Mockito.times(1)).save(accountTypeMap.get("test"));

    }


    @Test
    public void testGetAccountTypeById(){
        Mockito
                .when(repository.findById(accountTypeMap.get("test").accountTypeId))
                .thenReturn(Mono.just(accountTypeMap.get("test")));

        webClient.get()
                .uri("/accounts/type/{id}",accountTypeMap.get("test").accountTypeId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountType.class)
                .isEqualTo(accountTypeMap.get("test"));
        Mockito.verify(repository, Mockito.times(1)).findById(accountTypeMap.get("test").accountTypeId);
    }

    @Test
    public void testUpdateAccountType(){
        Mockito
                .when(repository.findById(accountTypeMap.get("test").accountTypeId))
                .thenReturn(Mono.just(accountTypeMap.get("test")));

        webClient.put()
                .uri("/accounts/type/{id}",accountTypeMap.get("test").accountTypeId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(accountTypeMap.get("test")))
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(repository,Mockito.times(1)).save(accountTypeMap.get("test"));
    }

    @Test
    public void testDeleteAccountType(){
        Mockito
                .when(repository.deleteById(accountTypeMap.get("test").accountTypeId))
                .thenReturn(Mono.empty());

        webClient.delete()
                .uri("/accounts/type/{id}",accountTypeMap.get("test").accountTypeId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountType.class)
                .isEqualTo(null);
        Mockito.verify(repository, Mockito.times(1)).deleteById(accountTypeMap.get("test").accountTypeId);

    }
}
