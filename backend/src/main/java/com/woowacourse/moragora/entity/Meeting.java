package com.woowacourse.moragora.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meeting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    private final List<Participant> participants = new ArrayList<>();

    @Builder
    public Meeting(final String name) {
        this.name = name;
    }

    public List<Long> getParticipantIds() {
        return participants.stream()
                .map(Participant::getId)
                .collect(Collectors.toList());
    }
}
