package org.example.orderbook;

/*
public class UpperBound {
    static int upper_bound(Order arr[], double key)
    {
        int mid, N = arr.length;
        int low = 0;
        int high = N;
        while (low < high && low != N) {
            // Find the index of the middle element
            mid = low + (high - low) / 2;
            if (key <= arr[mid].shares) {
                low = mid + 1;
            }
            else {
                high = mid;
            }
        }
        if (low == N ) {
            return N;
        }
        return low;
    }
}
*/