package com.bank.demo.domain.account;

import com.bank.demo.domain.account.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "account_tb")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 20)
    private Long number; // 계좌 번호
    @Column(nullable = false, length = 4)
    private long password; // 계좌 비번
    @Column(nullable = false) // 잔액 기본 1000원
    private long balance;
    // 항상 ORM에서 FK의 주인은 Many Entity 쪽이다.
    @ManyToOne(fetch = FetchType.LAZY) // account.getUser().아무필드호출() == Lazy 발동
    private User user;
    @CreatedDate // @EntityListeners, @EnableJpaAuditing require
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // @EntityListeners @EnableJpaAuditing require
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Account(Long id, Long number, long password, long balance, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
