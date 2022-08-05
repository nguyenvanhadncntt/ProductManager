package com.product.manager.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface LoggingService {
	void logRequest(HttpServletRequest httpServletRequest, Object body) throws JsonProcessingException;
	void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) throws JsonProcessingException;
}
