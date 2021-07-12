package com.tsystems.javaschool.tasks.pyramid;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
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

        int row = 0;
        int column;
        int elements = 0;

        while (elements < inputNumbers.size()) {
            row++;
            elements = elements + row;
        }
        column = row * 2 - 1;

        // check correct numbers of elements
        if (inputNumbers.size() != elements) {
            throw new CannotBuildPyramidException();
        }

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


}
