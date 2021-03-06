package com.example.jpapractice.repository;

import com.example.jpapractice.dto.Address;
import com.example.jpapractice.dto.Gender;
import com.example.jpapractice.dto.User;
import com.example.jpapractice.dto.UserHistory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Test
    void crud() {
        userRepository.findAll().forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud2() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        users.forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud3() {
        List<User> users = userRepository.findAllById(Lists.newArrayList(1L, 3L, 5L));
        users.forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud4() {
        User user1 = new User("jack", "jack@naver.com");
        User user2 = new User("steve", "steve@naver.com");

        userRepository.saveAll(Lists.newArrayList(user1, user2));
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud5() {
        User user = userRepository.findById(1L).orElse(null);
        logger.info("user : " + user);
    }

    @Test
    void crud6() {
        userRepository.saveAndFlush(new User("newHong", "newHong@naver.com"));
        userRepository.findAll().forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud7() {
        long count = userRepository.count();

        logger.info("count---------");
        logger.info("count : " + count);
        logger.info("count---------");
    }

    @Test
    void crud8() {
        boolean exists = userRepository.existsById(1L);

        logger.info("exists");
        logger.info("exists : " + exists);
        logger.info("exists");
    }

    @Test
    void crud9() {
        userRepository.delete(userRepository.findById(1L).orElseThrow(RuntimeException::new));
    }

    @Test
    void crud10() {
        userRepository.deleteById(1L);
        userRepository.findAll().forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud11() {
        userRepository.deleteAll();
        userRepository.findAll().forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud12() {
        // ??? ???????????? select 3??? ?????????
        // findAllById ??? ???
        // deleteAll ??? ??? ?????? ??? ???
        // ????????? ???????????????
        userRepository.deleteAll(userRepository.findAllById(Lists.newArrayList(1L, 3L)));
        userRepository.findAll().forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud13() {
        // ??? ????????? crud12??? ?????? ???????????? select??? 2??? ?????????
        // findAllById ??? ???
        // deleteInBatch ??? ???
        userRepository.deleteInBatch(userRepository.findAllById(Lists.newArrayList(1L, 3L)));
        userRepository.findAll().forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud14() {
        // ?????? ???????????? ?????? ??????
        // deleteAllInBatch??? ??? ?????? ????????? ??? ??????
        userRepository.deleteAllInBatch();
        userRepository.findAll().forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud15() {
        Page<User> users = userRepository.findAll(PageRequest.of(0, 3));

        logger.info("page : " + users);
        logger.info("totalElements : " + users.getTotalElements());
        logger.info("totalPages : " + users.getTotalPages());
        logger.info("numberOfElements : " + users.getNumberOfElements());
        logger.info("sort : " + users.getSort());
        logger.info("size : " + users.getSize());

        users.forEach(user -> {
            logger.info("user : " + user);
        });
    }

    @Test
    void crud16() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("name")
                .withMatcher("email", endsWith());

        Example<User> example = Example.of(new User("mo", "mo@nvaer.com"), matcher);

        userRepository.findAll(example).forEach(
                user -> {
                    logger.info("user : " + user);
                }
        );
    }

    @Test
    void crud17() {
        userRepository.save(new User("bong", "bong@naver.com"));

        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setEmail("changed@naver.com");

        userRepository.save(user);

        List<User> users = userRepository.findAll();
        users.forEach(
                u -> {
                    logger.info("user : " + u);
                }
        );
    }

    @Test
    void crud18() {
        logger.info("user : " + userRepository.findByName("aaa"));
    }

    @Test
    void crud19() {
        //????????? ??????????????? JPA??? ????????? ?????? ?????? ??? ??????
        //?????? ?????? ???????????? ??????
        logger.info("findByEmail : " + userRepository.findByEmail("hong@naver.com"));
        logger.info("getByEmail : " + userRepository.getByEmail("hong@naver.com"));
        logger.info("readByEmail : " + userRepository.readByEmail("hong@naver.com"));
        logger.info("queryByEmail : " + userRepository.queryByEmail("hong@naver.com"));
        logger.info("searchByEmail : " + userRepository.searchByEmail("hong@naver.com"));
        logger.info("streamByEmail : " + userRepository.streamByEmail("hong@naver.com"));
        logger.info("findUserByEmail : " + userRepository.findUserByEmail("hong@naver.com"));
    }

    @Test
    void crud20() {
        logger.info("something : " + userRepository.findSomethingByEmail("hong@naver.com"));
    }

    @Test
    void crud21() {
        logger.info("findByEmailAndName : " + userRepository.findByEmailAndName("hong@naver.com", "hong"));
    }

    @Test
    void crud22() {
        logger.info("findByNameStartingWith : " + userRepository.findByNameStartingWith("ho"));
        logger.info("findByNameEndingWith : " + userRepository.findByNameEndingWith("d"));
        logger.info("findByNameContains : " + userRepository.findByNameContains("o"));
    }

    @Test
    void crud23() {
        logger.info("findByNameLike : " + userRepository.findByNameLike("o"));
        logger.info("findByNameLike : " + userRepository.findByNameLike("%o%"));
    }

    @Test
    void crud24() {
        logger.info("findTop1ByName : " + userRepository.findTop1ByName("hong"));
    }

    @Test
    void crud25() {
        logger.info("findLast1ByName : " + userRepository.findLast1ByName("hong"));
    }

    @Test
    void paging() {
        logger.info("findByName : " + userRepository.findByName("hong", PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")))).getContent());
    }

    @Test
    void insertAndUpdateTest() {
        //User???
        //@Column(updatable = false)
        //@Column(insertable = false)
        //test
        User user = new User();
        user.setName("hong");
        user.setEmail("hong@naver.com");

        userRepository.save(user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("hoooooooooong");

        userRepository.save(user2);
    }

    @Test
    void enumTest() {
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setGender(Gender.MALE);

        userRepository.save(user);

        userRepository.findAll().forEach(
                u -> {
                    logger.info("user : " + u);
                }
        );
        logger.info("Map : " + userRepository.findRowRecord().get("name"));
        logger.info("Native Query : " + userRepository.findRowRecord().get("gender"));
    }

    @Test
    void listenerTest() {
        User user = new User();
        user.setEmail("martin2@naver.com");
        user.setName("martin");

        userRepository.save(user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("marrrrrrrtin");

        userRepository.save(user2);

        userRepository.deleteById(4L);
    }

    @Test
    void prePersistTest() {
        User user = new User();
        user.setEmail("hong2@naver.com");
        user.setName("hong");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        logger.info("-----------------------");
        logger.info("prePersistTest : " + userRepository.findByEmail("hong2@naver.com"));
        logger.info("-----------------------");
    }

    @Test
    void userHistoryTest() {
        User user = new User();
        user.setEmail("martin-new@naver.com");
        user.setName("martin-new");

        userRepository.save(user);

        user.setName("martin-new-new");

        userRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);
    }

    @Test
    void userRelationTest() {
        User user = new User();
        user.setName("david");
        user.setEmail("david@naver.com");
        user.setGender(Gender.MALE);
        userRepository.save(user);

        user.setName("daniel");
        userRepository.save(user);

        user.setEmail("daniel@naver.com");
        userRepository.save(user);

        //userHistoryRepository.findAll().forEach(System.out::println);

        //List<UserHistory> userHistories = userHistoryRepository.findByUserId(
        //userRepository.findByEmail("daniel@naver.com").getId()
        //);

        List<UserHistory> userHistories = userRepository.findByEmail("daniel@naver.com").getUserHistories();

        userHistories.forEach(System.out::println);

        System.out.println("UserHistory.getUser() : "+userHistoryRepository.findAll().get(0).getUser());
    }

    @Test
    void embedTest() {
        userRepository.findAll().forEach(System.out::println);
        User user = new User();
        user.setName("steve");
        user.setHomeAddress(new Address("???????????????", "??????", "????????? 168-16", "34544"));
        user.setCompanyAddress(new Address("???????????????", "?????????", "????????? 493-17", "102???"));

        userRepository.save(user);

        userRepository.findAll().forEach(System.out::println);
    }

}














































