package com.kravchenko.designermanager.security;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class SecurityConnector implements UrlConnector {

    @Autowired
    Environment environment;

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConnector.class);

    private List<String> cookies;
    private HttpURLConnection conn;
    public static final String USER_AGENT = "Mozilla/5.0";
    public static final String url = "http://stendy-vsem.com/admin/";


    @Override
    public List<String> findOrderedItemsInfo(List<String> orders) throws Exception {
        CookieHandler.setDefault(new CookieManager());
        getPageContent(url);
        String post = sendPost(url, getPostParams());
        return getPageContentNext(createUrlTarget(post, orders));
    }

    private String getPageContent(String url) throws Exception {
        String result = performGet(url);
        setCookies(conn.getHeaderFields().get("Set-Cookie"));
        LOGGER.debug(result);
        return result;
    }


    private String sendPost(String url, String postParams) throws Exception {
        URL obj = new URL(url);
        conn = (HttpURLConnection) obj.openConnection();
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Host", "stendy-vsem.com");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        for (String cookie : this.cookies) {
            conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
        }
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Referer", "http://stendy-vsem.com/admin/");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
        conn.setDoOutput(true);
        conn.setDoInput(true);
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        LOGGER.info("\nSending 'POST' request to URL : " + url);
        LOGGER.info("Response Code : " + responseCode);

        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        String result = response.toString();
        in.close();
        LOGGER.debug(result);
        return result;
    }

    private Map<String, String> createUrlTarget(String body, List<String> orders) {
        Map<String, String> urlTargets = new HashMap<>();
        String urlTarget = "";
        int startToken = body.indexOf("token");
        String value = body.substring(startToken, startToken + 38);
        LOGGER.debug("Token :" + value);
        for (String order : orders) {
            urlTarget = "http://stendy-vsem.com/admin/index.php?route=sale/order/invoice&" + value + "&order_id=" + order;
            urlTargets.put(order, urlTarget);
        }
        return urlTargets;
    }

    private List<String> getPageContentNext(Map<String, String> urls) throws Exception {
        List<String> result = new ArrayList<>();
        String url = "";
        for (String orderNumber : urls.keySet()) {
            url = urls.get(orderNumber);
            String currentPage = performGet(url);
            LOGGER.debug(currentPage);
            result.add(currentPage);
        }
        return result;
    }

    private String performGet(String url) throws IOException {
        String result;
        URL obj = new URL(url);
        conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if (cookies != null) {
            for (String cookie : this.cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
        int responseCode = conn.getResponseCode();
        LOGGER.info("\nSending 'GET' request to URL : " + url);
        LOGGER.info("Response Code : " + responseCode);
        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        // Get the response cookies
        result = response.toString();
        return result;
    }

    private String getPostParams() {
        String login = environment.getRequiredProperty("site.login");
        String password = environment.getRequiredProperty("site.password");
        return "username=" + login + "&password=" + password;
    }
}
