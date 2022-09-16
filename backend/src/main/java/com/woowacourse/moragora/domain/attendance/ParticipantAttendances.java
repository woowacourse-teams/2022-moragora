package com.woowacourse.moragora.domain.attendance;

import java.util.List;
import lombok.Getter;

@Getter
public class ParticipantAttendances {

    private final List<Attendance> values;

    public ParticipantAttendances(final List<Attendance> values) {
        validateSingleParticipant(values);
        this.values = values;
    }

    public int countTardy() {
        return (int) values.stream()
                .filter(Attendance::isEnabled)
                .filter(Attendance::isTardy)
                .count();
    }

    private void validateSingleParticipant(final List<Attendance> value) {
        final long participantCount = value.stream()
                .map(Attendance::getParticipant)
                .distinct()
                .count();

        if (participantCount > 1) {
            throw new IllegalArgumentException();
        }
    }
}
