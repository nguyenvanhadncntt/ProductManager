package com.product.manager.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class CustomURLFilter implements Filter {

	private static final String REQUEST_ID = "request_id";

	private final Logger log = LoggerFactory.getLogger(CustomURLFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		
		String requestId = UUID.randomUUID().toString();
		MDC.put("requestId", "REQUEST_ID: " + requestId);
		servletRequest.setAttribute(REQUEST_ID, requestId);
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request); 
		logRequest(cachingRequestWrapper, requestId);
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}

	private void logRequest(ContentCachingRequestWrapper request, String requestId) {
		if (request != null) {
			StringBuilder data = new StringBuilder();
			data.append("\nLOGGING REQUEST-----------------------------------\n").append("[REQUEST-ID]: ")
					.append(requestId).append("\n").append("[PATH]: ").append(request.getRequestURI()).append("\n")
					.append("[QUERIES]: ").append(request.getQueryString()).append("\n").append("[HEADERS]: ")
					.append("\n");

			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				data.append("---").append(key).append(" : ").append(value).append("\n");
			}
			
//			try {
//				data.append("[BODY]: ").append(new String(request.getContentAsByteArray()));
//			} catch (IOException e) {
//				data.append("[BODY]: ").append("Cannot Read content");
//			}
			data.append("LOGGING REQUEST-----------------------------------\n");

			log.info(data.toString());
		}
	}
	
//	private String writeStreamToString(ContentCachingRequestWrapper request) throws IOException {
//		ServletInputStream mServletInputStream = request.getInputStream();
//	    byte[] httpInData = new byte[request.getContentLength()];
//	    int retVal = -1;
//	    StringBuilder stringBuilder = new StringBuilder();
//
//	    while ((retVal = mServletInputStream.read(httpInData)) != -1) {
//	        for (int i = 0; i < retVal; i++) {
//	            stringBuilder.append(Character.toString((char) httpInData[i]));
//	        }
//	    }
//
//	    return stringBuilder.toString();
//	}
}
