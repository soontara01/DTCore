package th.co.ais.dt.core.util;

import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

public class HttpClientUtil {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json;charset=utf-8";

    HttpClientUtil() {}

    public static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put(CONTENT_TYPE, APPLICATION_JSON);
        httpHeaders.setAll(properties);

        return httpHeaders;
    }
}
