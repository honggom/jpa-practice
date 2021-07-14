package com.example.jpapractice.service;

import com.example.jpapractice.dto.User;
import com.example.jpapractice.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class EntityManagerTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User givenUser(String name, String email){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    @Test
    void entityManagerTest() {
        userRepository.saveAll(Lists.newArrayList(givenUser("hong", "hong@naver.com"), givenUser("gogo", "gogo@naver.com")));
        System.out.println(entityManager.createQuery("select u from User u").getResultList());
    }

    @Test
    void cacheFindTest() {
        userRepository.save(givenUser("hong", "hong@naver.com"));

        User user = userRepository.findById(1L).get();
        user.setName("hooooong");
        userRepository.save(user);

        System.out.println("--------------------------");

        user.setEmail("hoooooooooooooong@naver.com");
        userRepository.save(user);

        System.out.println(userRepository.findAll());
    }
}
