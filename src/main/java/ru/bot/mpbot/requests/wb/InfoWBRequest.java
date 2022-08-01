package ru.bot.mpbot.requests.wb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import ru.bot.mpbot.requests.ApiExecutable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class InfoWBRequest implements ApiExecutable {
    private static final String URI = "https://suppliers-api.wildberries.ru/public/api/v1/info";

    private final String apikey;

    public InfoWBRequest(String apikey) {
        this.apikey = apikey;
    }

    public JsonNode execute(String strOffset) throws IOException{
        Content content = Request.Get(URI)
                .setHeader("Authorization", apikey)
                .execute().returnContent();
        return new ObjectMapper().readTree(content.asString(StandardCharsets.UTF_8));
    }
}
