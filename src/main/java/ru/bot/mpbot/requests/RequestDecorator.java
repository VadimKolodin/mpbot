package ru.bot.mpbot.requests;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bot.mpbot.exception.*;


import java.io.IOException;


public class RequestDecorator {
    private final Logger LOGGER = LoggerFactory.getLogger(RequestDecorator.class);
    private final ApiExecutable apiRequest;
    private final boolean isOzon;

    public RequestDecorator(ApiExecutable apiRequest){
        if (apiRequest==null){
            throw new IllegalArgumentException("ApiRequest cannot be null");
        }
        this.apiRequest=apiRequest;
        this.isOzon = apiRequest.getClass().getSimpleName().toLowerCase().contains("ozon");
    }

    public JsonNode execute(String strOffset) throws IOException{
        int attempts=3;
        JsonNode result = null;
        IOException exception = null;
        do{
            try{
                result = apiRequest.execute(strOffset);
                attempts=0;
            } catch (HttpResponseException e){
                LOGGER.error("Exception making analytics request", e);
                switch (e.getStatusCode()){
                    case 401:
                    case 403:
                        throw (isOzon?new NotAuthorizedOzonException():new NotAuthorizedWBException());
                    case 429:
                        LOGGER.info("RETRYING...");
                        attempts--;
                        exception = (isOzon?new TooManyRequestsOzonException():new TooManyRequestsWBException());
                        try {LOGGER.error("Waiting to make new request", e);
                            Thread.sleep(60*1000);
                        } catch (InterruptedException ex) {
                            LOGGER.error("Error while repeating request", e);
                            throw new IOException("Error while repeating request");
                        }
                        break;
                    case 500:
                        LOGGER.info("RETRYING...");
                        attempts--;
                        exception = (isOzon?new ServerDownOzonException():new ServerDownWBException());
                        try {
                            Thread.sleep(60*1000);
                        } catch (InterruptedException ex) {
                            LOGGER.error("Error while repeating request", e);
                            throw new IOException("Error while repeating request");
                        }
                        break;
                }
            }
        }while(attempts>0);

        if (exception==null){
            return result;
        }
        throw exception;
    }
}
