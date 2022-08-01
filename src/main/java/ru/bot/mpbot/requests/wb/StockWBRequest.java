package ru.bot.mpbot.requests.wb;

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
import ru.bot.mpbot.requests.ApiExecutable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StockWBRequest implements ApiExecutable {
    private final Logger LOGGER = LoggerFactory.getLogger(StockWBRequest.class);
    private final static String BODY = "https://suppliers-api.wildberries.ru/api/v2/stocks?skip=%d&take=%d";
    private final String apikey;

    public StockWBRequest(String apikey) {
        this.apikey = apikey;
    }

    public JsonNode execute(String strOffset) throws IOException {
        int offset=0;
        try {
            offset = Integer.parseInt(strOffset);
        }catch (NumberFormatException e){}

        Content content = Request.Get(
                        String.format(BODY, offset, 1000))
                .setHeader("Authorization", apikey)
                .execute().returnContent();

        return new ObjectMapper().readTree(content.asString(StandardCharsets.UTF_8));
    }
}
