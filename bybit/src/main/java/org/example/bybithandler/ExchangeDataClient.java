package org.example.bybithandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import lombok.SneakyThrows;
import org.example.orderbook.OrderBook;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public abstract class ExchangeDataClient {
    protected CountDownLatch latch = new CountDownLatch(1);
    protected Session session;
    protected static ObjectMapper jsonMapper = new ObjectMapper();
    protected String security;
    protected String exchange;
    protected OrderBook orderbook;
    long latestSequenceNumber;

    public ExchangeDataClient(OrderBook orderbook, String security, String exchange) {
        this.orderbook = orderbook;
        this.security = security;
        this.exchange = exchange;
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
        this.session = session;
    }
    @OnMessage
    @SneakyThrows
    public void onMessage(Session session, String message) {
        this.session = session;
        System.out.println("Message: " + message);
        try {
            updateOrderbook(jsonMapper.readTree(message));
            System.out.println(orderbook.bids);
            System.out.println(orderbook.asks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract protected void updateOrderbook(JsonNode message);

    abstract public void subscribe();
}
