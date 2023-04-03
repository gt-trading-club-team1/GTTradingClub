package org.example.engine;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import lombok.SneakyThrows;
import org.example.bybithandler.ByBitClient;
import org.example.orderbook.OrderBook;

import java.net.URI;
import java.util.*;

public class Engine {
    @SneakyThrows
    public static void main(String[] args) {
        try {
            List<ByBitClient> clients = new ArrayList<ByBitClient>(1);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            clients.add(new ByBitClient(new OrderBook(), "BTCUSDT", "bybit"));
            //clients.add(new ByBitClient(new OrderBook(), "ETHUSDT", "bybit"));
            for (var client : clients) {
                container.connectToServer(client, new URI("wss://stream.bybit.com/v5/public/linear"));
                client.subscribe();
            }
            while(true) Thread.sleep(100000);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}