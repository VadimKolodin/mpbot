package ru.bot.mpbot.requests.ozon;

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
import ru.bot.mpbot.requests.ApiExecutable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GetPriceOzonRequest implements ApiExecutable {
    private final Logger LOGGER = LoggerFactory.getLogger(GetPriceOzonRequest.class);
    private static final String URI = "http://api-seller.ozon.ru/v2/product/info/list";
    public final static String PRICE_OZON = "{\n" +
            "    \"product_id\": %s }";
    public final static String PRICE_OZON_SKU = "{\n" +
            "    \"sku\": %s }";
    
    private final String apikey;
    private final String clientId;
    private final Long[] ids;
    private final boolean isSKU;

    public GetPriceOzonRequest(String apikey, String clientId, Long[] ids, boolean isSKU) {
        this.apikey = apikey;
        this.clientId = clientId;
        this.ids = ids;
        this.isSKU=isSKU;
    }
    
    public JsonNode execute(String offset) throws IOException {

        StringBuilder requestIds = new StringBuilder();
        requestIds.append("[ ");
        try {
            for (int j = 0; j < ids.length; ++j) {
                requestIds.append("\"").append(ids[j]).append("\",");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOGGER.info("Error creating fill price request", ex);
        }
        requestIds.deleteCharAt(requestIds.length() - 1);
        requestIds.append(" ]");
        Content postResultForm = Request.Post(URI)
                .setHeader("Client-Id", clientId)
                .setHeader("Api-Key", apikey)
                .bodyString( String.format(
                        isSKU?PRICE_OZON_SKU:PRICE_OZON, requestIds.toString()),
                        ContentType.APPLICATION_JSON)
                .execute().returnContent();
        return new ObjectMapper().readTree(postResultForm.asString(StandardCharsets.UTF_8));
    }

}
