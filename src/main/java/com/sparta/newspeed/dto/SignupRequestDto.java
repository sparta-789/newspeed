package com.sparta.newspeed.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.newspeed.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignupRequestDto {
    @Pattern(regexp = "^[a-z0-9]{4,10}$",
            message = "최소 4자 이상, 10자 이하이며 알파벳 소문자, 숫자로 구성되어야 합니다.")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{8,15}$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자, 숫자로 구성되어야 합니다.")
    private String password;

    @Email
    private String email;

    private UserRoleEnum role;

    //TODO admin이나 role 중에 하나만 남기기  
    private boolean admin = false;
    private String adminToken = "";
    private String authKey;
    private boolean isConfirm =false;
    private UserRoleEnum role;

}
