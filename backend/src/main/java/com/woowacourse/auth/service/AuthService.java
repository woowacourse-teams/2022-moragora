package com.woowacourse.auth.service;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.auth.dto.LoginResponse;
import com.woowacourse.auth.exception.AuthenticationFailureException;
import com.woowacourse.auth.support.JwtTokenProvider;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.user.RawPassword;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.repository.ParticipantRepository;
import com.woowacourse.moragora.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    public AuthService(final JwtTokenProvider jwtTokenProvider,
                       final UserRepository userRepository,
                       final ParticipantRepository participantRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
    }

    public LoginResponse createToken(final LoginRequest loginRequest) {
        final User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(AuthenticationFailureException::new);

        final RawPassword rawPassword = new RawPassword(loginRequest.getPassword());
        user.checkPassword(rawPassword);
        final String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()));
        return new LoginResponse(accessToken);
    }

    public boolean isMaster(final Long meetingId, final Long loginId) {
        final Participant participant = participantRepository
                .findByMeetingIdAndUserId(meetingId, loginId)
                .orElseThrow(ParticipantNotFoundException::new);
        return participant.getIsMaster();
    }
}
