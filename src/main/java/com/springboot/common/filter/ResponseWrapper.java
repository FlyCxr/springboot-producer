package com.springboot.common.filter;

import java.util.Iterator;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class ResponseWrapper extends ContentCachingResponseWrapper {
    private HttpHeaders headers;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public HttpHeaders getHeaders() {
        if (this.headers == null) {
            this.headers = new HttpHeaders();
            Iterator var1 = this.getHeaderNames().iterator();

            while(var1.hasNext()) {
                String headerName = (String)var1.next();
                Iterator var3 = this.getHeaders(headerName).iterator();

                while(var3.hasNext()) {
                    String headerValue = (String)var3.next();
                    this.headers.add(headerName, headerValue);
                }
            }
        }

        return this.headers;
    }
}
