package com.example.demo.config;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if ("OPTIONS".equals(req.getMethod())) {
            return;
        }
        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {

    }
}
