package com.bamboo.baekjoon.domain.checks;

import com.bamboo.baekjoon.domain.user.User;

public class JudgePolicy {

    public static boolean isSubmitPass(User user, int probTier) {
        if (user.getEnterYear() == 2022) {      // 22학번
            return true;
        } else {                                // ~21학번
            if (user.getUserTier() <= 5) {      // B5 ~ B1
                return true;
            } else {                            // S5 ~
                if (probTier >= (user.getUserTier() - 5)) {
                    if (probTier <= 5)
                        return false;
                    else
                        return true;
                } else {
                    return false;
                }
            }
        }
    }
}
