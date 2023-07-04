package com.sparta.newspeed.scheduler;

import com.sparta.newspeed.repository.TokenBlacklistRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenBlacklistScheduler {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public TokenBlacklistScheduler(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    // 초, 분, 시, 일, 월, 주 순서 - 매일 00시에 비워주기
    @Scheduled(cron = "0 0 0 * * *")
    public void clear() {
        tokenBlacklistRepository.deleteAll();
    }
}