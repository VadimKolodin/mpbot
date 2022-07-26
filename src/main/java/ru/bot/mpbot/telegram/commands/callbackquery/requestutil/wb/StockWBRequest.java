package ru.bot.mpbot.telegram.commands.callbackquery.requestutil.wb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bot.mpbot.exception.NotAuthorizedOzonException;
import ru.bot.mpbot.exception.ServerDownOzonException;
import ru.bot.mpbot.exception.TooManyRequestsOzonException;

import java.io.IOException;
import java.nio.charset.Charset;

public class StockWBRequest {
    private final Logger LOGGER = LoggerFactory.getLogger(StockWBRequest.class);
    private final static String STOCK_WB = "https://suppliers-api.wildberries.ru/api/v2/stocks?skip=%d&take=%d";
    private final String apikey;

    public StockWBRequest(String apikey) {
        this.apikey = apikey;
    }

    public JsonNode execute(int offset) throws IOException {
        Content postResultForm=null;
        try {
        postResultForm = Request.Get(
                        String.format(STOCK_WB, offset, 1000))
                .setHeader("Authorization", apikey)
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
        return new ObjectMapper().readTree(postResultForm.asString(Charset.forName("UTF-8")));
    }
}
