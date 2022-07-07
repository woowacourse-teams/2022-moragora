package com.woowacourse.moragora.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "opinion")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Opinion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    // TODO: LAZY-LOADING
    @ManyToOne
    @JoinColumn(name = "discussion_id")
    private Discussion discussion;

    public Opinion(final String content, final Discussion discussion) {
        this.content = content;
        this.discussion = discussion;
    }
}
