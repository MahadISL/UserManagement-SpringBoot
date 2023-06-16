package com.evampsaanga.usermanagement.config;

import com.evampsaanga.usermanagement.filter.CustomDashboardUrlFilter;
import com.evampsaanga.usermanagement.filter.CustomHelloUrlFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CustomHelloUrlConfig {

    @Bean
    public FilterRegistrationBean<CustomDashboardUrlFilter> filterFilterRegistrationBean(){
        FilterRegistrationBean<CustomDashboardUrlFilter> registrationBean = new FilterRegistrationBean<>();
        CustomDashboardUrlFilter customDashboardUrlFilter = new CustomDashboardUrlFilter();

        registrationBean.setFilter(customDashboardUrlFilter);
        registrationBean.addUrlPatterns("/User/dashboard");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CustomHelloUrlFilter> filterRegistrationBean(){

        FilterRegistrationBean<CustomHelloUrlFilter> registrationBean = new FilterRegistrationBean<>();
        CustomHelloUrlFilter customHelloUrlFilter = new CustomHelloUrlFilter();

        registrationBean.setFilter(customHelloUrlFilter);
        registrationBean.addUrlPatterns("/User/hello");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
