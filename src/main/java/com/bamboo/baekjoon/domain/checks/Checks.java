package com.bamboo.baekjoon.domain.checks;

import com.bamboo.baekjoon.domain.BaseTimeEntity;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.user.Users;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Checks extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "check_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CheckStatus status;

    @Column(nullable = true)
    private boolean success;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private FailureReason reason;

    @Column(nullable = true)
    private LocalDateTime runAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    @Override
    public String toString() {
        return "Checks{" +
                "id=" + id +
                ", status=" + status +
                ", success=" + success +
                ", reason=" + reason +
                ", runAt=" + runAt +
                ", user=" + user.getKorName() +
                ", term=" + term.getId() +
                '}';
    }

    public void changeCheck(CheckStatus status, Boolean success, FailureReason reason, Term term) {
        if (status != null) {
            this.status = status;
        }

        if (success != null) {
            this.success = success;
        }

        if (reason != null) {
            this.reason = reason;
        }

        if (term != null) {
            this.term = term;
        }
    }
}
