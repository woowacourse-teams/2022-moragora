package com.woowacourse.moragora.config;

import io.micrometer.core.instrument.util.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;

@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

    private static final Logger log = LoggerFactory.getLogger(EmbeddedRedisConfig.class);

    private RedisServer redisServer;

    @Value("${spring.redis.port}")
    private int redisPort;


    @PostConstruct
    public void redisServer() throws IOException {
        int availablePort = isRedisRunning() ? findAvailablePort() : redisPort;

        if (isArm()) {
            redisServer = new RedisServer(Objects.requireNonNull(getRedisFileForArmArch()), availablePort);
            log.info(redisServer.toString());
        }
        if (!isArm()) {
            redisServer = new RedisServer(availablePort);
        }

        redisServer.start();
    }

    private boolean isArm() {
        log.info("### OS ARCH ### = {} ", System.getProperty("os.arch"));
        log.info("### OS NAME ### = {} ", System.getProperty("os.name"));

        return Objects.equals(System.getProperty("os.arch"), "aarch64");
    }

    private File getRedisFileForArmArch() {
        try {
            return new ClassPathResource("binary/redis/redis-server-6.2.5-arm64").getFile();
        } catch (Exception e) {
            throw new RuntimeException("Embedded Redis 설정에 실패했습니다.");
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    /**
     * Embedded Redis가 현재 실행중인지 확인
     */
    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    /**
     * 현재 PC/서버에서 사용가능한 포트 조회
     */
    public int findAvailablePort() throws IOException {

        for (int port = 10000; port <= 65535; port++) {
            final Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }

    /**
     * 해당 port를 사용중인 프로세스 확인하는 sh 실행
     */
    private Process executeGrepProcessCommand(int port) throws IOException {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            final String command = String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port);
            final String[] shell = {"cmd.exe", "/y", "/c", command};
            return Runtime.getRuntime().exec(shell);
        }

        final String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        final String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private boolean isRunning(Process process) {
        final StringBuilder pidInfo = new StringBuilder();

        String line;
        try (final BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (final Exception e) {
        }

        return !StringUtils.isEmpty(pidInfo.toString());
    }
}
