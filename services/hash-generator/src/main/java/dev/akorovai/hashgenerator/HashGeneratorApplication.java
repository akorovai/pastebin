package dev.akorovai.hashgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HashGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(HashGeneratorApplication.class, args);
	}

}
