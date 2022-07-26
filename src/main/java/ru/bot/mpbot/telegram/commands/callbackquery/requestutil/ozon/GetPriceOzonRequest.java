package ru.bot.mpbot.telegram.commands.callbackquery.requestutil.ozon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bot.mpbot.exception.NotAuthorizedOzonException;
import ru.bot.mpbot.exception.ServerDownOzonException;
import ru.bot.mpbot.exception.TooManyRequestsOzonException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GetPriceOzonRequest {
    private final Logger LOGGER = LoggerFactory.getLogger(GetPriceOzonRequest.class);
    private static final String URI = "http://api-seller.ozon.ru/v2/product/info/list";
    public final static String PRICE_OZON = "{\n" +
            "    \"product_id\": %s }";
    public final static String PRICE_OZON_SKU = "{\n" +
            "    \"sku\": %s }";
    
    private final String apikey;
    private final String clientId;
    private final Long[] ids;

    public GetPriceOzonRequest(String apikey, String clientId, Long[] ids) {
        this.apikey = apikey;
        this.clientId = clientId;
        this.ids = ids;
    }
    
    public JsonNode execute() throws IOException {
        StringBuilder requestIds = new StringBuilder();
        requestIds.append("[ ");
        try {
            for (int j = 0; j < ids.length; ++j) {
                requestIds.append("\"" + ids[j] + "\",");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOGGER.info("Error creating fill price request", ex);
        }
        requestIds.deleteCharAt(requestIds.length() - 1);
        requestIds.append(" ]");
        Content postResultForm = Request.Post(URI)
                .setHeader("Client-Id", clientId)
                .setHeader("Api-Key", apikey)
                .bodyString( String.format(PRICE_OZON, requestIds.toString()), ContentType.APPLICATION_JSON)
                .execute().returnContent();
        return new ObjectMapper().readTree(postResultForm.asString(StandardCharsets.UTF_8));
    }
    public JsonNode executeSKU() throws IOException {
        StringBuilder requestIds = new StringBuilder();
        requestIds.append("[ ");
        try {
            for (int j = 0; j < ids.length; ++j) {
                requestIds.append("\"" + ids[j] + "\",");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOGGER.info("Error creating fill price request", ex);
        }
        requestIds.deleteCharAt(requestIds.length() - 1);
        requestIds.append(" ]");
        Content postResultForm=null;
        try {
            postResultForm = Request.Post(URI)
                    .setHeader("Client-Id", clientId)
                    .setHeader("Api-Key", apikey)
                    .bodyString(String.format(PRICE_OZON_SKU, requestIds.toString()), ContentType.APPLICATION_JSON)
                    .execute().returnContent();
        } catch(HttpResponseException e){
            LOGGER.error("Exception making analytics request", e);
            switch (e.getStatusCode()){
                case 401:
                case 403:
                    throw new NotAuthorizedOzonException();
                case 429:
                    throw new TooManyRequestsOzonException();
                case 500:
                    throw new ServerDownOzonException();
            }
        }
        return new ObjectMapper().readTree(postResultForm.asString(StandardCharsets.UTF_8));
    }
}
