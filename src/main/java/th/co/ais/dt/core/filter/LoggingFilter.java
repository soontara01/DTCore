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
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private static final int MAX_BODY_REQ_LENGTH = 10000;
    private static final int MAX_BODY_RES_LENGTH = 2000;

    private static final Set<String> MASKED_HEADERS = Set.of(
            "authorization", "cookie", "set-cookie", "x-api-key", "proxy-authorization"
    );

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
                String requestHeaders = collectHeaders(request);
                String responseHeaders = collectResponseHeaders(response);

                log.info("method={} uri={} query={} requestHeaders={} requestBody={} responseStatus={} responseHeaders={} responseBody={} durationMs={}",
                        request.getMethod(),
                        request.getRequestURI(),
                        request.getQueryString(),
                        requestHeaders,
                        requestBody,
                        response.getStatus(),
                        responseHeaders,
                        responseBody,
                        duration);
            } catch (Exception e) {
                e.printStackTrace();
            }

            wrappedResponse.copyBodyToResponse();
        }
    }

    private String collectHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream()
                .map(name -> name + "=" + maskValue(name, request.getHeader(name)))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    private String collectResponseHeaders(HttpServletResponse response) {
        return response.getHeaderNames().stream()
                .map(name -> name + "=" + maskValue(name, response.getHeader(name)))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    private String maskValue(String headerName, String value) {
        return MASKED_HEADERS.contains(headerName.toLowerCase()) ? "***" : value;
    }

    private String truncateReq(String body) {
        if (body == null || body.isEmpty()) return "-";
        return body.length() <= MAX_BODY_REQ_LENGTH ? body : body.substring(0, MAX_BODY_REQ_LENGTH) + "...[truncated]";
    }

    private String truncateRes(String body) {
        if (body == null || body.isEmpty()) return "-";
        return body.length() <= MAX_BODY_RES_LENGTH ? body : body.substring(0, MAX_BODY_RES_LENGTH) + "...[truncated]";
    }
}
