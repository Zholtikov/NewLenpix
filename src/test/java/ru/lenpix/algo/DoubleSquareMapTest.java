package ru.lenpix.algo;

import org.junit.jupiter.api.Test;

class DoubleSquareMapTest {

    @Test
    void test_bounds() {
        DoubleSquareMap map = new DoubleSquareMap(0, 1, 0, 1);
        map.set(0,1, 1);
        map.set(1,0, 1);
    }
}