package com.account.account.app.service;

import com.account.account.app.dto.AccountDto;
import com.account.account.app.entity.Account;
import com.account.account.app.exception.AccountNotFoundException;
import com.account.account.app.exception.InsufficientBalanceException;
import com.account.account.app.mapper.AccountMapper;
import com.account.account.app.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AccountService {


    private AccountRepository accountRepository;

    public AccountDto saveAccount(AccountDto accountDto){
        Account account = AccountMapper.mapAccountDtoToAccount(accountDto);
        Account accountSaved = accountRepository.save(account);

        return AccountMapper.mapAccountToAccountDto(accountSaved);
    }

    public AccountDto getAccountById(Integer accountId) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isEmpty()){
            throw new AccountNotFoundException("account does not exists");
        }
        return AccountMapper.mapAccountToAccountDto(optionalAccount.get());
    }

    public List<AccountDto> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();
        List<AccountDto> accountDtoList = accounts.stream()
                .map(account -> new AccountDto(account.getId(),
                        account.getAccountHolderName(),
                        account.getBalance()))
                .toList();

        return  accountDtoList;
    }

    public double getCurrentBalance(Integer accountId) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isEmpty()){
            throw new AccountNotFoundException("Account doesn't exists");
        }
        return optionalAccount.get().getBalance();
    }

    public AccountDto depositBalance(Integer accountId,double depositAmount) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isEmpty()){
            throw new AccountNotFoundException("Account doesn't exists");
        }
        double currentBalance = optionalAccount.get().getBalance();
        double newBalance = currentBalance+depositAmount;
        optionalAccount.get().setBalance(newBalance);
        Account accountSaved = accountRepository.save(optionalAccount.get());

        return AccountMapper.mapAccountToAccountDto(accountSaved);
    }

    public AccountDto withdrawBalance(Integer accountId,double withdrawAmount) throws AccountNotFoundException, InsufficientBalanceException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isEmpty()){
            throw new AccountNotFoundException("Account doesn't exists");
        }

        double currentBalance = optionalAccount.get().getBalance();
         if(currentBalance<withdrawAmount){
             throw new InsufficientBalanceException("Insufficient Balance,cannot withdraw");
         }

         currentBalance = currentBalance - withdrawAmount;
         optionalAccount.get().setBalance(currentBalance);
         Account accountSaved = accountRepository.save(optionalAccount.get());

         return AccountMapper.mapAccountToAccountDto(accountSaved);
    }
}
