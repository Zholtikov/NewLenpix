package ru.lenpix.algo;

public class NccInterpolation {
    private final ImageOffsetNCCMatrix matrix;

    private DoubleMatrix interMatrix;
    private int maxNodeDX, maxNodeDY;
    private double maxInter = -2;

    public NccInterpolation(ImageOffsetNCCMatrix matrix) {
        this.matrix = matrix;
        calcInterMarix();
    }


    public double getInterDX() {
        int xPlus = maxNodeDX + 1;
        int xMinus = maxNodeDX - 1;
        double maxPlus = interMatrix.get(maxNodeDX + 1, maxNodeDY);
        double maxMinus = interMatrix.get(maxNodeDX - 1, maxNodeDY);
        double num = (Math.pow(xPlus,2) - Math.pow(maxNodeDX, 2)) * (maxInter - maxMinus) +
                (Math.pow(maxNodeDX,2) - Math.pow(xMinus, 2)) * (maxInter - maxPlus);

        double den = (xPlus -maxNodeDX) * (maxInter - maxMinus) +
                (maxNodeDX - xMinus) * (maxInter - maxPlus);

        return num / (2 * den);
    }

    public double getInterDY() {
        int yPlus = maxNodeDY + 1;
        int yMinus = maxNodeDY - 1;
        double maxPlus = interMatrix.get(maxNodeDY + 1, maxNodeDY);
        double maxMinus = interMatrix.get(maxNodeDY - 1, maxNodeDY);
        double num = (Math.pow(yPlus,2) - Math.pow(maxNodeDY, 2)) * (maxInter - maxMinus) +
                (Math.pow(maxNodeDY,2) - Math.pow(yMinus, 2)) * (maxInter - maxPlus);

        double den = (yPlus -maxNodeDY) * (maxInter - maxMinus) +
                (maxNodeDX - yMinus) * (maxInter - maxPlus);

        return num / (2 * den);
    }

    private void calcInterMarix() {
        int maxDx = matrix.getMaxDX();
        int maxDy = matrix.getMaxDY();

        double max = matrix.get(maxDx, maxDy);
        double maxPlusX = matrix.get(maxDx + 1, maxDy);
        double maxPlusY = matrix.get(maxDx, maxDy + 1);
        double maxPlusXY = matrix.get(maxDx + 1, maxDy + 1);

        int k = 10;
        double h = 1 / k;

        interMatrix = DoubleMatrix.createNew(k, k);

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                double res = (1 - i * h) * (1 - j * h) * max +
                        (1 - j * h) * i * h * maxPlusX +
                        (1 - i * h) * j * h * maxPlusY +
                        i * j * h * h * maxPlusXY;
                interMatrix.set(i, j, res);
                if (res > maxInter) {
                    maxInter = res;
                    maxNodeDX = i;
                    maxNodeDY = j;
                }
            }
        }
    }
}
