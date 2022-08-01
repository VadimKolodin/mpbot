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
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrderWbRequest implements ApiExecutable {
    private Logger LOGGER = LoggerFactory.getLogger(OrderWbRequest.class);
    private static final String URI = "https://suppliers-api.wildberries.ru/api/v2/orders" +
            "?date_start=%s&date_end=%s&status=%d&take=1000&skip=%d";

    private final LocalDate from;
    private final LocalDate to;
    private final int status;
    private final String apikey;

    public OrderWbRequest(LocalDate from, LocalDate to, int status, String apikey) {
        this.from = from;
        this.to = to;
        this.status=status;
        this.apikey = apikey;
    }
    public JsonNode execute(String strOffset) throws IOException {
        int offset=0;
        try {
            offset = Integer.parseInt(strOffset);
        }catch (NumberFormatException e){}

        String uri = String.format(URI,
                from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"T0%3A00%3A00%2B03%3A00",
                to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"T0%3A00%3A00%2B03%3A00",
                status,
                offset);
        Content content = content = Request.Get(uri)
                .setHeader("Authorization", apikey).execute().returnContent();

        return new ObjectMapper().readTree(content.asString(StandardCharsets.UTF_8));
    }
}
