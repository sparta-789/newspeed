package com.sparta.newspeed.entity;

import com.sparta.newspeed.dto.UserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_email", nullable = false,unique = true)
    private String email;

    @Column(name = "self_introduction")
    private String selfIntroduction;

    @Column(name = "user_role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    //메일 인증 때문에 추가
    @Column(name="user_authkey")
    private String authKey;

    @Column(name="user_confirmn",nullable = false)
    @ColumnDefault("false")
    private Boolean isConfirm;

    public User(String username, String password, String email, UserRoleEnum role,String authKey) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.authKey=authKey;
        this.isConfirm=false;
    }

    public void updateUser(UserUpdateRequestDto updateRequestDto) {
        this.username = updateRequestDto.getUsername();
        this.email = updateRequestDto.getEmail();
        this.selfIntroduction = updateRequestDto.getIntro();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void setIsConfirmTrue() {
        this.isConfirm=true;
    }
}
