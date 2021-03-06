package com.bamboo.baekjoon.domain.checks;

import com.bamboo.baekjoon.domain.BaseTimeEntity;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.user.Users;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    @OneToMany(mappedBy = "check")
    private List<CheckHistory> checkHistoryList = new ArrayList<>();

    public void setTerm(Term term) {
        if (this.term != null)
            this.term.getChecks().remove(this);

        this.term = term;
        term.getChecks().add(this);
    }

    @Builder
    public Checks(CheckStatus status, boolean success, FailureReason reason, Users user) {
        this.status = status;
        this.success = success;
        this.reason = reason;
        this.user = user;
    }

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

    public void admitCheck() {
        this.status = CheckStatus.COMPLETE;
        this.success = true;
        this.runAt = LocalDateTime.now();
    }

    public void failCheck(FailureReason reason) {
        this.status = CheckStatus.COMPLETE;
        this.success = false;
        this.reason = reason;
        this.runAt = LocalDateTime.now();
    }
}
