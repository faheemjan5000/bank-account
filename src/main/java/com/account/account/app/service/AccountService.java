package com.account.account.app.service;

import com.account.account.app.dto.AccountDto;
import com.account.account.app.entity.Account;
import com.account.account.app.mapper.AccountMapper;
import com.account.account.app.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountService {


    private AccountRepository accountRepository;

    public AccountDto saveAccount(AccountDto accountDto){
        Account account = AccountMapper.mapAccountDtoToAccount(accountDto);
        Account accountSaved = accountRepository.save(account);

        return AccountMapper.mapAccountToAccountDto(accountSaved);
    }
}
