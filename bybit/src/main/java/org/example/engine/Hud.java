package org.example.engine;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.bybithandler.ByBitClient;
import org.example.orderbook.OrderBook;
import org.springframework.web.socket.TextMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

public class Hud extends Application {
    static TableView<OrderDisplay> orderbookHUD = new TableView<>();
    static HashMap<String, OrderBook> orderbooks = new HashMap<String, OrderBook>();

    static ObservableList<OrderDisplay> hudOrderbookData = FXCollections.observableArrayList();
    /*
    public static void main(String[] args) {
        UpdateOrderbook runnable = new UpdateOrderbook();
        Thread thread = new Thread(runnable);
        thread.start();

        launch(args);
    }
     */
    @Override
    public void start(Stage primaryStage) {
        // Create columns for the table
        TableColumn<OrderDisplay, Double> bidVolumeCol = new TableColumn<>("Bid Volume");
        bidVolumeCol.setCellValueFactory(new PropertyValueFactory<>("BidVolume"));

        TableColumn<OrderDisplay, Double> bidPriceCol = new TableColumn<>("Bid Price");
        bidPriceCol.setCellValueFactory(new PropertyValueFactory<>("BidPrice"));
        /*
        bidPriceCol.setCellFactory(new Callback<>() {
            public TableCell<OrderDisplay, Double> call(TableColumn param) {
                return new TableCell<OrderDisplay, Double>() {
                    @Override
                    public void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setTextFill(Color.RED);
                            setText(item.toString());
                        }
                    }
                };
            }
        });
         */

        TableColumn<OrderDisplay, Double> askPriceCol = new TableColumn<>("Ask Price");
        askPriceCol.setCellValueFactory(new PropertyValueFactory<>("AskPrice"));

        TableColumn<OrderDisplay, Double> askVolumeCol = new TableColumn<>("Ask Volume");
        askVolumeCol.setCellValueFactory(new PropertyValueFactory<>("AskVolume"));

        // Add the data to the table
        orderbookHUD.getColumns().add(bidVolumeCol);
        orderbookHUD.getColumns().add(bidPriceCol);
        orderbookHUD.getColumns().add(askPriceCol);
        orderbookHUD.getColumns().add(askVolumeCol);

        // Set size of data table
        for (int i = 0; i < 20; i++) {
            hudOrderbookData.add(new OrderDisplay());
        }

        // Create a layout and add the table to it
        StackPane root = new StackPane();
        root.getChildren().add(orderbookHUD);

        // Create a scene and add the layout to it
        Scene scene = new Scene(root, 480, 720);

        // Set the title and show the stage
        primaryStage.setTitle("Order Book for BTCUSDT");
        primaryStage.setScene(scene);
        primaryStage.show();

        orderbookHUD.setItems(hudOrderbookData);
    }

    public static void updateOrderbookData(TreeMap<Double, Double> asks, TreeMap<Double, Double> bids) {
        int row = 9;
        for (Map.Entry<Double, Double> order : asks.entrySet()) {
            hudOrderbookData.set(row--, new OrderDisplay(OrderType.ASK, order.getKey(), order.getValue()));
            if (row < 0) break;
        }
        row = 10;
        for (Map.Entry<Double, Double> order : bids.entrySet()) {
            hudOrderbookData.set(row++, new OrderDisplay(OrderType.BID, order.getKey(), order.getValue()));
            if (row >= 20) break;
        }
    }
    /*
    public static class UpdateOrderbook implements Runnable {
        public void run() {
            // Create barrier and set countdown counter to 0
            CountDownLatch doneSignal = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    // Unlock the latch with main thread awaiting for it, will continue and do the clean up
                    doneSignal.countDown();
                }
            });

            ByBitClient sampleClient;
            orderbooks.put("BTCUSDT", new OrderBook());
            try {
                sampleClient = new ByBitClient(orderbooks);
            } catch (Exception e) {
                System.out.println("Couldn't create ByBitClient");
                e.printStackTrace();
                return;
            }
            try {
                sampleClient.getClientSession().sendMessage(new TextMessage("{\"req_id\": \"100000\", \"op\": \"ping\"}"));
                String orderbookSubscribe = "{\n" +
                        "  \"req_id\": \"test\",\n" +
                        "  \"op\": \"subscribe\",\n" +
                        "  \"args\": [\n" +
                        "    \"orderbook.50.BTCUSDT\"\n" +
                        "  ]\n" +
                        "}";
                sampleClient.getClientSession().sendMessage(new TextMessage(orderbookSubscribe));
            } catch (Exception e) {
                System.out.println("Couldn't receive message from ByBitClient session");
                e.printStackTrace();
            }

            // Block in wait state till unlocked by pressing Control-c
            try {
                doneSignal.await();
                sampleClient.getClientSession().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Exiting.");
        }
    }
    */
}