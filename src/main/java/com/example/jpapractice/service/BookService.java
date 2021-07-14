package com.example.jpapractice.service;
import com.example.jpapractice.dto.Author;
import com.example.jpapractice.dto.Book;
import com.example.jpapractice.repository.AuthorRepository;
import com.example.jpapractice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public void putBookAndAuthor() {
        Book book = new Book();
        book.setName("start jpa");
        bookRepository.save(book);

        Author author = new Author();
        author.setName("hong");
        authorRepository.save(author);
    }
}
