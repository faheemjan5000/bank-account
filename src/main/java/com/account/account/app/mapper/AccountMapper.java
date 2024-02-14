package com.account.account.app.mapper;

import com.account.account.app.dto.AccountDto;
import com.account.account.app.entity.Account;

public class AccountMapper {

    public static AccountDto mapAccountToAccountDto(Account account){
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountHolderName(account.getAccountHolderName());
        accountDto.setId(account.getId());
        accountDto.setBalance(account.getBalance());

        return accountDto;
    }

    public static Account mapAccountDtoToAccount(AccountDto accountDto){
        Account account = new Account();
        account.setAccountHolderName(accountDto.getAccountHolderName());
        account.setBalance(accountDto.getBalance());
        account.setId(accountDto.getId());

        return account;
    }
}
