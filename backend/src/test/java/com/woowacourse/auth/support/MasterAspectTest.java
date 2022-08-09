package com.woowacourse.auth.support;

import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.PHILLZ;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MasterAspectTest {

    @Autowired
    private MasterAspect masterAspect;

    @Autowired
    private DataSupport dataSupport;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }


    @DisplayName("로그인한 유저가 해당 미팅의 Master인지 판단한다.")
    @Test
    void authorizeMaster() {
        // given
        final User user1 = dataSupport.saveUser(KUN.create());
        final User user2 = dataSupport.saveUser(PHILLZ.create());

        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        dataSupport.saveParticipant(user1, meeting, true);
        dataSupport.saveParticipant(user2, meeting, false);

        // when, then
        assertThatCode(() -> masterAspect.authorizeMaster(meeting.getId(), user1.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("로그인한 유저가 해당 미팅의 Master인지 판단한다(아닌 경우)")
    @Test
    void authorizeMaster_NotMaster() {
        // given
        final User user1 = dataSupport.saveUser(KUN.create());
        final User user2 = dataSupport.saveUser(PHILLZ.create());

        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        dataSupport.saveParticipant(user1, meeting, true);
        dataSupport.saveParticipant(user2, meeting, false);

        // when, then
        assertThatThrownBy(() -> masterAspect.authorizeMaster(meeting.getId(), user2.getId()))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("마스터 권한이 없습니다.");
    }
}
