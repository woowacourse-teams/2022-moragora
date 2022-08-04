package com.woowacourse.moragora.support;

import com.woowacourse.moragora.entity.Meeting;
import java.time.LocalDate;

public enum MeetingFixtures {

    MORAGORA("모라고라",
            LocalDate.of(2022, 8, 1),
            LocalDate.of(2022, 8, 19)
    ),
    SOKDAKSOKDAK("속닥속닥",
            LocalDate.of(2022, 8, 1),
            LocalDate.of(2022, 8, 19)
    ),
    F12("F12",
            LocalDate.of(2022, 8, 1),
            LocalDate.of(2022, 8, 19)
    ),
    TEATIME("티타임",
            LocalDate.of(2022, 8, 1),
            LocalDate.of(2022, 8, 19)
    );

    private final String name;

    private final LocalDate startDate;

    private final LocalDate endDate;

    MeetingFixtures(final String name, final LocalDate startDate, final LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Meeting create() {
        return Meeting.builder()
                .name(this.name)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .build();
    }
}
