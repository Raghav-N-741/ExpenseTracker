package com.raghav.expense.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raghav.expense.model.User;

@Repository 
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);
}
