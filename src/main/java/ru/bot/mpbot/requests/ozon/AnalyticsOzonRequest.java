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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AnalyticsOzonRequest implements ApiExecutable {
    private Logger LOGGER = LoggerFactory.getLogger(AnalyticsOzonRequest.class);

    private static final String URI = "https://api-seller.ozon.ru/v1/analytics/data";
    private static final String BODY = """
       {
             "date_from": "%s",
             "date_to": "%s",
             "metrics": [
                 "%s"
             ],
             "dimension": [
                 "sku",
                 "day"
             ],
             "filters": [
             {
                    "key":"%s",
                    "op":"GT",
                    "value":"0"
             }
             ],
             "limit": 1000,
             "offset": %d
       }
            """;

    private final LocalDate from;
    private final LocalDate to;
    private final String apikey;
    private final String clientId;
    private String metrics;

    public AnalyticsOzonRequest(LocalDate from, LocalDate to, String apikey, String clientId, String metrics) {
        this.from = from;
        this.to = to;
        this.apikey = apikey;
        this.clientId = clientId;
        this.metrics = metrics;
    }

    public JsonNode execute(String strOffset) throws IOException {
        int offset=0;
        try {
            offset = Integer.parseInt(strOffset);
        }catch (NumberFormatException e){}
        String body = String.format(BODY,
                from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                metrics,
                metrics,
                offset);
        Content content=null;
        try {
            content = Request.Post(URI)
                    .setHeader("Client-Id", clientId)
                    .setHeader("Api-Key", apikey)
                    .bodyString(body, ContentType.APPLICATION_JSON)
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
        return new ObjectMapper().readTree(content.asString(StandardCharsets.UTF_8));
    }
}
