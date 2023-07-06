package com.sparta.newspeed.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;



@Configuration
@PropertySource("classpath:email.properties") //설정 파일을 읽어 속성을 주입
public class EmailConfig { //이메일 설정 관련 class

    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.smtp.socketFactory.port}")
    private int socketPort;
    @Value("${mail.smtp.auth}")
    private boolean auth;
    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;
    @Value("${mail.smtp.starttls.required}")
    private boolean startlls_required;
    @Value("${mail.smtp.socketFactory.fallback}")
    private boolean fallback;
    @Value("${AdminMail.id}")
    private String id;
    @Value("${AdminMail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        // Gmail SMTP 서버 호스트를 설정
        javaMailSender.setHost("smtp.gmail.com");
        // Gmail 계정의 아이디를 설정 : id 변수는 사용자의 Gmail 계정아이디로 대체
        javaMailSender.setUsername(id);
        javaMailSender.setPassword(password);
        // Gmail SMTP 서버 포트 번호를 설정
        javaMailSender.setPort(port);
        // 메일 전송에 대한 구체적인 속성들을 설정
        javaMailSender.setJavaMailProperties(getMailProperties());
        // 기본 인코딩을 UTF-8로 설정
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }
    private Properties getMailProperties()
    {
        Properties pt = new Properties();
        // SMTP 서버의 소켓 포트 번호를 설정
        pt.put("mail.smtp.socketFactory.port", socketPort);
        // SMTP 서버에서 사용자 인증을 요구하는지 여부를 설정
        pt.put("mail.smtp.auth", auth);
        // STARTTLS 암호화를 사용하여 연결을 암호화하는지 여부를 설정
        pt.put("mail.smtp.starttls.enable", starttls);
        // STARTTLS 암호화를 필수로 요구하는지 여부를 설정
        pt.put("mail.smtp.starttls.required", startlls_required);
        // 소켓 팩토리의 대체 메커니즘을 설정
        pt.put("mail.smtp.socketFactory.fallback",fallback);
        // 소켓 팩토리 클래스를 설정 :  SSL 소켓 팩토리 클래스를 사용하도록 "javax.net.ssl.SSLSocketFactory"로 설정
        pt.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return pt;
    }
}