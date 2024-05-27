package utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiTokenGenerator {

    private static final String TOKEN_ENDPOINT = PropertiesUtil.getProperty("token.endpoint");
    private static final String CLIENT_ID = PropertiesUtil.getProperty("client.id");
    private static final String CLIENT_SECRET = PropertiesUtil.getProperty("client.secret");
    private static final String USERNAME = PropertiesUtil.getProperty("username");
    private static final String PASSWORD = PropertiesUtil.getProperty("password");

    public static String generateBearerToken() throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(TOKEN_ENDPOINT);

        // Request parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("client_id", CLIENT_ID));
        params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        params.add(new BasicNameValuePair("username", USERNAME));
        params.add(new BasicNameValuePair("password", PASSWORD));

        // Set request body
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        // Execute the request
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            JSONObject jsonResponse = new JSONObject(EntityUtils.toString(entity));
            String token = jsonResponse.getString("access_token");
            return token;
        } else {
            throw new Exception("Failed to generate bearer token");
        }
    }
}