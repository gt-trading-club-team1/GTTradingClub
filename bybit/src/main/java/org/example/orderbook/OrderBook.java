package org.example.orderbook;

import java.util.*;

public class OrderBook {
    public TreeMap<Double, Order> bids, asks;
    // No longer needed
    double[] askPrices;
    double[] bidPrices;
    SegmentTree askSegment;
    SegmentTree bidSegment;

    public OrderBook() {
        this.bids = new TreeMap<Double, Order>(Collections.reverseOrder());
        this.asks = new TreeMap<Double, Order>();
    }

    public OrderBook(TreeMap<Double, Order> bids, TreeMap<Double, Order> asks) {
        this.bids = bids;
        this.asks = asks;
    }
        /*
        Arrays.sort(this.bids, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        Order t1 = (Order) o1;
                        Order t2 = (Order) o2;
                        if (t1.shares < t2.shares) return 1;
                        if (t1.shares > t2.shares) return -1;
                        return 0;
                    }
                }
            );
        Arrays.sort(this.asks, new Comparator() {
            public int compare(Object o1, Object o2) {
                Order t1 = (Order) o1;
                Order t2 = (Order) o2;
                if (t1.shares < t2.shares) return 1;
                if (t1.shares > t2.shares) return -1;
                return 0;
            }
        });
        askPrices = new double[this.asks.length];
        bidPrices = new double [this.bids.length];
        for (int i = 0; i < askPrices.length; i++) askPrices[i] = this.asks[i].price;
        for (int i = 0; i < bidPrices.length; i++) bidPrices[i] = this.bids[i].price;
        askSegment = new SegmentTree(askPrices, true);
        bidSegment = new SegmentTree(bidPrices, false);
        */
    /*
    double askQuery(double s) {
        int index = UpperBound.upper_bound(asks, s) - 1;
        //System.out.println("Index is " + index);
        return askSegment.RMQ(askPrices.length,  0, index);
    }
    double bidQuery(double s) {
        int index = UpperBound.upper_bound(bids, s) - 1;
        return bidSegment.RMQ(bidPrices.length, 0, index);
    }
    double ask(double s) {
        return askSegment.st[0];
    }
    double bid(double s) {
        return bidSegment.st[0];
    }
    public static void main(String[] args) {
        /*
        Order[] asks = new Order[5];
        asks[0] = new Order(1440.0, 1000);
        asks[1] = new Order(1439.0, 1000);
        asks[2] = new Order(1441.5, 1000);
        asks[3] = new Order(1437.0, 100);
        asks[4] = new Order(1439.0, 1000);
        Order[] bids = new Order[5];
        bids[0] = new Order(1460.0, 1000);
        bids[1] = new Order(1461.0, 1000);
        bids[2] = new Order(1459.0,  1000);
        bids[3] = new Order(1460.0, 1000);
        bids[4] = new Order(1460.0, 1000);
        OrderBook ob = new OrderBook(bids, asks);
        System.out.println("Ask Price " + ob.ask(1000));
        System.out.println("Bid Price " + ob.bid(1000));

    }
*/
}