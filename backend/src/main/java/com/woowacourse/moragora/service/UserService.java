package com.woowacourse.moragora.service;

import com.woowacourse.auth.exception.AuthorizationFailureException;
import com.woowacourse.moragora.dto.EmailCheckResponse;
import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.dto.UserResponse;
import com.woowacourse.moragora.dto.UsersResponse;
import com.woowacourse.moragora.dto.WithdrawalRequest;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.user.EncodedPassword;
import com.woowacourse.moragora.entity.user.RawPassword;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.NoParameterException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import com.woowacourse.moragora.repository.AttendanceRepository;
import com.woowacourse.moragora.repository.ParticipantRepository;
import com.woowacourse.moragora.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;

    public UserService(final UserRepository userRepository,
                       final ParticipantRepository participantRepository,
                       final AttendanceRepository attendanceRepository) {
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public Long create(final UserRequest userRequest) {
        final User user = new User(userRequest.getEmail(), EncodedPassword.fromRawValue(userRequest.getPassword()),
                userRequest.getNickname());
        final User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public EmailCheckResponse isEmailExist(final String email) {
        if (email.isBlank()) {
            throw new NoParameterException();
        }
        final boolean isExist = userRepository.findByEmail(email).isPresent();
        return new EmailCheckResponse(isExist);
    }

    public UsersResponse searchByKeyword(final String keyword) {
        validateKeyword(keyword);
        final List<User> searchedUsers = userRepository.findByNicknameOrEmailLike(keyword);
        final List<UserResponse> responses = searchedUsers.stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
        return new UsersResponse(responses);
    }

    private void validateKeyword(final String keyword) {
        if (keyword.isEmpty()) {
            throw new NoParameterException();
        }
    }

    public UserResponse findById(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse.from(user);
    }

    @Transactional
    public void delete(final WithdrawalRequest request, final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        validateOldPassword(user, request.getPassword());
        final List<Participant> participants = participantRepository.findByUserId(id);
        validateMaster(participants);

        final List<Long> participantIds = participants.stream()
                .map(Participant::getId)
                .collect(Collectors.toUnmodifiableList());
        attendanceRepository.deleteByParticipantIdIn(participantIds);
        participantRepository.deleteByIdIn(participantIds);
        userRepository.delete(user);
    }

    private void validateOldPassword(final User user, final String oldPassword) {
        try {
            user.checkPassword(new RawPassword(oldPassword));
        } catch (AuthorizationFailureException e) {
            // TODO: InvalidPasswordException으로 교체
            throw new ClientRuntimeException("비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateMaster(final List<Participant> participants) {
        final boolean isMaster = participants.stream()
                .anyMatch(Participant::getIsMaster);
        if (isMaster) {
            throw new ClientRuntimeException("마스터로 참여중인 모임이 있어 탈퇴할 수 없습니다.", HttpStatus.FORBIDDEN);
        }
    }
}
