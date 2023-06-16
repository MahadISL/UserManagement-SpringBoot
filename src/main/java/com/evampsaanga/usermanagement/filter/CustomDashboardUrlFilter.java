package com.evampsaanga.usermanagement.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomDashboardUrlFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CustomHelloUrlFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.info("Method and filter request mapped for: " + request.getMethod() + request.getRequestURI());

        filterChain.doFilter(request,response);

        logger.info("Done with handling request for : " + request.getRequestURI());
    }
}
