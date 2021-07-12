package com.tsystems.javaschool.tasks.pyramid;

import java.util.Collections;
import java.util.List;

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

        // check is empty
        if (inputNumbers.isEmpty()) {
            throw new CannotBuildPyramidException();
        }

        // check correct numbers of elements
        int height = 0;
        int width = 0;
        int elements = 0;
        while (elements < inputNumbers.size()) {
            height++; // add row
            if (height == 1) // if row is first?
                width++; // add only one column
            else
                width += 2; // else add two column
            elements = elements + height;
        }
        if (inputNumbers.size() != elements) {
            throw new CannotBuildPyramidException();
        }

        System.out.printf("height = %d, weight = %d, elements = %d", height, width, elements);

        // sort elements
        Collections.sort(inputNumbers);

        // result array
        int[][] result = new int[height][width];


        return result;
    }


}
