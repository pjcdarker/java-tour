package com.pjcdarer.j9.net;


import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @author pjcdarker
 */
public class HttpClientManager {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static Map<String, Object> get(String url) {
        Objects.requireNonNull(url, "request url require non null");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI(url)).GET().build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandler.asString());
            resultMap.put("status", httpResponse.statusCode());
            resultMap.put("data", httpResponse.body());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
            resultMap.put("status", "500");
            resultMap.put("data", "request fail");
        }
        return resultMap;
    }


    public static Map<String, Object> asyncGet(String url) {
        Objects.requireNonNull(url, "request url require non null");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
            CompletableFuture<HttpResponse<String>> asyncHttpResponse = httpClient.sendAsync(httpRequest,
                                                                                             HttpResponse.BodyHandler.asString());

            System.out.println("================executor other thing==================");
            HttpResponse<String> httpResponse = asyncHttpResponse.get();
            resultMap.put("status", httpResponse.statusCode());
            resultMap.put("data", httpResponse.body());

        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            resultMap.put("status", "500");
            resultMap.put("data", "request fail");
        }
        return resultMap;
    }


    public static void post(String url) {

    }

    public static void asyncPost(String url) {

    }


    public static void main(String[] args) {
        String url = "https://github.com";
        Map<String, Object> resultMap = asyncGet(url);
        System.out.println(resultMap);
    }
}
