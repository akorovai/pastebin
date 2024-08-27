package dev.akorovai.post.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilterConfig {
	private final JwtFilter jwtFilter;


	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilterBean() {
		FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(jwtFilter);
		filterRegistrationBean.addUrlPatterns("/api/posts/*");
		return filterRegistrationBean;
	}


}