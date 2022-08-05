package com.product.manager.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class CustomRequestLoggingFilter extends CommonsRequestLoggingFilter {
	private static final String REQUEST_ID = "request_id";

	
	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		String requestId = UUID.randomUUID().toString();
		request.setAttribute(REQUEST_ID, requestId);
		
		logger.debug(message);
	}
	
	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		logger.debug(message);
		logRequest(request);
	}
	
	private void logRequest(HttpServletRequest request) {
		if (request != null) {
			StringBuilder data = new StringBuilder();
			data.append("\nLOGGING REQUEST-----------------------------------\n").append("[REQUEST-ID]: ")
					.append(request.getAttribute(REQUEST_ID)).append("\n").append("[PATH]: ").append(request.getRequestURI()).append("\n")
					.append("[QUERIES]: ").append(request.getQueryString()).append("\n").append("[HEADERS]: ")
					.append("\n");

			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				data.append("---").append(key).append(" : ").append(value).append("\n");
			}
			
			try {
				data.append("[BODY]: ").append(writeStreamToString(request));
			} catch (IOException e) {
				data.append("[BODY]: ").append("Cannot Read content");
			}
			data.append("LOGGING REQUEST-----------------------------------\n");

			logger.info(data.toString());
		}
	}
	
	private String writeStreamToString(HttpServletRequest request) throws IOException {
		ServletInputStream mServletInputStream = request.getInputStream();
	    byte[] httpInData = new byte[request.getContentLength()];
	    int retVal = -1;
	    StringBuilder stringBuilder = new StringBuilder();

	    while ((retVal = mServletInputStream.read(httpInData)) != -1) {
	        for (int i = 0; i < retVal; i++) {
	            stringBuilder.append(Character.toString((char) httpInData[i]));
	        }
	    }

	    return stringBuilder.toString();
	}
}
