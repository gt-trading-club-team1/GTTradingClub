package org.example.bybithandler;

import java.net.URI;
import jakarta.websocket.*;

@ClientEndpoint
public class WSClient  {
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
        this.session = session;
    }
    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Received message: " + message);
        this.session = session;
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            WSClient client = new WSClient();
            container.connectToServer(client, new URI("wss://stream.bybit.com/v5/public/linear"));
            String orderbookSubscribe = "{\n" +
                    "  \"req_id\": \"test\",\n" +
                    "  \"op\": \"subscribe\",\n" +
                    "  \"args\": [\n" +
                    "    \"orderbook.50.BTCUSDT\",\n" +
                    "    \"orderbook.50.ETHUSDT\"\n" +
                    "  ]\n" +
                    "}";
            client.sendMessage(orderbookSubscribe);
            while (true) Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
