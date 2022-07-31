package com.bamboo.baekjoon.domain.user;

import com.bamboo.baekjoon.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class UserTierHistory extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "tier_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int beforeTier;
    private int afterTier;
}
