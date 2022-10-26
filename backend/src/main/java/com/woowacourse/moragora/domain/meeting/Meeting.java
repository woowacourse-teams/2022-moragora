package com.woowacourse.moragora.domain.meeting;

import com.woowacourse.moragora.domain.exception.BusinessException;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantAndCount;
import com.woowacourse.moragora.exception.global.InvalidFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meeting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Meeting {

    private static final int MAX_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "meeting")
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public Meeting(final Long id, final String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public Meeting(final String name) {
        this(null, name);
    }

    public void updateName(final String name) {
        validateName(name);
        this.name = name;
    }

    public Optional<Participant> findParticipant(final Long userId) {
        return participants.stream()
                .filter(it -> it.isSameParticipant(userId))
                .findAny();
    }

    public void allocateParticipantsTardyCount(final List<ParticipantAndCount> participantAndCounts) {
        participantAndCounts.forEach(it -> it.getParticipant().allocateTardyCount(it.getTardyCount()));
    }

    public Boolean isTardyStackFull() {
        return calculateTardy() >= participants.size();
    }

    public long calculateTardy() {
        return participants.stream()
                .mapToLong(Participant::getTardyCount)
                .sum();
    }

    public long tardyCountByUserId(final Long loginUserId) {
        return participants.stream()
                .filter(participant -> participant.getUser().getId().equals(loginUserId))
                .map(participant -> participant.getTardyCount())
                .findAny()
                .orElseThrow(() -> new BusinessException("지각 횟수가 할당되지 않았습니다."));
    }

    public List<Long> getParticipantIds() {
        return participants.stream()
                .map(Participant::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    public boolean isMaster(final Long userId) {
        return participants.stream()
                .filter(participant -> participant.getUser().getId().equals(userId))
                .map(participant -> participant.getIsMaster())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("유저 ID에 해당하는 참가자가 없습니다, id: " + userId));
    }

    private void validateName(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidFormatException();
        }
    }
}
