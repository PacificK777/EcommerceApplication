package org.example.userservice.Repository;

import org.example.userservice.Models.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Tokens, Long> {
    Optional<Tokens> findByTokenValueAndIsDeleted(String token, boolean isDeleted);

    Optional<Tokens> findByTokenValueAndIsDeletedAndExpiryAtGreaterThan(String token, boolean isDeleted, Date expiryAt);
}
