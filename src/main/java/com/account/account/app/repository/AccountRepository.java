package com.account.account.app.repository;

import com.account.account.app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    Account findByAccountHolderName(String accountHolderName);
    Account findByIban(String iban);

    @Query("SELECT u.iban FROM Account u")  //not native query
    List<String> getAllIbanNumbers();

}
