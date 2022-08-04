package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.user.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CoffeeStatResponse {

    private final Long id;
    private final String nickname;
    private final Long coffeeCount;

    public CoffeeStatResponse(final Long id, final String nickname, final Long coffeeCount) {
        this.id = id;
        this.nickname = nickname;
        this.coffeeCount = coffeeCount;
    }

    public static CoffeeStatResponse of(final User user, final Long coffeeCount) {
        return new CoffeeStatResponse(
                user.getId(), user.getNickname(), coffeeCount
        );
    }
}
