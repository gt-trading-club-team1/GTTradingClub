package org.example.orderbook;

public class Order {
    public double volume;
    public long timestamp;
    public Order(double volume, long timestamp) {
        this.volume = volume;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return volume + "/" + timestamp;
    }
}
