package com.example.jpapractice.repository;

import com.example.jpapractice.dto.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
