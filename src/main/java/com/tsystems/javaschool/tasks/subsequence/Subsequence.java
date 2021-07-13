package com.tsystems.javaschool.tasks.subsequence;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List<?> x, List<?> y) {

        if (x == null || y == null) {
            throw new IllegalArgumentException();
        }
        if (x.size() > y.size()) {
            return false;
        }
        if (x.isEmpty() || x.equals(y)) {
            return true;
        }

        // Create two queue for simple remove
        Queue yQueue = new LinkedList<>(y);
        Queue xQueue = new LinkedList<>(x);

        Object xItem = xQueue.remove(); // take first item X
        Object yItem;

        while (!yQueue.isEmpty()) { // while all Y queue
            yItem = yQueue.remove(); // take item Y until Y == X

            if (yItem.equals(xItem)) { // if Equals
                if (xQueue.isEmpty()) { // if X queue is end, all is ok :)
                    return true;
                }
                xItem = xQueue.remove(); // take next X item
            }
        }

        return false; // if we remove all Y queue, and not finished X queue. Result means false.


    }
}
