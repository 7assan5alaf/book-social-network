package com.hks.book.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.hks.book.entity.Token;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {

	      Optional<Token>findByToken(String token);
}
