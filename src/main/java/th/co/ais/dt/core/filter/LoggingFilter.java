package th.co.ais.dt.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private static final int MAX_BODY_REQ_LENGTH = 10000;
    private static final int MAX_BODY_RES_LENGTH = 2000;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
        	try {
        		 long duration = System.currentTimeMillis() - start;

                 String requestBody = truncateReq(new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8));
                 String responseBody = truncateRes(new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8));

                 log.info("method={} uri={} query={} requestBody={} responseStatus={} responseBody={} durationMs={}",
                         request.getMethod(),
                         request.getRequestURI(),
                         request.getQueryString(),
                         requestBody,
                         response.getStatus(),
                         responseBody,
                         duration);
        	}catch (Exception e) {
				e.printStackTrace();
			}
           

            wrappedResponse.copyBodyToResponse();
        }
    }

    private String truncateReq(String body) {
        if (body == null || body.isEmpty()) return "-";
        return body.length() <= MAX_BODY_REQ_LENGTH ? body : body.substring(0, MAX_BODY_REQ_LENGTH) + "...[truncated]";
    }
    
    private String truncateRes(String body) {
        if (body == null || body.isEmpty()) return "-";
        if( !(body.startsWith("[") || body.startsWith("{")) ) return "-";
        return body.length() <= MAX_BODY_RES_LENGTH ? body : body.substring(0, MAX_BODY_RES_LENGTH) + "...[truncated]";
    }
}
