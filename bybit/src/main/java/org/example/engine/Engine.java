package org.example.engine;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import lombok.SneakyThrows;
import org.example.bybithandler.ByBitClient;
import org.example.orderbook.OrderBook;

import java.net.URI;

public class Engine {
    private static OrderBook orderbook = new OrderBook();
    @SneakyThrows
    public static void main(String[] args) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            ByBitClient client = new ByBitClient(orderbook, "BTCUSDT", "bybit");
            container.connectToServer(client, new URI("wss://stream.bybit.com/v5/public/linear"));
            client.subscribe();

            while(true) Thread.sleep(100000);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}