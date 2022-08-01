package ru.bot.mpbot.telegram.commands.callbackquery.capitalize;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.exception.RequestExceptionHandler;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.exception.NotAuthorizedOzonException;
import ru.bot.mpbot.exception.ServerDownOzonException;
import ru.bot.mpbot.exception.TooManyRequestsOzonException;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.requests.ApiExecutable;
import ru.bot.mpbot.requests.RequestDecorator;
import ru.bot.mpbot.requests.ozon.StockOzonRequest;
import ru.bot.mpbot.requests.wb.InfoWBRequest;
import ru.bot.mpbot.telegram.commands.BotMediaCommand;
import ru.bot.mpbot.requests.ozon.GetPriceOzonRequest;
import ru.bot.mpbot.requests.wb.StockWBRequest;
import ru.bot.mpbot.telegram.constants.ErrorConst;
import ru.bot.mpbot.telegram.constants.MessageConst;
import ru.bot.mpbot.telegram.misc.PieChartImage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class CapitalizeCommand extends BotMediaCommand {

    private static Logger LOGGER = LoggerFactory.getLogger(CapitalizeCommand.class);
    private static final int LOAD_FACTOR = 1000;

    private final Long chatId;
    private Client client;
    private double totalOzon;
    private double totalWB;

    public CapitalizeCommand(Long chatId){
        this.chatId=chatId;
    }

    @Override
    public void execute() throws IOException{
        String finalAnswer = ErrorConst.INTERNAL_ERROR.getMessage();
        try {
            String ozonAnswer;
            String wbAnswer;
            ClientService clientService = SpringContext.getBean(ClientService.class);
            client = clientService.getClientByTgId(chatId);
            if (client.getOznId() != null && client.getOznKey() != null) {
                try {
                    ozonAnswer = ozonCapitalize() + "\n";
                } catch (IOException ioe){
                    ozonAnswer = "*Ozon:* "+new RequestExceptionHandler().handle(ioe)+ "\n";
                }
            } else {
                ozonAnswer = ErrorConst.NO_OZON_KEY_COMMAND.getMessage() + "\n";
            }
            if (client.getWbKey() != null) {
                try {
                    wbAnswer = wbCapitalize() + "\n";
                } catch (IOException ioe){
                    wbAnswer = "*WB:* "+new RequestExceptionHandler().handle(ioe)+ "\n";
                }
            } else {
                wbAnswer = ErrorConst.No_WB_KEY_COMMAND.getMessage() + "\n";
            }

            finalAnswer = (ozonAnswer != null ? ozonAnswer : "Ozon: " + ErrorConst.INTERNAL_ERROR.getMessage()) +
                    (wbAnswer != null ? wbAnswer : "Wildberries: " + ErrorConst.INTERNAL_ERROR.getMessage());
            if (ozonAnswer != null && wbAnswer != null) {
                HashMap<String, Double> map = new HashMap<>();
                map.put("Ozon", totalOzon);
                map.put("WB", totalWB);
                PieChartImage pieChartImage = new PieChartImage(map);
                File file = pieChartImage.drawToFile(
                        "temp/f" + chatId + "_"
                                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH.mm.dd.MM.yyyy"))
                                + ".png", "Капитализация складов");
                this.mediaAnswer = new SendPhoto(chatId.toString(), new InputFile(file));
                ((SendPhoto) this.mediaAnswer).setCaption(finalAnswer);
            } else {
                this.answer = new SendMessage(chatId.toString(), finalAnswer);
            }
            super.execute();
        }catch (IOException e){
            LOGGER.error("Error drawing piechart", e);
            this.answer = new SendMessage(chatId.toString(), finalAnswer);
            super.execute();
        }
    }

    private String ozonCapitalize() throws IOException{

        HashMap<Long, Product> products = getStockInfoOzon();
        fillPriceOzon(products);
        double fboTotal = 0;
        double fbsTotal = 0;
        for (Product product : products.values()) {
            fboTotal += product.capitalizeFbo();
            fbsTotal += product.capitalizeFbs();
        }
        totalOzon=fboTotal+fbsTotal;
        return String.format(MessageConst.CAPITALIZE_OZON.getMessage(), fboTotal, fbsTotal);

    }
    public HashMap<Long, Product> getStockInfoOzon() throws IOException {
        String clientId = client.getOznId();
        String apikey = client.getOznKey();

        RequestDecorator request = new RequestDecorator(new StockOzonRequest(apikey, clientId));

        JsonNode node = request.execute("").get("result");

        int total = node.get("total").asInt();
        String lastId = node.get("last_id").asText();

        HashMap<Long, Product> products = new HashMap<>();
        addNotNullItemsOzon(products, node.get("items"));

        total -= LOAD_FACTOR;
        while(total>0){

            node = request.execute(lastId).get("result");

            lastId = node.get("last_id").asText();
            addNotNullItemsOzon(products, node.get("items"));
            total -= LOAD_FACTOR;
        }
        return products;
    }
    public void fillPriceOzon(HashMap<Long, Product> products) throws IOException {
        String clientId = client.getOznId();
        String apikey = client.getOznKey();
        Long[] keys = new Long[products.size()];
        keys = products.keySet().toArray(keys);
        int step = keys.length/1000;

        JsonNode items = new RequestDecorator(
                new GetPriceOzonRequest(apikey, clientId, keys, false))
                .execute("");
        items = items.get("result").get("items");
        addPriceOzon(products, items);
    }
    private static void addPriceOzon(HashMap<Long, Product> products, JsonNode array){
        for(JsonNode element: array){
            products.get(element.get("id").asLong()).setPrice(element.get("marketing_price").asDouble());
        }
    }
    private void addNotNullItemsOzon(HashMap<Long, Product> products, JsonNode array){
        for (JsonNode element: array){
            JsonNode stocks = element.get("stocks");
            JsonNode fbo = stocks.get(0);
            JsonNode fbs = stocks.get(1);
            if (fbo.get("type").asText().equals("fbs")){
                JsonNode temp = fbo;
                fbo=fbs;
                fbs=temp;
            }
            Product temp = new Product(element.get("product_id").asInt(),
                    fbo.get("present").asInt(),
                    fbo.get("reserved").asInt(),
                    fbs.get("present").asInt(),
                    fbs.get("reserved").asInt());
            if (temp.getFboStock()+temp.getFbsStock()>0){
                products.put(temp.getId(), temp);
            }
        }
    }

    private String wbCapitalize() throws IOException{

        HashMap<Long, Product> products = getStockInfoWB();
        fillPriceWB(products);
        double total = 0;

        for (Product product : products.values()) {
            total += product.capitalizeFbo();
        }
        totalWB=total;
        return String.format(MessageConst.CAPITALIZE_WB.getMessage(),total);
    }
    public HashMap<Long, Product> getStockInfoWB() throws IOException {
        String apikey = client.getWbKey();
        RequestDecorator request = new RequestDecorator(new StockWBRequest(apikey));
        JsonNode node = request.execute("0");

        int total = node.get("total").asInt();
        HashMap<Long, Product> products = new HashMap<>();
        addNotNullItemsWB(products, node.get("stocks"));

        int remain = total-LOAD_FACTOR;
        while(remain>0){
            node = request.execute(Integer.toString(total-remain));
            addNotNullItemsOzon(products, node);
            remain -= LOAD_FACTOR;
        }
        return products;
    }
    private void addNotNullItemsWB(HashMap<Long, Product> products, JsonNode array){
        for (JsonNode element: array){
            JsonNode stocks = element.get("stock");

            Product temp = new Product(element.get("nmId").asInt(),
                    stocks.asInt(),
                    0,
                    0,
                    0);

            if (temp.getFboStock()>0){
                products.put(temp.getId(), temp);
            }
        }
    }
    public void fillPriceWB(HashMap<Long, Product> products) throws IOException {
        String apikey = client.getWbKey();

        JsonNode items = new RequestDecorator(
                new InfoWBRequest(apikey))
                .execute("");
        addPriceWB(products, items);
    }
    private static void addPriceWB(HashMap<Long, Product> products, JsonNode array){
        for(JsonNode element: array){
            Product temp = products.get(element.get("nmId").asLong());
            if (temp!=null){
                temp.setPrice(element.get("price").asDouble());
            }
        }
    }


    class Product{
        private long id;
        private int fboStock;
        private int fboReserved;
        private int fbsStock;
        private int fbsReserved;
        private double price;

        public Product(long id, int fboStock, int fboReserved, int fbsStock, int fbsReserved, double price) {
            this.id = id;
            this.fboStock = fboStock;
            this.fboReserved = fboReserved;
            this.fbsStock = fbsStock;
            this.fbsReserved = fbsReserved;
            this.price = price;
        }

        public Product(long id, int fboStock, int fboReserved, int fbsStock, int fbsReserved) {
            this.id = id;
            this.fboStock = fboStock;
            this.fboReserved = fboReserved;
            this.fbsStock = fbsStock;
            this.fbsReserved = fbsReserved;
        }

        public double capitalizeFbs(){
            return (fbsStock-fbsReserved)*price;
        }

        public double capitalizeFbsWithReserved(){
            return (fbsStock)*price;
        }
        public double capitalizeFbo(){
            return (fboStock-fboReserved)*price;
        }

        public double capitalizeFboWithReserved(){
            return (fboStock)*price;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getFboStock() {
            return fboStock;
        }

        public void setFboStock(int fboStock) {
            this.fboStock = fboStock;
        }

        public int getFboReserved() {
            return fboReserved;
        }

        public void setFboReserved(int fboReserved) {
            this.fboReserved = fboReserved;
        }

        public int getFbsStock() {
            return fbsStock;
        }

        public void setFbsStock(int fbsStock) {
            this.fbsStock = fbsStock;
        }

        public int getFbsReserved() {
            return fbsReserved;
        }

        public void setFbsReserved(int fbsReserved) {
            this.fbsReserved = fbsReserved;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

}
