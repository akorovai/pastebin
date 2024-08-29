package dev.akorovai.hashgenerator.config;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BeanConfig {

	@Bean
	public ModelMapper modelMapper() {
		log.info("Creating ModelMapper bean");
		return new ModelMapper();
	}
}
