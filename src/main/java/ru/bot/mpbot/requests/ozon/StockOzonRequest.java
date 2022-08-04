package ru.bot.mpbot.requests.ozon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bot.mpbot.requests.ApiExecutable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StockOzonRequest implements ApiExecutable {
    private final Logger LOGGER = LoggerFactory.getLogger(StockOzonRequest.class);
    private static final String URI = "https://api-seller.ozon.ru/v3/product/info/stocks";
    public final static String BODY = "{\n" +
            "    \"filter\": {\n" +
            "        \"visibility\": \"ALL\"\n" +
            "    },\n" +
            "    \"last_id\": \"%s\",\n" +
            "    \"limit\": 1000\n" +
            "}";
    private final String apikey;
    private final String clientId;

    public StockOzonRequest(String apikey, String clientId){
        this.apikey = apikey;
        this.clientId = clientId;
    }
    public JsonNode execute (String lastId) throws IOException {

         Response response= Request.Post(URI)
                .setHeader("Client-Id", clientId)
                .setHeader("Api-Key", apikey)
                .bodyString(String.format(BODY, lastId), ContentType.APPLICATION_JSON)
                .execute();
                Content content=response.returnContent();

        return new ObjectMapper().readTree(content.asString(StandardCharsets.UTF_8));
    }
}
