package com.woowacourse.auth.service;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.auth.dto.LoginResponse;
import com.woowacourse.auth.exception.AuthorizationFailureException;
import com.woowacourse.auth.support.JwtTokenProvider;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.user.RawPassword;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.repository.MasterRepository;
import com.woowacourse.moragora.repository.participant.ParticipantRepository;
import com.woowacourse.moragora.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final MasterRepository masterRepository;

    public AuthService(final JwtTokenProvider jwtTokenProvider,
                       @Qualifier("userSpringJpaRepository") final UserRepository userRepository,
                       @Qualifier("participantSpringJpaRepository") final ParticipantRepository participantRepository,
                       final MasterRepository masterRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.masterRepository = masterRepository;
    }

    public LoginResponse createToken(final LoginRequest loginRequest) {
        final User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(AuthorizationFailureException::new);

        final RawPassword rawPassword = new RawPassword(loginRequest.getPassword());
        user.checkPassword(rawPassword);
        final String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()));
        return new LoginResponse(accessToken);
    }

    public boolean isMaster(final Long meetingId, final Long loginId) {
        final Participant participant = participantRepository
                .findByMeetingIdAndUserId(meetingId, loginId)
                .orElseThrow(ParticipantNotFoundException::new);
        return masterRepository.findByParticipantId(participant.getId())
                .isPresent();
    }
}
