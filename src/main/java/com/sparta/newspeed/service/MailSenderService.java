package com.sparta.newspeed.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSenderService { //이메일 전송을 처리하는 서비스 클래스
    // spring Framework 에서 제공하는 이메일 전송을 위한 인터페이스
    private JavaMailSender mailSender;

    // 8자리의 인증 키를 랜덤하게 생성하는 메서드
    public static String authKey = createKey();

    @Autowired
    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    //전달된 이메일 주소를 사용하여 메세지 내용을 구성
    private MimeMessage createMessage(String email) {
        log.info("보내는 대상 : " + email);
        log.info("인증 번호 : " + authKey);
        MimeMessage mail = mailSender.createMimeMessage();
        String mailContent = "<h1>[이메일 인증]</h1><br><p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>"
                + "<a href='http://localhost:8080/api/auth/confirmSignup?email="
                + email + "&authKey=" + authKey + "' target='_blenk'>이메일 인증 확인</a>";

        try {
            mail.setSubject("뉴스피드 회원가입 인증 이메일", "utf-8");
            mail.setText(mailContent, "utf-8", "html");
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); // 메일의 수신자를 추가하는 메서드
            mailSender.send(mail); //메일을 전송 -> 본문에는 이메일 인증을 위한 링크가 포함
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mail;
    }

    //랜덤값(a-zA-Z0-9)중에 8글자 조합
    public static String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0 -> key.append((char) (rnd.nextInt(26) + 97));

                //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                case 1 -> key.append((char) (rnd.nextInt(26) + 65));

                //  A~Z
                case 2 -> key.append((rnd.nextInt(10)));

                // 0~9
            }
        }
        return key.toString();
    }

    // create method 에서 구성한 message 를 전송
    // 메서드가 호출될 때마다 해당 인증 키가 반환
    public String sendSimpleMessage(String email) {

        try { //예외처리
            MimeMessage message = createMessage(email);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return authKey;
    }

}