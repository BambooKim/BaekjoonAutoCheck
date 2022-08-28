package com.bamboo.baekjoon.domain.checks;

import com.bamboo.baekjoon.domain.user.User;

public class JudgePolicy {

    public static boolean isSubmitPass(User user, int probTier) {
        if (user.getEnterYear() == 2022) {
            return true;
        } else {
            if (probTier >= (user.getUserTier() - 5)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
