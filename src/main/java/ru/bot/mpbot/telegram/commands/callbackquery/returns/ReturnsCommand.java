package ru.bot.mpbot.telegram.commands.callbackquery.returns;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.requests.RequestDecorator;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.requests.ozon.AnalyticsOzonRequest;
import ru.bot.mpbot.requests.wb.OrderWbRequest;
import ru.bot.mpbot.requests.wb.StockWBRequest;
import ru.bot.mpbot.telegram.constants.MessageConst;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ReturnsCommand extends BotCommand {

    private Logger LOGGER = LoggerFactory.getLogger(ReturnsCommand.class);

    private final boolean isOzn;
    private final ReturnPeriod period;
    private final LocalDate from;
    private final LocalDate to;
    private final Client client;

    public ReturnsCommand(boolean isOzn, ReturnPeriod period, Long chatId) {
        this.isOzn = isOzn;
        this.period = period;
        ClientService clientService = SpringContext.getBean(ClientService.class);
        this.client  = clientService.getClientByTgId(chatId);
        to = LocalDate.now();
        if (period==ReturnPeriod.TWO_WEEKS){
            from = to.minusWeeks(2);
        } else if (period==ReturnPeriod.ONE_MONTH){
            from = to.minusMonths(1);
        } else if (period==ReturnPeriod.THREE_MONTHS){
            from = to.minusMonths(3);
        } else {
            throw new IllegalArgumentException("Wrong period");
        }
    }

    @Override
    public void execute() throws IOException {
        List<Product> products;
        if (isOzn){
            products = executeOzn();
        } else {
            products = executeWB();
        }
        if (products == null){
            return;
        }
        if (products.isEmpty()){
            this.answer=new SendMessage(client.getTgId().toString(),
                    String.format(MessageConst.RETURN_TOP_EMPTY.getMessage(),
                            from.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            isOzn?"Ozon":"WB"
                    ));
        } else {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return o2.getReturns() - o1.getReturns();
                }
            });
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(MessageConst.RETURN_TOP_TEMPLATE.getMessage(),
                    isOzn?"Ozon":"WB",
                    from.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            ));
            int n = Math.min(products.size(), 10);
            for (int i=0; i<n;++i){
                builder.append(i+1).append(") ");
                builder.append("*").append(products.get(i).getReturns()).append("* шт. — ");
                if (products.get(i).getName()!=null) {
                    builder.append(products.get(i).getName()).append("\n");
                } else {
                    builder.append("Имя товара не найдено\n");
                }
            }
            this.answer= new SendMessage(client.getTgId().toString(),
                    builder.toString());
        }
        super.execute();
    }

    public List<Product> executeWB() throws IOException {
        this.answer = new SendMessage(client.getTgId().toString(), "WB: from:"+from+" to: "+to);

        JsonNode items = new RequestDecorator(new OrderWbRequest(
            from,
            to,
            2,
            client.getWbKey()))
                .execute("0");
        HashMap<Long, Product> products = new HashMap();
        for (JsonNode element: items.get("orders")){
            Long id = element.get("barcode").asLong();
            if (element.get("userStatus").asInt()==3) {
                if (products.containsKey(id)) {
                    products.get(id).addReturn(1);
                } else {
                    products.put(id, new Product(
                            id,
                            null,
                            1
                    ));
                }
            }
        }
        RequestDecorator request = new RequestDecorator(new StockWBRequest(client.getWbKey()));
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
        List<Product> productList = new ArrayList<>(products.values());
        return productList;

    }
    public List<Product> executeOzn() throws IOException {

        RequestDecorator request = new RequestDecorator(
                new AnalyticsOzonRequest(
                        LocalDate.now().minusMonths(1),
                        LocalDate.now(),
                        client.getOznKey(),
                        client.getOznId(),
                        "returns"));
        JsonNode node = request.execute("0").get("result").get("data");
        List<Product> products = new ArrayList<>();
        for (JsonNode element: node){
            products.add(new Product(
                    element.get("dimensions").get("id").asLong(),
                    element.get("dimensions").get("name").asText(),
                    element.get("metrics").get(0).asInt()
            ));
        }
        return products;


    }
}
