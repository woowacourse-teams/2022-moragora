package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
@ToString
public class MeetingRequest {

    private static final int MAX_NAME_LENGTH = 50;
    private static final String MISSING_REQUIRED_INPUT = "필수 입력 값이 누락됐습니다.";

    @NotBlank(message = MISSING_REQUIRED_INPUT)
    @Length(max = MAX_NAME_LENGTH, message = "모임 이름은 " + MAX_NAME_LENGTH + "자를 초과할 수 없습니다.")
    private String name;

    private List<Long> userIds;

    public MeetingRequest(final String name, final List<Long> userIds) {
        this.name = name;
        this.userIds = userIds;
    }

    public Meeting toEntity() {
        return new Meeting(name);
    }
}
