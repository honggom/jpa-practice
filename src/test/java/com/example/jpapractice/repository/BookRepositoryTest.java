package com.example.jpapractice.repository;

import com.example.jpapractice.dto.Book;
import com.example.jpapractice.dto.Publisher;
import com.example.jpapractice.dto.Review;
import com.example.jpapractice.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void bookTest() {
        Book book = new Book();
        book.setName("JpaBook");
        book.setAuthorId(1L);
        //book.setPublisherId(1L);

        bookRepository.save(book);

        System.out.println(bookRepository.findAll());
    }

    @Test
    @Transactional
    void bookRelationTest() {
        givenBookAndReview();

        User user = userRepository.findByEmail("hong@naver.com");

        System.out.println("Review : "+user.getReviews());
        System.out.println("Book : "+user.getReviews().get(0).getBook());
        System.out.println("Publisher : "+user.getReviews().get(0).getBook().getPublisher());
    }

    private void givenBookAndReview(){
        givenReview(givenUser(), givenBook(givenPublisher()));
    }

    private User givenUser(){
        return userRepository.findByEmail("hong@naver.com");
    }

    private Book givenBook(Publisher publisher){
        Book book = new Book();
        book.setName("JPA BOOK");
        book.setPublisher(publisher);

        return bookRepository.save(book);
    }

    private void givenReview(User user, Book book){
        Review review = new Review();
        review.setTitle("책입니다ㅏㅏㅏ");
        review.setContent("내용입니다ㅏㅏㅏㅏ");
        review.setScore(5.0f);
        review.setUser(user);
        review.setBook(book);

        reviewRepository.save(review);
    }

    private Publisher givenPublisher(){
        Publisher publisher = new Publisher();
        publisher.setName("홍");

        return publisherRepository.save(publisher);
    }

}
















