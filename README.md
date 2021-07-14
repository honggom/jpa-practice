## ORM (Object-Relation Mapping / 객체 관계 매핑)
    ORM은 결국 자바 객체와 디비 레코드와의 연결 관계를 맺어주는 것이므로 최종 동작하는 것은 쿼리문이다.
- 객체를 통해 간접적으로 디비 데이터를 다룬다.
- 객체와 디비의 데이터를 자동으로 매핑해준다.
## JPA (Java Persistence API)
- 자바 ORM 기술에 대한 표준 명세, 자바에서 제공하는 API
- 자바 어플리케이션에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스

## 쿼리 메소드
    쿼리 메소드 기능은 스프링 데이터 JPA가 제공하는 특별한 기능이다. 크게 3가지 기능이 있다.

- 메소드 이름으로 쿼리 생성
- 메소드 이름으로 JPA NamedQuery 호출
- @Query 어노테이션을 사용하여 레포지토리 인터페이스에 쿼리 직접 정의
이 기능들을 활용하면 인터페이스만으로 필요한 대부분의 쿼리 기능을 개발할 수 있다.

## Entity의 기본속성 (Annotation)
코드 예시 :
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //생성 방법을 db에 맡기겠다.
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Transient
    private String testData;

}
```
- @Entity : JPA에서 관리하고 있는 Entity 객체임을 정의함
    - Entity로 지정시 PK가 반드시 필요함 -> @Id Annotation으로 지정 가능 
- @GeneratedValue : 개발자가 아닌 JPA에게 키 값 생성 역할을 넘김
  - GenerationType 옵션 (ex : @GeneratedValue(strategy = GenerationType.IDENTITY)) :
    - TABLE : DB종류에 상관없이 ID 값을 관리하는 별도의 테이블을 생성하고 그 테이블에서 추출해서 사용 
    - SEQUENCE : SEQUENCE를 사용하는 DB에서 활용 가능 / SEQUENCE에서 키 값을 받음 (Oracle, PostgreSQL, H2 등)  
    - IDENTITY : 사용되는 DB의 기능 활용 (ex : MySQL의 AUTO_INCREMENT)
    - AUTO : 각 DB에 적합한 값을 자동으로 넘겨줌 (Default)
- @Column : 각 컬럼마다 다양한 옵션 지정 가능
- @Transient : 해당 필드는 영속성 처리에서 제외됨 -> DB의 레코드로써 사용하는 것이 아닌 자바의 객체로 사용
- @Enumerated(value = EnumType.STRING) : Enum 객체 사용시 Odinal (서순)이 DB에 저장되거나 하는 문제를 방지

## Entity의 Listener
    Entity의 시점 select, insert, update, delete에 원하는 기능을 적용할 수 있음 
    개인적으로 Entity 버전 AOP 같은 느낌..

- @PrePersist : Insert 전에 동작
- @PreUpdate : Update 전에 동작 
- @PreRemove : Delete 전에 동작
- @PostPersist : Insert 후에 동작
- @PostUpdate : Update 후에 동작
- @PostRemove : Delete 후에 동작
- @PostLoad : Select 후에 동작

### @EntityListeners(value = AuditingEntityListener.class)
- JPA의 Entity에 대한 동일한 기능을 만들기 유용함

## 연관 관계 정의 규칙
(참조 : https://jeong-pro.tistory.com/231)
- 방향 : 단방향, 양방향 (객체 참조)
  - DB 테이블은 외래키 하나로 양쪽 테이블 조인이 가능하지만 jpa는 그렇지 않음
  - 두 객체 사이에 하나의 객체만 참조용 필드를 갖고 참조하면 단방향 관계, 두 객체 모드가 각각 참조용
  필드를 갖고 참조하면 양방향 관계
- 연관 관계의 주인 : 양방향일 때, 연관 관계에서 관리 주체
- 다중성 : 다대일(N:1), 일대다(1:N), 일대일(1:1), 다대다(N:N)

## JPA 연관 관계 어노테이션
    ```java
    @Entity
    @NoArgsConstructor
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public class Book extends BaseEntity {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        private String name;
    
        private String category;
    
        private Long authorId;
    
        //private Long publisherId;
    
        @OneToOne(mappedBy = "book")
        @ToString.Exclude
        private BookReviewInfo bookReviewInfo;
    
        @OneToMany
        @JoinColumn(name = "book_id")
        @ToString.Exclude
        private List<Review> reviews;
    
        @ManyToOne
        @ToString.Exclude
        private Publisher publisher;
    }
    ```
- @OneToOne : 1:1 관계를 정의
  ```
  위 코드 @OneToOne에서 옵션으로 
  (mappedBy = {해당 클래스})가 정의되어 있는데,
  해당 옵션이 의미하는 바는 Book와 BookReviewInfo끼리 1:1 관계를 형성하되,
  Book에 Foreign Key를 정의하지 않겠다는 의미이다.
  ```
- @OneToMany : 1:N 관계를 정의
  ```
  위 코드에서 @JoinColumn(name = "book_id")가 의미하는 것은
  우선 1:N 관계 중 1이 Book을 의미하고 N은 Review를 의미하는데,
  1:N 관계를 표현하기 위해 RDB 상에서는 Review 측에 Foreign Key가 필요한데
  JPA에서 해당 Foreign Key를 "book_id"로 네이밍하고 연관 관계를 짓겠다는 의미이다.
  ```
- @ManyToOne : N:1 관계를 정의
- @ManyToMany : N:N 관계를 정의
  ```
  기본적으로 N:N 관계는 실무에서 잘 사용하지 않음
  ```
### application.yml 설정
    hibernate:
      ddl-auto: 설정값
      #none : ddl을 실행 안 함
      #create : drop 후 create
      #create-drop : create 후 종료 시 drop
      #update : 실제 스키마와 비교하여 변경된 부분만 반영
      #validate : 단순 비교작업만 하고 Entity와 실제 스키마가 다르면 오류를 발생시킴
- generate-ddl, ddl-auto의 차이
  - generate-ddl : jpa의 하위 속성, 구현체와 상관없이 자동화된 ddl을 사용할 수 있게 해줌 (false가 default)
  - ddl-auto : ddl-auto가 설정되면 generate-ddl 옵션은 무시됨
## 영속성 컨텍스트 (Persistence Context)
    Entity를 관리하는 컨테이너
- 영속성 캐시 : 영속성 컨텍스트가 지닌 캐시
    - 영속성 캐시가 flush 돼서 DB에 반영되는 시점 :
      1. flush()를 명시적으로 사용하는 시점
      2. 트랜잭션이 끝나서 해당 쿼리가 커밋되는 시점
      3. 복잡한 조회 조건에 jpql 쿼리가 실행되는 시점 
- Repository.flush() : 영속성 캐시에 쌓여서 아직 반영되지 않은 Entity의 변경을 해당 시점에 모두 반영시킴
- Jpa의 save()를 통해 명시적으로 Entity의 변화를 DB에 적용시킬 수 있지만 save()를 명시하지
않더라고 트랜잭션이 종료되면 Entity의 변경 내용이 커밋(flush)됨
  
## 영속 상태
- 영속 (managed) : EntityManager가 Entity를 관리하는 상태 / 영속성 컨텍스트에 Entity가 저장된 상태
- 준영속 (detached) : EntityManager가 Entity를 관리하지 않는 상태 / 영속성 컨텍스트에 저장되었다가 분리된 상태 
- 비영속 (new) : DB와 관련이 없는 순수 Java Object 상태 / 영속성 컨텍스트와 관련이 없는 상태

## 영속 상태 조작 
- @Trasient : 해당 필드는 영속화에서 제외됨, 영속성 컨텍스트에서 관리를 안 한다.
- EntityManager.persist(target) : target을 영속화(managed 상태)한다.
- EntityManager.detach(target) : target을 준영속화(detached 상태)한다.
- EntityManager.merge(target) : 준영속화(detached 상태)된 target을 다시 영속화(managed 상태) 한다.
- EntityManager.remove(target) : target을 삭제한다.

## EntityManager
    쿼리메서드나 SimpleJpaRepository의 내부적인 실제 동작은 EntityManager에의해 동작한다.







