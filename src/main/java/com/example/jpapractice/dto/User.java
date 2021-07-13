package com.example.jpapractice.dto;

import com.example.jpapractice.dto.listener.UserEntityListener;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@EntityListeners(value = UserEntityListener.class)
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //생성 방법을 db에 맡기겠다.
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ToString.Exclude
    // User 입장에서 UserHistory를 수정하거나 삽입하면 안 됨 따라서 위와 같이
    // insertable = false, updatable = false 옵션을 줘서
    // User 입장에서 UserHistory를 read-only로 만듬
    private List<UserHistory> userHistories;

    @OneToMany
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Review> reviews;

    /*
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Transient
    private String testData;

    @PrePersist
    public void prePersist() {
        System.out.println(">>> prePersist");
    }

    @PostPersist
    public void postPersist() {
        System.out.println(">>> postPersist");
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println(">>> preUpdate");
    }

    @PostUpdate
    public void postUpdate() {
        System.out.println(">>> postUpdate");
    }

    @PreRemove
    public void preRemove() {
        System.out.println(">>> preRemove");
    }

    @PostRemove
    public void postRemove() {
        System.out.println(">>> postRemove");
    }

    @PostLoad
    public void postLoad() {
        System.out.println(">>> postLoad");
    }
    */

    /**
     * PrePersist, PreUpdate 사용 예
     *
     *
     *     @PrePersist
     *     public void prePersist() {
     *         this.createdAt = LocalDateTime.now();
     *         this.updatedAt = LocalDateTime.now();
     *     }
     *
     *     @PreUpdate
     *     public void preUpdate() {
     *         this.updatedAt = LocalDateTime.now();
     *     }
     *
     *
     */

}
