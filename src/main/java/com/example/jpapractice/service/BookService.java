package com.example.jpapractice.service;
import com.example.jpapractice.dto.Author;
import com.example.jpapractice.dto.Book;
import com.example.jpapractice.repository.AuthorRepository;
import com.example.jpapractice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void putBookAndAuthor() {
        Book book = new Book();
        book.setName("start jpa");
        bookRepository.save(book);

        authorService.putAuthor();

        throw new RuntimeException("에러 발생");
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void get(Long id) {
        System.out.println(">>> "+bookRepository.findById(id));
        System.out.println(">>> "+bookRepository.findAll());

        System.out.println(">>> "+bookRepository.findById(id));
        System.out.println(">>> "+bookRepository.findAll());
    }

}
