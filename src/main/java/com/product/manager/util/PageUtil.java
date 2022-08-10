package com.product.manager.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public class PageUtil {
	
	private final static String HEADER_TOTAL_COUNT = "X-Total-Count";
	
    public static <T> HttpHeaders createHeaderForPaganation(Page<T> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_TOTAL_COUNT, Long.toString(page.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", HEADER_TOTAL_COUNT);
        return headers;
    }
}
