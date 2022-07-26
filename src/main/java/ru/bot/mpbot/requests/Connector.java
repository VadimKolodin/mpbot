package ru.bot.mpbot.requests;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bot.mpbot.telegram.constants.Colors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Connector {
    private static Logger LOGGER = LoggerFactory.getLogger(Connector.class);

    public static  void createSimpleRequest(URL url){
        LOGGER.info("Trying to connect to "+ Colors.GREEN.get()+url+Colors.RESET.get());
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            LOGGER.info("Got: "+Colors.GREEN.get()+content+Colors.RESET.get());
        } catch (IOException e) {
            LOGGER.error(Colors.RED.get()+"Error connecting to "+url+Colors.RESET.get(), e);
        }
    }
    public static Response postRequest(String uri, HashMap<String, String> headers, String body){
            LOGGER.info("Trying to connect to "+ Colors.GREEN.get()+uri+Colors.RESET.get());
          Request request = Request.Post(uri)
                .bodyString(body, ContentType.APPLICATION_JSON);
          for (Map.Entry<String, String> header: headers.entrySet()){
              request.setHeader(header.getKey(), header.getValue());
          }
        try {
            return request.execute();
        } catch (IOException e) {
            LOGGER.error("Error making request to"+Colors.RED+uri+Colors.RESET, e);
            return null;
        }

    }
    public static Response getRequest(String uri, HashMap<String, String> headers){
        LOGGER.info("Trying to connect to "+ Colors.GREEN.get()+uri+Colors.RESET.get());
        Request request = Request.Get(uri);
        for (Map.Entry<String, String> header: headers.entrySet()){
            request.setHeader(header.getKey(), header.getValue());
        }
        try {
            return request.execute();
        } catch (IOException e) {
            LOGGER.error("Error making request to"+Colors.RED+uri+Colors.RESET, e);
            return null;
        }

    }
}
