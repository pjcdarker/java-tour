package com.pjcdarker.util.http;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class HttpClientManager {

    private static final String CONTENT_TYPE = "application/json";
    private static final String SERVER_HOME = "jetty.home";
    private static final String WEB_APPS_DIR = "/webapps";

    private static HttpClientBuilder httpClientBuilder = init();

    /**
     * CloseableHttpClient
     *
     * @return
     */
    public static CloseableHttpClient getCloseableHttpClient() {
        return httpClientBuilder.build();
    }

    private static HttpClientBuilder init() {
        if (httpClientBuilder == null) {
            httpClientBuilder = createHttpClientBuilder();
        }
        return httpClientBuilder;
    }

    private static HttpClientBuilder createHttpClientBuilder() {
        ConnectionSocketFactory plainConnectionSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainConnectionSocketFactory)
                .register("https", sslConnectionSocketFactory)
                .build();

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);

        // set max connection
        poolingHttpClientConnectionManager.setMaxTotal(1000);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(400);

        // set request timeout
        RequestConfig.Builder requestBuilder = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setContentCompressionEnabled(true);

        // create httpClient
        HttpClientBuilder builder = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestBuilder.build())
                .setConnectionManager(poolingHttpClientConnectionManager);

        return builder;
    }

    public static String exec(final String url, String method, Map<String, String> params, HttpEntity requestEntity) {
        CloseableHttpClient client = getCloseableHttpClient();
        HttpRequestBase request = null;
        try {
            if ("GET".equalsIgnoreCase(method)) {
                request = new HttpGet();
            } else if ("POST".equalsIgnoreCase(method)) {
                request = new HttpPost();
                if (requestEntity != null) {
                    ((HttpPost) request).setEntity(requestEntity);
                }
            }
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            request.setURI(builder.build());
            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String respBody = EntityUtils.toString(entity, Consts.UTF_8);
            if (entity != null) {
                EntityUtils.consume(entity);
            }
            return respBody;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            if (request != null) {
                request.abort();
                request.releaseConnection();
            }
        }
        return "";
    }

    /**
     * http GET
     *
     * @param url
     * @param params
     * @return
     */
    public static String get(final String url, Map<String, String> params) {
        return exec(url, "GET", params, null);
    }

    /**
     * POST
     *
     * @param url
     * @param params
     * @param requestEntity
     * @return
     */
    public static String post(final String url, Map<String, String> params, HttpEntity requestEntity) {
        return exec(url, "POST", params, requestEntity);
    }

    /**
     * POST JSON
     *
     * @param url
     * @param params
     * @return
     */
    public static String postToJson(final String url, Map<String, String> params) {
        StringEntity entity = new StringEntity(params.toString(), Charset.forName("UTF-8"));
        entity.setContentType(CONTENT_TYPE);
        return exec(url, "POST", params, entity);
    }

    /**
     * POST Multipart/form-data
     *
     * @param url
     * @param filePath
     * @return
     */
    public static String postMultipartFormData(final String url, final String filePath) {
        File file = new File(filePath);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", new FileBody(file));
        return exec(url, "POST", null, builder.build());
    }

    /**
     * download
     *
     * @param fileUrl
     * @param saveFileDir
     * @param fileName
     * @return
     */
    public static String download(final String fileUrl, String saveFileDir, final String fileName) {
        HttpGet httpGet = new HttpGet(fileUrl);
        String filePath = "";
        try {
            CloseableHttpResponse httpResponse = getCloseableHttpClient().execute(httpGet);
            System.out.println(httpResponse);
            if (200 == httpResponse.getStatusLine().getStatusCode()) {
                HttpEntity entity = httpResponse.getEntity();
                filePath = System.getProperty(SERVER_HOME) + WEB_APPS_DIR + saveFileDir;
                filePath += fileName;
                Path path = Paths.get(filePath);
                try (BufferedOutputStream BufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(path))) {
                    entity.writeTo(BufferedOutputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpGet != null) {
                httpGet.abort();
                httpGet.releaseConnection();
            }
        }
        return filePath;
    }

    public static void main(String[] args) {
        String body = HttpClientManager.get("https://github.com/explore", null);
        System.out.println(body);
    }
}
