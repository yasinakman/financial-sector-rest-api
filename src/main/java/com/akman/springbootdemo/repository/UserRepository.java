package com.akman.springbootdemo.repository;

import com.akman.springbootdemo.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
