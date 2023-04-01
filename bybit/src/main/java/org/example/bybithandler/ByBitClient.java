package org.example.bybithandler;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.websocket.ClientEndpoint;
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
        parseMessage(message);
        if (success) {
            TreeMap<Double, Double> bids = orderbook.bids, asks = orderbook.asks;
            if (jsonMapper.convertValue(message.get("type"), String.class).equals("snapshot")) {
                bids.clear();
                asks.clear();
            }
            for (List order : apiBids) {
                if (Double.parseDouble((String) order.get(1)) == 0)
                    bids.remove(Double.parseDouble((String) order.get(0)));
                bids.put(Double.parseDouble((String) order.get(0)), Double.parseDouble((String) order.get(1)));
            }
            for (List order : apiAsks) {
                if (Double.parseDouble((String) order.get(1)) == 0)
                    asks.remove(Double.parseDouble((String) order.get(0)));
                asks.put(Double.parseDouble((String) order.get(0)), Double.parseDouble((String) order.get(1)));
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


