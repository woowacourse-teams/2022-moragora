package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.user.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class CoffeeStatsResponse {

    private final List<CoffeeStatResponse> userCoffeeStats;

    public CoffeeStatsResponse(final List<CoffeeStatResponse> userCoffeeStats) {
        this.userCoffeeStats = List.copyOf(userCoffeeStats);
    }

    public static CoffeeStatsResponse from(Map<User, Long> userCoffeeStats) {
        final List<CoffeeStatResponse> coffeeStatResponses = userCoffeeStats.keySet().stream()
                .map(user -> CoffeeStatResponse.of(user, userCoffeeStats.get(user).intValue()))
                .collect(Collectors.toList());
        return new CoffeeStatsResponse(coffeeStatResponses);
    }
}
