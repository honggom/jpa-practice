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

## 영속성 전이 (Cascade)
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private Publisher publisher;
```
- ex) 위 코드에서 @ManyToOne(cascade = CascadeType.PERSIST) 의미하는 바는 Book 입장에서
Publisher가 Persist(Insert)될 떄 영속성을 전이시키겠다는 의미, 다른 동작(update, delete 등)에서는
동작을 안 한다.
  
## OrphanRemoval (고아 제거)
    연관관계가 없는 Entity를 삭제한다.
    ex) Book dto에 필드 변수로 Publisher publisher이 있다.
        Book와 Publisher는 서로 연관을 지을 수 있음
- @OneToMany(orphanRemoval = true)
```java
@OneToMany(orphanRemoval = true)
@JoinColumn(name = "publisher_id")
@ToString.Exclude
private List<Book> books = new ArrayList<>();
```
<br>
- setter에 null을 주입하면?

```java
// .. jpa를 통해 Book Entity에 Publisher가 주입되어 있다고 가정
book.setPublisher(null);
bookRepository.save(book);
```
위와 같이 하면 연관관계는 제거 되지만, publisher는 존재함

## 커스텀 쿼리
    JPA의 쿼리 메서드의 네이밍 특징 때문에 경우에 따라 매우긴 이름의 쿼리 메서드를 
    정의해야 하는 경우가 생기는데, 이때 이런 점을 해결하고자 커스텀 쿼리를 사용할 수 있다.
- @Query 어노테이션을 Repository에 Method에 붙여서 사용

## Converter
    DB의 레코드를 자바객체로 Convert 해줌 필요할 때 찾아보기..

## Transaction
- Transaction내에서 RuntimeException(UnChecked)이 발생하면 Roll Back
- Transaction내에서 Checked Exception이 발생하면 Roll Back이 발생하지 않음
  - Checked Exception은 전적으로 개발자 책임으로 간주
  - 만약 Checked Exception이 발생시 Roll Back를 발생시키길 원하면
    ```java
    @Transactional(rollbackFor = Exception.class)
    ```
    위 어노테이션을 사용하면 됨
- @Transactional 어노테이션이 붙은 함수를 직접 사용하지 않고 해당 함수를 콜해서 사용하면 @Transactional이 무시됨

### Transaction 격리 (Isolation) 단계
- @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  - 커밋이 안 된 데이터도 읽을 수 있음, 데이터를 읽을 수 있는
    이유가 jpa를 통해 Entity를 update하기 때문에 해당 Transaciton사이에서 Entity 자체는 실제로 update가 된 상태가 된다. 
    따라서 데이터 변질이 될 가능성이 높음 사용시 많은 주의가 필요하다.
- @Transactional(isolation = Isolation.READ_COMMITTED)
  - 커밋된 데이터만 읽는다.
- @Transactional(isolation = Isolation.REPEATABLE_READ)
  - UNREPEATABLE_READ 상태 : 반복된 조회 등으로 인해 예상되는 값이 변경되어 예상치 못한 값이 조회되는 상태
  - REPEATABLE_READ : 반복해서 값을 조회해도 항상 동일한 값이 리턴되는 것을 보장한다.
- @Transactional(isolation = Isolation.SERIALIZABLE)
  - phantom read를 방지하기 위해 사용
  - 다른쪽 Transaction이 commit돼야 다음 로직이 실행됨

### Transaction 전파 (Propagation) 단계
- @Transactional(propagation = Propagation.REQUIRED) (DEFAULT) : @Transactional의 기본값으로 트랜잭션이 있으면 그 트랜잭션을 사용하고 없으면 트랜잭션을 만들어서 사용한다.
jpa의 save 메서드도 @Transactional 어노테이션이 선언되어 있는데 따라서 해당 save 메서드는 각 save 메서드마다 트랜잭션을 가지고 있다. 
- @Transactional(propagation = Propagation.REQUIRES_NEW) : 트랜잭션을 별도로 만들어서 사용한다.
- @Transactional(propagation = Propagation.NESTED) : 상위 트랜잭션에 종속 적이지만 NESTED 트랜잭션은 상위에 영향을 주지 않는다.
- @Transactional(propagation = Propagation.SUPPORTS) : 상위 트랜잭션이 있으면 그 트랜잭션을 사용하고, 없다면 트랜잭션 없이 동작한다.
- @Transactional(propagation = Propagation.NOT_SUPPORTED) : 상위 트랜잭션이 있고 해당 트랜잭션에 접근하면 작업을 멈추고 상위 트랜잭션이 끝나면 동작한다.
- @Transactional(propagation = Propagation.MANDATORY) : 필수적으로 상위 트랜잭션이 있어야 된다. 없다면 오류를 발생시킨다.
- @Transactional(propagation = Propagation.NEVER) : 상위 트랙잭션이 없어야 된다. 있다면 오류를 발생시킨다.

### Transaction 스코프
- 메서드 : 메서드의 시작과 끝, 우선순위가 더 높음 (ex : 메서드의 트랜잭션이 있고 해당 메서드의 클래스에 트랜잭션이 있으면 메서드의 트랜잭션만 동작)
- 클래스 : 각 메서드의 시작과 끝