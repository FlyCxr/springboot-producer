package com.springboot.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局编码过滤器
 */
@Component
@WebFilter(urlPatterns = "/*")
@SuppressWarnings("all")
public class GlobalEncodingFilter implements Filter {

    @Value("${server.defaultCharset}")
    private String defaultCharset;

    private FilterConfig config;

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalEncodingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("GlobalEncodingFilter start");
        String charset = this.config.getInitParameter("charset");
        if (charset == null) {
            charset = defaultCharset;
        }
        //设置编码
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding(charset);
        response.setCharacterEncoding(charset);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
