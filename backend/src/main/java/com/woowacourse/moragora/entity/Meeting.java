package com.woowacourse.moragora.entity;

import com.woowacourse.moragora.exception.InvalidFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    private final List<Participant> participants = new ArrayList<>();

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

    public List<Long> getParticipantIds() {
        return participants.stream()
                .map(Participant::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    private void validateName(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidFormatException();
        }
    }
}
