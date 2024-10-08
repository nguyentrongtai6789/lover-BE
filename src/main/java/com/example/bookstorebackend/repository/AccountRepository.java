package com.example.bookstorebackend.repository;

import com.example.bookstorebackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface AccountRepository extends BaseRepository<Account>, JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String name);
    Account findByEmail (String email);

}
