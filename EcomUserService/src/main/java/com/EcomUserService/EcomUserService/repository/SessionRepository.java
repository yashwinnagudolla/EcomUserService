package com.EcomUserService.EcomUserService.repository;

import com.EcomUserService.EcomUserService.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findById(Long SessionId);
    Optional<Session> findByTokenAndUserId(String token, Long userId);
}
