package com.sparta.newspeed.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) //입력되지 않는 필드는 json 에 추가되지 않는다.
public class UserUpdateRequestDto {
    private Long id;
    private String username;
    //비밀번호 수정 시 비밀번호 한 번 더 입력받는다.
    private String password;
    private String email;
    private String intro;
    //비밀번호가 일치할 경우 입력받을 password
    private String newPassword;
}
