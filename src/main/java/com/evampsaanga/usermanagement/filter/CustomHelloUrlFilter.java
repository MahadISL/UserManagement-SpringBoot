package com.evampsaanga.usermanagement.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class CustomHelloUrlFilter implements Filter {


    private static final Logger logger = LoggerFactory.getLogger(CustomHelloUrlFilter.class);
    @Override
    public void doFilter(ServletRequest request1, ServletResponse response1, FilterChain filterChain) throws IOException, ServletException{

        HttpServletRequest request = (HttpServletRequest) request1;
        HttpServletResponse response = (HttpServletResponse) response1;

        logger.info("This filter is only called when request is mapped for "+request.getRequestURI()+" resource");
        logger.info(request.getMethod());

        //call next filter in the filter chain
        //handover request & response Object to controller to perform all operations
        filterChain.doFilter(request,response);

        //After all the operations performed in the controller, control back to method here
        logger.info("Done with handling request for "+ request.getRequestURI());
        //logger.info(request.getContentType());

    }
}
