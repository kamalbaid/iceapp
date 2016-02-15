package com.iceapp.x;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionFilter implements Filter  {
	
    private final static Logger logger = LoggerFactory.getLogger(SessionFilter.class);

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		String requestURL = request.getRequestURL().toString();
		
		String applicationName = request.getContextPath();
		boolean loginPage = requestURL.endsWith("jsp/login.jsp");
		
		boolean indexJsp = loginPage;
		indexJsp = indexJsp || requestURL.endsWith("jsp/index.jsp");
		indexJsp = indexJsp || requestURL.endsWith(applicationName);
		indexJsp = indexJsp || requestURL.endsWith(applicationName+"/");
		
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
