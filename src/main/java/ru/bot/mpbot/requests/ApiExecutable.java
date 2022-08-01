package ru.bot.mpbot.requests;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface ApiExecutable {
    public JsonNode execute (String offset) throws IOException;
}
