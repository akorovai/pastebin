package dev.akorovai.post.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FilterConfig {
	private final JwtFilter jwtFilter;

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilterBean() {
		log.info("Configuring JwtFilter with URL pattern: /api/posts/*");

		FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(jwtFilter);
		filterRegistrationBean.addUrlPatterns("/api/posts/*");

		log.info("JwtFilter registered successfully.");

		return filterRegistrationBean;
	}
}
