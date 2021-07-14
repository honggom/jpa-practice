package com.example.jpapractice.service;

import com.example.jpapractice.repository.AuthorRepository;
import com.example.jpapractice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void transactionTest() {
        bookService.putBookAndAuthor();

        System.out.println("books : "+bookRepository.findAll());
        System.out.println("authors : "+authorRepository.findAll());
    }

}