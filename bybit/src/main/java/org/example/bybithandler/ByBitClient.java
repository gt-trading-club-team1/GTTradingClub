package org.example.bybithandler;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.websocket.ClientEndpoint;
import org.example.orderbook.Order;
import org.example.orderbook.OrderBook;
import java.util.*;
import java.util.regex.Pattern;

@ClientEndpoint
public class ByBitClient extends ExchangeDataClient {
    protected List<List<Double>> apiBids, apiAsks;
    boolean success = false;
    public ByBitClient(OrderBook orderbook, String security, String exchange) {
        super(orderbook, security, exchange);
    }

    @Override
    protected void updateOrderbook(JsonNode message) {
        long blockTimestamp = jsonMapper.convertValue(message.get("ts"), Long.class);
        parseMessage(message);
        if (success) {
            TreeMap<Double, Order> bids = orderbook.bids, asks = orderbook.asks;
            if (jsonMapper.convertValue(message.get("type"), String.class).equals("snapshot")) {
                bids.clear();
                asks.clear();
            }
            for (List order : apiBids) {
                Double volume = Double.parseDouble((String) order.get(1));
                Double price = Double.parseDouble((String) order.get(0));
                if (volume == 0)
                    bids.remove(price);
                else {
                    if (bids.get(price) == null || bids.get(price).timestamp < blockTimestamp) bids.put(price, new Order(volume, blockTimestamp));
                }
            }
            for (List order : apiAsks) {
                Double volume = Double.parseDouble((String) order.get(1));
                if (volume == 0)
                    asks.remove(Double.parseDouble((String) order.get(0)));
                else {
                    asks.put(Double.parseDouble((String) order.get(0)), new Order(volume, blockTimestamp));
                }
            }
            success = false;
        }
    }

    protected void parseMessage(JsonNode message) {
        try {
            String ticker = jsonMapper.convertValue(message.get("topic"), String.class);
            String[] words = ticker.split(Pattern.quote("."));
            ticker = words[words.length - 1];
            if (ticker.equals(security)) {
                JsonNode orderbookData = message.get("data");
                this.apiBids = jsonMapper.convertValue(orderbookData.get("b"), ArrayList.class);
                this.apiAsks = jsonMapper.convertValue(orderbookData.get("a"), ArrayList.class);
            }
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribe() {
        sendMessage("{\n" +
                "  \"req_id\": \"test\",\n" +
                "  \"op\": \"subscribe\",\n" +
                "  \"args\": [\n" +
                "    \"orderbook.50." + security + "\"\n" +
                "  ]\n" +
                "}");
    }
}


