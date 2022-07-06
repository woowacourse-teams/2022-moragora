package com.woowacourse.moragora.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "discussion")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Discussion extends BaseTimeEntity {

    private static final int MAX_TITLE_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = MAX_TITLE_LENGTH)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int views;

    public Discussion(final Long id, final String title, final String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.views = 0;
    }

    public Discussion(final String title, final String content) {
        this(null, title, content);
    }

    public void increaseViews() {
        views++;
    }
}
