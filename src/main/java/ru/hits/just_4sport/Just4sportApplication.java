package ru.hits.just_4sport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.hits.just_4sport.properties.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class Just4sportApplication {

	public static void main(String[] args) {
		SpringApplication.run(Just4sportApplication.class, args);
	}

}
