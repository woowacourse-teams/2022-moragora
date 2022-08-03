package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.user.User;
import lombok.Getter;

@Getter
public class CoffeeStatResponse {

    private final Long id;
    private final String nickname;
    private final int coffeeCount;

    public CoffeeStatResponse(final Long id, final String nickname, final int coffeeCount) {
        this.id = id;
        this.nickname = nickname;
        this.coffeeCount = coffeeCount;
    }

    public static CoffeeStatResponse of(final User user, final int coffeeCount) {
        return new CoffeeStatResponse(
                user.getId(), user.getNickname(), coffeeCount
        );
    }
}
