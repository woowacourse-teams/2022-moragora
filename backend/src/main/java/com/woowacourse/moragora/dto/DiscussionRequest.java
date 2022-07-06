package com.woowacourse.moragora.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class DiscussionRequest {

    @NotBlank(message = "제목을 입력하지 않았습니다.")
    @Size(max = 50, message = "제목의 길이는 50자를 초과할 수 없습니다.")
    private String title;

    @NotBlank(message = "본문을 입력하지 않았습니다.")
    private String content;

    public DiscussionRequest(final String title, final String content) {
        this.title = title;
        this.content = content;
    }
}
