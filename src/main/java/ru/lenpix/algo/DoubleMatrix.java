package ru.lenpix.algo;

import java.util.Arrays;

/**
 * Матрица double-ов.
 * <p>Мутабельная.</p>
 */
public class DoubleMatrix {

    public DoubleMatrix createFrom(double[][] mat) {
        return new DoubleMatrix(mat);
    }

    public static DoubleMatrix createNew(int n, int m) {
        return new DoubleMatrix(new double[n][m]);
    }

    private final double[][] val;

    private DoubleMatrix(double[][] val) {
        this.val = val;
    }

    public double get(int n, int m) {
        return val[n][m];
    }

    public void set(int n, int m, double val) {
        this.val[n][m] = val;
    }

    public int getHeight() {
        return val.length;
    }

    public int getWidth() {
        return val[0].length;
    }

    @Override
    public String toString() {
        return "DoubleMatrix{" + Arrays.deepToString(val) + '}';
    }
}
