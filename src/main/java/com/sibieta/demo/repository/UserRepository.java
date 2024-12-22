package com.sibieta.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sibieta.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
