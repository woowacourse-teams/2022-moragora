package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.UserResponse;
import com.woowacourse.moragora.entity.User;
import com.woowacourse.moragora.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> findByMeetingId(final Long meetingId) {
        List<User> users = userRepository.findByMeetingId(meetingId);
        return users.stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getAbsentCount()))
                .collect(Collectors.toList());
    }
}
