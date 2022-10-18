package com.woowacourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MoragoraApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoragoraApplication.class, args);
    }
}
