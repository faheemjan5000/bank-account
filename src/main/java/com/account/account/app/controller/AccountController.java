package com.account.account.app.controller;

import com.account.account.app.dto.AccountDto;
import com.account.account.app.exception.AccountNotFoundException;
import com.account.account.app.exception.InsufficientBalanceException;
import com.account.account.app.exception.NoAccountFoundException;
import com.account.account.app.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {


    private AccountService accountService;

    @PostMapping("/add")
    public ResponseEntity<AccountDto> saveAccount(@RequestBody @Validated AccountDto accountDto){
        log.info(".AccountController.saveAccount() method is called...");

           return new ResponseEntity<>(accountService.saveAccount(accountDto), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getAccountById(@PathVariable("id") Integer accountId){
        log.info("AccountController.getAccountById() method is called...");
        log.info("ACCOUNTID IS : {}",accountId);
        try {
            AccountDto accountDto = accountService.getAccountById(accountId);
            return ResponseEntity.ok(accountDto);
        }catch (AccountNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllAccounts(){
        log.info("AccountController.getAllAccounts() method is called...");

        try {
            return ResponseEntity.ok(accountService.getAllAccounts());
        } catch (NoAccountFoundException e) {
          return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<Object> getCurrentBalance(@PathVariable("id") Integer accountId){
        log.info("AccountController.getCurrentBalance() method is called...");
        try {
            return ResponseEntity.ok(accountService.getCurrentBalance(accountId));
        }catch(AccountNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/deposit/{id}/{amount}")
    public ResponseEntity<Object> deposit(@PathVariable("id") Integer accountId,@PathVariable("amount") double amountToAdd){
        log.info("AccountController.deposit() method is called...");

        try {
            return ResponseEntity.ok(accountService.depositBalance(accountId,amountToAdd));
        }catch (AccountNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public ResponseEntity<Object> withdraw(@PathVariable("id") Integer accountId,@PathVariable("amount") double amountToWithdraw){
        log.info("AccountController.deposit() method is called...");

        try {
            return ResponseEntity.ok(accountService.withdrawBalance(accountId,amountToWithdraw));
        }catch (AccountNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (InsufficientBalanceException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Integer accountId){
        log.info("AccountController.deleteAccount() method is called...");
        try {
            accountService.deleteById(accountId);
            return ResponseEntity.ok("account deleted successfully");
        } catch (AccountNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
