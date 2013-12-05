package com.boco.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/EncodingFilter")
public class EncodingFilter implements Filter {

	final static Logger LOG = LoggerFactory.getLogger(EncodingFilter.class);
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		LOG.info("init...");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
		LOG.info("destroy...");
	}

}
