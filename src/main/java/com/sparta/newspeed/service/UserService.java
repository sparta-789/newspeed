package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.UserResponseDto;
import com.sparta.newspeed.dto.UserUpdateRequestDto;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.entity.UserRoleEnum;
import com.sparta.newspeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //유저 조희하는 메서드를 내부에서 사용. 리턴을 UserResponseDto 로 해준다.
    //프로필 용( 본인이 본인 조희)     나중에 다른사람이 정보를 조희활 떈 dto 를 새로 만들어서 password, role 등의 정보를 제외해야 한다.
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        return new UserResponseDto(findUser(id));
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestDto updateRequestDto, User user) {
        //admin 혹은 작성자인지 확인
        if (!(findUser(updateRequestDto.getId()).equals(user)) || !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new SecurityException("유저를 수정할 권한이 없습니다.");
        }

        //새 비밀번호가 존재하고, 기존 비밀번호가 일치하면 비밀번호 변경
        if (updateRequestDto.getNewPassword() != null && !user.getPassword().equals(updateRequestDto.getPassword())) {
            user.updatePassword(passwordEncoder.encode(updateRequestDto.getNewPassword()));
        }
        user.updateUser(updateRequestDto);
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long id, User user) {
        if (!findUser(id).equals(user)) {
            throw new RejectedExecutionException();
        }
        userRepository.delete(user);
    }

    //id에 해당하는 유저 조희
    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));
    }
}
