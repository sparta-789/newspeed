package com.sparta.newspeed.dto;

import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 특정 유저의 정보 조희

public class UserResponseDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String intro;
    private UserRoleEnum role;

    public UserResponseDto(User user) {
        this.id=user.getId();
        this.username=user.getUsername();
        this.password=user.getPassword();
        this.email=user.getEmail();
        this.intro=user.getSelfIntroduction();
        this.role=user.getRole();
    }
}
