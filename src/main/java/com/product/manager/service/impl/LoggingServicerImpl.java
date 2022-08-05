package com.product.manager.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.manager.filter.CustomURLFilter;
import com.product.manager.service.LoggingService;

@Service
public class LoggingServicerImpl implements LoggingService {

	private static final String REQUEST_ID = "request_id";
	
	private final Logger log = LoggerFactory.getLogger(CustomURLFilter.class);
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Override
	public void logRequest(HttpServletRequest httpServletRequest, Object body) throws JsonProcessingException {
		if (httpServletRequest.getRequestURI().contains("medias")) {
	           return;
	       }
	       Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
	       StringBuilder data = new StringBuilder();
	       data.append("\nLOGGING REQUEST BODY-----------------------------------\n")
	               .append("[REQUEST-ID]: ").append(requestId).append("\n")
	               .append("[BODY REQUEST]: ").append("\n\n")
	               .append(objectMapper.writeValueAsString(body))
	               .append("\n\n")
	               .append("LOGGING REQUEST BODY-----------------------------------\n");

	       log.info(data.toString());
	}

	@Override
	public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object body) throws JsonProcessingException {
		if (httpServletRequest.getRequestURI().contains("medias")) {
	           return;
	       }
	       Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
	       StringBuilder data = new StringBuilder();
	       data.append("\nLOGGING RESPONSE-----------------------------------\n")
	               .append("[REQUEST-ID]: ").append(requestId).append("\n")
	               .append("[Status]: ").append(httpServletResponse.getStatus()).append("\n\n")
	               .append("[BODY RESPONSE]: ").append("\n\n")
	               .append(objectMapper.writeValueAsString(body))
	               .append("\n\n")
	               .append("LOGGING RESPONSE-----------------------------------\n");

	       log.info(data.toString());
	}

}
