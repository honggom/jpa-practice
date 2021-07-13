package com.example.jpapractice.repository;

import com.example.jpapractice.dto.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
