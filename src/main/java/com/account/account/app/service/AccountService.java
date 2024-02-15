package com.account.account.app.service;

import com.account.account.app.dto.AccountDto;
import com.account.account.app.entity.Account;
import com.account.account.app.exception.AccountNotFoundException;
import com.account.account.app.exception.InsufficientBalanceException;
import com.account.account.app.exception.InvalidAccountIdException;
import com.account.account.app.mapper.AccountMapper;
import com.account.account.app.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class AccountService {


    private AccountRepository accountRepository;

    public AccountDto saveAccount(AccountDto accountDto){
        log.info("AccountService.saveAccount() method is called...");
        Account account = AccountMapper.mapAccountDtoToAccount(accountDto);
        Account accountSaved = accountRepository.save(account);
        log.debug("account saved in database : {}",accountSaved);
        return AccountMapper.mapAccountToAccountDto(accountSaved);
    }

    public AccountDto getAccountById(Integer accountId) throws AccountNotFoundException {
        log.info("AccountService.getAccountById() method is called...");
        Optional<Account> optionalAccount = getAccount(accountId);
        log.debug("Account retrieved from database : {}",optionalAccount.get());
        return AccountMapper.mapAccountToAccountDto(optionalAccount.get());
    }

    public List<AccountDto> getAllAccounts(){
        log.info("AccountService.getAllAccounts() method is called...");
        List<Account> accounts = accountRepository.findAll();
        log.debug("all accounts retrieved from database : {}",accounts);
        return accounts.stream()
                .map(account -> new AccountDto(account.getId(),
                        account.getAccountHolderName(),
                        account.getBalance()))
                .toList();
    }

    public double getCurrentBalance(Integer accountId) throws AccountNotFoundException {
        log.info("AccountService.getCurrentBalance() method is called...");
        Optional<Account> optionalAccount = getAccount(accountId);
        log.info("CURRENT BALANCE retrieved : {}",optionalAccount.get().getBalance());
        return optionalAccount.get().getBalance();
    }

    public AccountDto depositBalance(Integer accountId,double depositAmount) throws AccountNotFoundException {
        log.info("AccountService.depositBalance() method is called...");
        log.info("Balance to deposit : {}",depositAmount);
        Optional<Account> optionalAccount = getAccount(accountId);
        log.debug("Account retrieved : {}",optionalAccount.get());
        double currentBalance = optionalAccount.get().getBalance();
        log.info("CURRENT BALANCE : {}",currentBalance);
        double newBalance = currentBalance+depositAmount;
        log.info("NEW BALANCE : {}",newBalance);
        optionalAccount.get().setBalance(newBalance);
        Account accountSaved = accountRepository.save(optionalAccount.get());
        log.debug("Account saved : {}",accountSaved);
        return AccountMapper.mapAccountToAccountDto(accountSaved);
    }

    public AccountDto withdrawBalance(Integer accountId,double withdrawAmount) throws AccountNotFoundException, InsufficientBalanceException {
        log.info("AccountService.withdrawBalance() method is called...");
        log.info("AMOUNT TO WITHDRAW : {}",withdrawAmount);
        Optional<Account> optionalAccount = getAccount(accountId);
        log.debug("Account retrieved : {}",optionalAccount.get());
        double currentBalance = optionalAccount.get().getBalance();
        log.info("CURRENT BALANCE : {}",currentBalance);
         if(currentBalance<withdrawAmount){
             log.error("current balance is less than withdraw amount");
             throw new InsufficientBalanceException("Insufficient Balance,cannot withdraw");
         }

         double newBalance = currentBalance - withdrawAmount;
         log.info("NEW BALANCE : {}",newBalance);
         optionalAccount.get().setBalance(newBalance);
         Account accountSaved = accountRepository.save(optionalAccount.get());
         log.debug("Account updated and saved : {}",accountSaved);
         return AccountMapper.mapAccountToAccountDto(accountSaved);
    }

    private Optional<Account> getAccount(Integer accountId) throws AccountNotFoundException {
        log.info("AccountService.getAccount() method is called...");
        log.info("retrieving account for ID : {}",accountId);
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isEmpty()){
            log.error("account retrieving error");
            throw new AccountNotFoundException("Account doesn't exists");
        }
        return optionalAccount;
    }
}
