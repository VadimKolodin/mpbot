package ru.bot.mpbot.telegram.commands.callbackquery.sales;

import com.fasterxml.jackson.databind.JsonNode;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.exception.RequestExceptionHandler;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.requests.RequestDecorator;
import ru.bot.mpbot.telegram.commands.BotMediaCommand;
import ru.bot.mpbot.requests.ozon.AnalyticsOzonRequest;
import ru.bot.mpbot.requests.ozon.GetPriceOzonRequest;
import ru.bot.mpbot.requests.wb.OrderWbRequest;
import ru.bot.mpbot.requests.wb.StockWBRequest;
import ru.bot.mpbot.telegram.constants.ErrorConst;
import ru.bot.mpbot.telegram.constants.MessageConst;
import ru.bot.mpbot.telegram.handler.ClientValidator;
import ru.bot.mpbot.telegram.handler.MenuKeyboardMaker;
import ru.bot.mpbot.telegram.misc.PieChartImage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class SalesTodayCommand extends BotMediaCommand {
    private final Logger LOGGER = LoggerFactory.getLogger(SalesTodayCommand.class);

    private Client client;
    private final boolean isDetailed;

    public SalesTodayCommand(Long chatId, boolean isDetailed) {
        ClientService clientService = SpringContext.getBean(ClientService.class);
        this.client = clientService.getClientByTgId(chatId);
        this.isDetailed=isDetailed;
    }
    public void execute() throws IOException {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now();

        if (isDetailed) {
            executeDetailed(from, to);
        } else {
            executeDefault(from, to);
            if (this.answer!=null) {
                this.answer.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class).getDetailedSales());
            }
            if (this.mediaAnswer!=null){
                this.mediaAnswer.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class).getDetailedSales());
            }
        }
        super.execute();
    }
    private void executeDetailed(LocalDate from, LocalDate to) throws IOException {
        String ozonAnswer=null;
        String wbAnswer=null;
        if (client==null){
            ozonAnswer = ErrorConst.NO_OZON_KEY_COMMAND.getMessage() + "\n";
            wbAnswer = ErrorConst.No_WB_KEY_COMMAND.getMessage();
            this.answer = new SendMessage(client.getTgId().toString(), ozonAnswer+wbAnswer);
            return;
        }
        ClientValidator validator = new ClientValidator(client);
        if (validator.validateOzon()) {
            try {
                HashMap<Long, Product> ozonSales = ozonSales(from, to);
                StringBuilder tempOzon;
                if (ozonSales.size() == 0) {
                    tempOzon = new StringBuilder().append(MessageConst.SALES_TODAY_OZON_ALL_EMPTY.getMessage());
                } else {
                    tempOzon = new StringBuilder().append(MessageConst.SALES_TODAY_OZON_ALL.getMessage());
                }
                for (Product product : ozonSales.values()) {
                    tempOzon.append("\n")
                            .append(product.getQuantity())
                            .append(" шт.")
                            .append(" - ")
                            .append(product.getName());
                }
                ozonAnswer = tempOzon.toString();
            } catch (IOException ioe){
            ozonAnswer = "*Ozon:* "+new RequestExceptionHandler().handle(ioe);
            }
        } else {
            ozonAnswer = validator.getErrorMessageOzon() + "\n";
        }
        if (validator.validateWB()) {
            try{
                HashMap<Long, Product> wbSales = wbSales(from, to);
                StringBuilder tempWb;
                if (wbSales.size() == 0) {
                    tempWb = new StringBuilder().append(MessageConst.SALES_TODAY_WB_ALL_EMPTY.getMessage());
                } else {
                    tempWb = new StringBuilder().append(MessageConst.SALES_TODAY_WB_ALL.getMessage());
                }
                for (Product product : wbSales.values()) {
                    tempWb.append("\n")
                            .append(product.getQuantity())
                            .append(" шт.")
                            .append(" - ")
                            .append(product.getName());
                }
                wbAnswer = tempWb.toString();
            } catch (IOException ioe){
                wbAnswer = "*WB:* "+new RequestExceptionHandler().handle(ioe);
            }
        } else {
            wbAnswer = validator.getErrorMessageWB() + "\n";
        }

        this.answer= new SendMessage (client.getTgId().toString(),
                (ozonAnswer != null ? ozonAnswer : "Ozon: " + ErrorConst.INTERNAL_ERROR.getMessage()) +"\n"+
                (wbAnswer != null ? wbAnswer : "Wildberries: " + ErrorConst.INTERNAL_ERROR.getMessage()));
    }
    private void executeDefault(LocalDate from, LocalDate to) throws IOException {
        String ozonAnswer=null;
        String wbAnswer=null;
        int totalOzon=0;
        double priceOzon=0;
        int totalWB=0;
        double priceWB=0;
        if (client==null){
            ozonAnswer = ErrorConst.NO_OZON_KEY_COMMAND.getMessage() + "\n";
            wbAnswer = ErrorConst.No_WB_KEY_COMMAND.getMessage();
            this.answer = new SendMessage(client.getTgId().toString(), ozonAnswer+wbAnswer);
            return;
        }
        ClientValidator validator = new ClientValidator(client);
        if (validator.validateOzon()) {
            try{
                HashMap<Long, Product> ozonSales = ozonSales(from, to);
                for (Product product : ozonSales.values()) {
                   priceOzon+=product.getTotalPrice();
                   totalOzon+=product.getQuantity();
                }
                ozonAnswer = String.format(MessageConst.SALES_TODAY_OZON_TOTAL.getMessage(),
                        totalOzon,
                        priceOzon);
            } catch (IOException ioe){
                ozonAnswer = "*Ozon:* "+new RequestExceptionHandler().handle(ioe);
            }
        } else {
            ozonAnswer = validator.getErrorMessageOzon() + "\n";
        }
        if (validator.validateWB()) {
            try {
                HashMap<Long, Product> wbSales = wbSales(from, to);

            for (Product product : wbSales.values()) {
                priceWB+=product.getTotalPrice();
                totalWB+=product.getQuantity();
            }
            wbAnswer = String.format(MessageConst.SALES_TODAY_WB_TOTAL.getMessage(),
                    totalWB,
                    priceWB);
            } catch (IOException ioe){
                wbAnswer = "*WB:* "+new RequestExceptionHandler().handle(ioe);
            }
        } else {
            wbAnswer = validator.getErrorMessageWB() + "\n";
        }
        try {
            if (ozonAnswer != null && wbAnswer != null && (totalOzon > 0 && totalWB > 0)) {
                HashMap<String, Double> map = new HashMap<>();
                map.put("Ozon", (double) totalOzon);
                map.put("WB", (double) totalWB);
                PieChartImage pieChartImage = new PieChartImage(map);
                File file1 = pieChartImage.drawToFile(
                        "temp/t" + client.getTgId() + "_"
                                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH.mm.dd.MM.yyyy"))
                                + ".png", "Продано штук");
                map = new HashMap<>();
                map.put("Ozon", priceOzon);
                map.put("WB", priceWB);
                File file2 = pieChartImage.drawToFile(
                        "temp/p" + client.getTgId() + "_"
                                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH.mm.dd.MM.yyyy"))
                                + ".png", "Продано в руб.");

                this.mediaAnswer = new SendPhoto(client.getTgId().toString(),
                        new InputFile(PieChartImage.joinCharts(file1, file2)));
                ((SendPhoto) this.mediaAnswer).setCaption(ozonAnswer + "\n" + wbAnswer);
            } else {
                this.answer = new SendMessage(client.getTgId().toString(),
                        (ozonAnswer != null ? ozonAnswer : "Ozon: " + ErrorConst.INTERNAL_ERROR.getMessage()) +"\n"+
                                (wbAnswer != null ? wbAnswer : "Wildberries: " + ErrorConst.INTERNAL_ERROR.getMessage()));
            }
        }catch (IOException e){
            LOGGER.error("Error drawing chart", e);
            this.answer = new SendMessage(client.getTgId().toString(),
                    (ozonAnswer != null ? ozonAnswer : "Ozon: " + ErrorConst.INTERNAL_ERROR.getMessage()) +"\n"+
                            (wbAnswer != null ? wbAnswer : "Wildberries: " + ErrorConst.INTERNAL_ERROR.getMessage()));
        }
    }
    private HashMap<Long, Product> ozonSales(LocalDate from, LocalDate to) throws IOException {
        JsonNode items = new RequestDecorator(
                new AnalyticsOzonRequest(
                from,
                to,
                client.getOznKey(),
                client.getOznId(),
                "ordered_units")).execute("0");
        HashMap<Long, Product> products = new HashMap(items.get("result").get("data").size()+3, 1);
        for (JsonNode element: items.get("result").get("data")){
            Long id = element.get("dimensions").get(0).get("id").asLong();
            products.put(id, new Product(
                    id,
                    element.get("dimensions").get(0).get("name").asText(),
                    element.get("metrics").get(0).asInt(),
                    0
            ));
        }
        Long[] keys = new Long[products.size()];
        keys = products.keySet().toArray(keys);
        JsonNode prices = new RequestDecorator(
                new GetPriceOzonRequest(
                        client.getOznKey(),
                    client.getOznId(),
                    keys, true))
                    .execute("");
        Product temp;
        for(JsonNode element: prices.get("result").get("items")){
            temp = products.get(element.get("sources").get(0).get("sku").asLong());
            if (temp==null){
                temp = products.get(element.get("sources").get(1).get("sku").asLong());
            }
            if (temp !=null) {
                temp.setPrice(element.get("marketing_price").asDouble());
            }
        }
        return products;
    }

    private HashMap<Long, Product> wbSales(LocalDate from, LocalDate to) throws IOException {
        JsonNode items = new RequestDecorator(
                new OrderWbRequest(
                from,
                to,
                2,
                client.getWbKey()))
                .execute("0");
        HashMap<Long, Product> products = new HashMap();
        for (JsonNode element: items.get("orders")){
            Long id = element.get("barcode").asLong();
            if (element.get("userStatus").asInt()<3) {
                if (products.containsKey(id)) {
                    products.get(id).addQuantity(1);
                } else {
                    products.put(id, new Product(
                            id,
                            null,
                            1,
                            element.get("totalPrice").asDouble() / 100
                    ));
                }
            }
        }
        RequestDecorator request = new RequestDecorator(
                new StockWBRequest(client.getWbKey())
        );
        items = request.execute("0");
        int total = items.get("total").asInt();
        Product temp;
        for (JsonNode element: items.get("stocks")){
            temp = products.get(element.get("barcode").asLong());
            if (temp!=null){
                temp.setName(String.format("%s (%s)",
                        element.get("article"),
                        element.get("name")));
            }
        }
        int remain = total-1000;
        while(remain>0){
            items = request.execute(Integer.toString(total-remain));
            for (JsonNode element: items.get("stocks")){
                temp = products.get(element.get("barcode").asLong());
                if (temp!=null){
                    temp.setName(String.format("%s (%s)",
                            element.get("article"),
                            element.get("name")));
                }
            }
            remain -= 1000;
        }
        return products;
    }
}
