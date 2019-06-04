package ru.lenpix.algo;

public class DoubleSquareMap {
    private final int minX, maxX;
    private final int minY, maxY;
    private final DoubleMatrix mat;

    public DoubleSquareMap(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        this.mat = DoubleMatrix.createNew(Math.abs(minX) + Math.abs(maxX) + 1, Math.abs(minY) + Math.abs(maxY) + 1);
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public double get(int x, int y) {
        checkArguments(x, y);
        return mat.get(x - minX, y - minY);
    }

    public void set(int x, int y, double val) {
        checkArguments(x, y);
        mat.set(x - minX, y - minY, val);
    }

    private void checkArguments(int x, int y) {
        if (minX > x || x > maxX || minY > y || y > maxY) {
            throw new IllegalArgumentException(String.format("minX=%s, maxX=%s, minY=%s, maxY=%s, but x=%s, y=%s", minX, maxX, minY, maxY, x, y));
        }
    }
}
