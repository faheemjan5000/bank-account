package com.account.account.app.service;

import com.account.account.app.dto.AccountDto;
import com.account.account.app.entity.Account;
import com.account.account.app.exception.AccountNotFoundException;
import com.account.account.app.exception.DublicateIbanException;
import com.account.account.app.exception.InsufficientBalanceException;
import com.account.account.app.exception.NoAccountFoundException;
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

    public AccountDto saveAccount(AccountDto accountDto) throws DublicateIbanException {
        log.info("AccountService.saveAccount() method is called...");
        log.info("checking if IBAN already exists...");
        log.info("IBAN : {}",accountDto.getIban());
        if(this.getAllIbanNumbers().contains(accountDto.getIban())){
            log.error("IBAN already exists");
            throw new DublicateIbanException("IBAN already exists,plz use a different IBAN number");
        }
        log.error("IBAN duplication test passed.");
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

    public List<AccountDto> getAllAccounts() throws NoAccountFoundException {
        log.info("AccountService.getAllAccounts() method is called...");
        List<Account> accounts = accountRepository.findAll();
        if(accounts.isEmpty()){
            log.error("There is no account exist in database");
            throw new NoAccountFoundException("No account exists in database");
        }
        log.debug("all accounts retrieved from database : {}",accounts);
        return accounts.stream()
                .map(account -> new AccountDto(account.getId(),
                        account.getAccountHolderName(),
                        account.getBalance(),account.getIban()))
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

    public AccountDto findByAccountHolderName(String accountholder) throws AccountNotFoundException {
        log.info("AccountService.findByAccountHolderName() method is calling...");
        log.info("Retrieving account by Name : {}",accountholder);
         Account accountRetrieved  = accountRepository.findByAccountHolderName(accountholder);
         if(accountRetrieved == null){
             log.error("Account retrieving error");
             throw new AccountNotFoundException("account doesn't exist by name : "+accountholder);
         }
         log.info("Account retrieved by Name : {}",accountRetrieved);
         return AccountMapper.mapAccountToAccountDto(accountRetrieved);
    }

    public AccountDto findByIban(String iban) throws AccountNotFoundException {
        log.info("AccountService.findByIban() method is calling...");
        log.info("Retrieving account by iban : {}",iban);
        Account accountRetrieved  = accountRepository.findByIban(iban);
        if(accountRetrieved == null){
            log.error("Account retrieving error");
            throw new AccountNotFoundException("Account not found for IBAN : "+iban);
        }
        log.info("Account retrieved by IBAN : {}",accountRetrieved);
        return AccountMapper.mapAccountToAccountDto(accountRetrieved);
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

    public void deleteById(Integer accountId) throws AccountNotFoundException {
        log.info("AccountService.deleteById() method is called...");
        Optional<Account> optionalAccount = this.getAccount(accountId);
        log.info("account to be deleted : {}",optionalAccount.get());
        accountRepository.deleteById(accountId);
        log.info("account deleted successfully");

    }

    public String getIbanNumberById(Integer accountId) throws AccountNotFoundException {
        log.info("AccountService.getIbanNumberById() method is called...");
        log.info("Account ID : {}",accountId);
        Optional<Account> accountRetrieved =  this.getAccount(accountId);
        log.info("IBAN retrieved : {}",accountRetrieved.get().getIban());
        return accountRetrieved.get().getIban();
    }

    public List<String> getAllIbanNumbers(){
        log.info("AccountService.getAllIbanNumbers() method is called...");
        return accountRepository.getAllIbanNumbers();
    }

}
