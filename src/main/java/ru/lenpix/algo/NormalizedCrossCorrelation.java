package ru.lenpix.algo;

import java.util.function.BiFunction;

import static java.lang.Math.sqrt;

public class NormalizedCrossCorrelation {
    private final DoubleMatrix mat1;
    private final DoubleMatrix mat2;
    private final int n;
    private final int x;
    private final int y;

    public NormalizedCrossCorrelation(DoubleMatrix mat1, DoubleMatrix mat2, int n, int x, int y) {
        this.mat1 = mat1;
        this.mat2 = mat2;
        this.n = n;
        this.x = x;
        this.y = y;
    }

    public double apply(int dx, int dy) {
        double nominator = sum(x, x + n, y, y + n,
                (i, j) -> func(mat1, i, j, 0, 0) * func(mat2, i, j, dx, dy));

        double a = sum(x, x + n, y, y + n,
                (i, j) -> Math.pow(func(mat1, i, j, 0, 0), 2));

        double b = sum(x, x + n, y, y + n,
                (i, j) -> Math.pow(func(mat2, i, j, dx, dy), 2));


        double denominator = sqrt(a * b);

        return nominator / denominator;
    }

    private double func(DoubleMatrix mat, int x, int y, int dx, int dy) {
        return mat.get(x + dx, y + dy) - normI(mat, x + dx, y + dy);
    }

    private double normI(DoubleMatrix mat, int x, int y) {
        double sum = sum(x, x + n, y, y + n, mat::get);

        return sum / (n * n);
    }

    private double sum(int minI, int maxI, int minJ, int maxJ, BiFunction<Integer, Integer, Double> func) {
        double ret = 0;

        for (int i = minI; i < maxI; i++) {
            for (int j = minJ; j < maxJ; j++) {
                ret += func.apply(i, j);
            }
        }

        return ret;
    }
}