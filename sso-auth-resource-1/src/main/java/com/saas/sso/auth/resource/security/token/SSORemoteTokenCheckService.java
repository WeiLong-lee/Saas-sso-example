package com.saas.sso.auth.resource.security.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * token服务
 *
 * @author feng
 * @date 2019/12/11
 */
@Slf4j
@Component
public class SSORemoteTokenCheckService {

    private RestOperations restTemplate;

    @Value("#{'${custom.properties.auth.client-id}'}")
    private String clientId;

    @Value("#{'${custom.properties.auth.client-secret}'}")
    private String clientSecret;

    @Value("#{'${custom.properties.auth.sso-auth-server-url}'}")
    private String checkTokenEndpointUrl;

    private String tokenName = "token";

    public SSORemoteTokenCheckService() {
        restTemplate = new RestTemplate();
        ((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    public Map<String, Object> loadAuthentication(String accessToken) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(tokenName, accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));
        Map<String, Object> map = postForMap(checkTokenEndpointUrl, formData, headers);

        if (map.containsKey("error")) {
            if (log.isDebugEnabled()) {
                log.debug("check_token returned error: " + map.get("error"));
            }
            // TODO 定义Exception
            // throw new InvalidTokenException(accessToken);
            throw new RuntimeException("InvalidTokenException:" + accessToken);
        }

        if (!Boolean.TRUE.equals(map.get("active"))) {
            log.debug("check_token returned active attribute: " + map.get("active"));
            // throw new InvalidTokenException(accessToken);
            throw new RuntimeException("InvalidTokenException:" + accessToken);
        }

        return map;
    }

    private String getAuthorizationHeader(String clientId, String clientSecret) {

        if (clientId == null || clientSecret == null) {
            log.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
        }

        String creds = String.format("%s:%s", clientId, clientSecret);
        return "Basic " + new String(Base64Utils.encode(creds.getBytes(StandardCharsets.UTF_8)));
    }

    private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        @SuppressWarnings("rawtypes")
        Map map = restTemplate
                .exchange(path, HttpMethod.POST, new HttpEntity<>(formData, headers), Map.class)
                .getBody();
        @SuppressWarnings("unchecked")
        Map<String, Object> result = map;
        return result;
    }

}
