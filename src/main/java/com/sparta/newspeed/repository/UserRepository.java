package com.sparta.newspeed.repository;

import com.sparta.newspeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    //인증이 된 유저만 이름으로 조회
    Optional<User> findByUsernameAndIsConfirmIsTrue(String username);
    Optional<User> findByEmail(String email);
}
