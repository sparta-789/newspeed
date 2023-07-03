package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.AuthRequestDto;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import com.sparta.newspeed.dto.SignupRequestDto;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.entity.UserRoleEnum;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void login(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException(" 등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 id 입니다. 다른 id를 입력해 주세요");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, email, role);
        userRepository.save(user);
    }
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







