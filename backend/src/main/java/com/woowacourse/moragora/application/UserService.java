package com.woowacourse.moragora.application;

import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.dto.response.user.EmailCheckResponse;
import com.woowacourse.moragora.dto.request.user.NicknameRequest;
import com.woowacourse.moragora.dto.request.user.PasswordRequest;
import com.woowacourse.moragora.dto.request.user.UserDeleteRequest;
import com.woowacourse.moragora.dto.request.user.UserRequest;
import com.woowacourse.moragora.dto.response.user.UserResponse;
import com.woowacourse.moragora.dto.response.user.UsersResponse;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.password.EncodedPassword;
import com.woowacourse.moragora.domain.user.password.RawPassword;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.global.NoParameterException;
import com.woowacourse.moragora.exception.user.InvalidPasswordException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import com.woowacourse.moragora.domain.participant.ParticipantRepository;
import com.woowacourse.moragora.domain.user.UserRepository;
import java.util.List;
import java.util.Objects;
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

    @Transactional
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

    public UserResponse findById(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse.from(user);
    }

    @Transactional
    public void updateNickname(final NicknameRequest request, final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user.updateNickname(request.getNickname());
    }

    @Transactional
    public void updatePassword(final PasswordRequest request, final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        final String oldPassword = request.getOldPassword();
        final String newPassword = request.getNewPassword();
        validateOldPasswordIsCorrect(user, oldPassword);
        validateNewPasswordIsNotSame(oldPassword, newPassword);

        user.updatePassword(EncodedPassword.fromRawValue(newPassword));
    }

    @Transactional
    public void delete(final UserDeleteRequest request, final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        validateOldPasswordIsCorrect(user, request.getPassword());
        final List<Participant> participants = participantRepository.findByUserId(id);
        validateHasMasterRole(participants);

        final List<Long> participantIds = participants.stream()
                .map(Participant::getId)
                .collect(Collectors.toUnmodifiableList());
        attendanceRepository.deleteByParticipantIdIn(participantIds);
        participantRepository.deleteByIdIn(participantIds);
        userRepository.delete(user);
    }

    private void validateOldPasswordIsCorrect(final User user, final String oldPassword) {
        try {
            user.checkPassword(new RawPassword(oldPassword));
        } catch (AuthenticationFailureException e) {
            throw new InvalidPasswordException();
        }
    }

    private void validateNewPasswordIsNotSame(final String oldPassword, final String newPassword) {
        if (Objects.equals(oldPassword, newPassword)) {
            throw new ClientRuntimeException("새로운 비밀번호가 기존의 비밀번호와 일치합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateKeyword(final String keyword) {
        if (keyword.isBlank()) {
            throw new NoParameterException();
        }
    }

    private void validateHasMasterRole(final List<Participant> participants) {
        final boolean isMaster = participants.stream()
                .anyMatch(Participant::getIsMaster);
        if (isMaster) {
            throw new ClientRuntimeException("마스터로 참여중인 모임이 있어 탈퇴할 수 없습니다.", HttpStatus.FORBIDDEN);
        }
    }
}
