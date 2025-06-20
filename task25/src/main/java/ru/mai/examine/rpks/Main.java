package ru.mai.examine.rpks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.mai.examine.rpks.config.KafkaConfiguration;

@SpringBootApplication
@EnableConfigurationProperties({KafkaConfiguration.class, PostgresConfiguration.class})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}