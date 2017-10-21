package com.pjcdarker.j9.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author pjcdarker
 * @created 10/14/2017.
 */
public class HttpClientManager {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static Map<String, Object> get() {

        Map<String, Object> resultMap = new HashMap<>();

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("https://github.com")).GET().build();
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


    public static Map<String, Object> asyncGet() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("https://github.com")).GET().build();
            CompletableFuture<HttpResponse<String>> asyncHttpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandler.asString());

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


    public static void post() {

    }


    public static void main(String[] args) {
        Map<String, Object> resultMap = asyncGet();
        System.out.println(resultMap);
    }
}
