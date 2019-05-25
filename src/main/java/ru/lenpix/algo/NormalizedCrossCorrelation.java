package ru.lenpix.algo;

import java.util.function.BiFunction;

import static java.lang.Math.sqrt;

public class NormalizedCrossCorrelation {
    private final DoubleMatrix mat1;
    private final DoubleMatrix mat2;
    private final int n;
    private final int x;
    private final int y;
    private double firstIsumm;
    private double firstISqSumm;
    private boolean inited = false;

    public NormalizedCrossCorrelation(DoubleMatrix mat1, DoubleMatrix mat2, int n, int x, int y) {
        this.mat1 = mat1;
        this.mat2 = mat2;
        this.n = n;
        this.x = x;
        this.y = y;
    }

    // считаем сумму I1 с квадратами (она не меняется)
    private double countSqSumFirst(DoubleMatrix mat1, int n, int x, int y, double firstSum) {
        double resSq = 0.0;
        for (int i = x; i < x + n; i++) {
            for (int j = y; j < y + n; j++) {
                resSq += Math.pow(mat1.get(i, j), 2) - Math.pow(firstSum, 2) / (n * n);
            }
        }
        return resSq;
    }

    // считаем сумму I1 (она не меняется)
    private double countSumOfFirst(DoubleMatrix mat1, int n, int x, int y) {
        double res = 0.0;
        for (int i = x; i < x + n; i++) {
            for (int j = y; j < y + n; j++) {
                res += mat1.get(i, j);
            }
        }
        return res;
    }

    public double apply(int dx, int dy) {
        if (!inited) {
            firstIsumm = countSumOfFirst(mat1, n, x, y);
            firstISqSumm = countSqSumFirst(mat1, n, x, y, firstIsumm);
            inited = true;
        }

        // считаем сумму I2
        double secondIsum = 0.0;
        for (int i = x; i < x + n; i++) {
            for (int j = y; j < y + n; j++) {
                secondIsum += mat2.get(i + dx, j + dy);
            }
        }

        // считаем сумму I2 с квадратами
        double secondISqSum = 0.0;
        for (int i = x; i < x + n; i++) {
            for (int j = y; j < y + n; j++) {
                secondISqSum += Math.pow(mat2.get(i + dx, j + dy), 2) - Math.pow(secondIsum, 2) / (n * n);
            }
        }

        // считаем знаменатель
        double det = Math.sqrt(firstISqSumm * secondISqSum);

        // считаем числитель
        double num = 0.0;
        for (int i = x; i < x + n; i++) {
            for (int j = y; j < y + n; j++) {
                num += mat1.get(i, j) * mat2.get(i + dx, j + dy) - firstIsumm * secondIsum / (n * n);
            }
        }

        return num / det;
    }
}