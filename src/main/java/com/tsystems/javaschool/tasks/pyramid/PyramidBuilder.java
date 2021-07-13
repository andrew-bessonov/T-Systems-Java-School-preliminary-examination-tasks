package com.tsystems.javaschool.tasks.pyramid;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minimum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {

        // check is empty and null
        if (inputNumbers.isEmpty() || inputNumbers.contains(null)) {
            throw new CannotBuildPyramidException();
        }

        int row = getRowNumber(inputNumbers);

        // if getRowNumber is uncorrected numbers of elements
        if(row == -1) {
            throw new CannotBuildPyramidException();
        }
        // correct column is 1-1, 2-3, 3-5, 4-7, 5-9, where row-column.
        int column = row * 2 - 1;

        // sort elements
        Collections.sort(inputNumbers);

        int[][] result = new int[row][column]; // result array
        Queue<Integer> queue = new LinkedList<>(inputNumbers); // use Queue to easy remove items
        int startPos = column / 2; // start position

        for (int i = 0; i < result.length; i++) {
            int nextPos = startPos;
            for (int j = 0; j <= i; j++) {
                result[i][nextPos] = queue.remove();
                nextPos += 2;
            }
            startPos--;
        }

        return result;
    }

    private static int getRowNumber(List<Integer> input) {
        double result = (Math.sqrt(1 + 8 * input.size()) - 1) / 2;
        if (result == Math.ceil(result)) {
            return (int) result;
        } else {
            return -1;
        }
    }


}
