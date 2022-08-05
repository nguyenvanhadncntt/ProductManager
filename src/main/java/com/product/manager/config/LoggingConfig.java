package com.product.manager.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.product.manager.filter.CustomURLFilter;

@Configuration
public class LoggingConfig {

	@Bean
	public FilterRegistrationBean<CustomURLFilter> registCustomURLFilter() {
		FilterRegistrationBean<CustomURLFilter> registrationBean = new FilterRegistrationBean<CustomURLFilter>();
		CustomURLFilter customURLFilter = new CustomURLFilter();

		registrationBean.setFilter(customURLFilter);
		registrationBean.setOrder(2);
		return registrationBean;
	}
}
