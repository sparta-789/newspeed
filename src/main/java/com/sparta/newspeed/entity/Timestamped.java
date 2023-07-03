package com.sparta.newspeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AutoCloseable.class)
public abstract class Timestamped {
    @CreatedDate
    @Column(unique = false) // 처음만 생성되고 업데이트를 false로 막음
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createAt;

    @LastModifiedDate //조회한 Entity객체의 값을 변경할 때마다 변경된 시간이 자동으로 저장
    @Column
    @Temporal(TemporalType.TIMESTAMP) // 날자 데이터를 매핑
    private LocalDateTime modifiedAt;
}
