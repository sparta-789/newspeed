package com.sparta.newspeed.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostDeleteDto {
    private String msg;

    public void setMsg(String msg){
        this.msg = msg;
    }
}
