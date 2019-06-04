package ru.lenpix.algo;

public class ImageOffsetNCCMatrix {
    private int maxDX, maxDY;
    private DoubleMatrix nccMatrix;

    public ImageOffsetNCCMatrix(DoubleMatrix nccMatrix){
        this.nccMatrix = nccMatrix;

        findMaxOffset(nccMatrix);
    }

    private void findMaxOffset(DoubleMatrix nccMatrix) {
        int mI = 0, mJ = 0;
        for (int i = 0; i < nccMatrix.getWidth(); i++) {
            for (int j = 0; j < nccMatrix.getHeight(); j++) {
                if (nccMatrix.get(mI, mJ) < nccMatrix.get(i, j)) {
                    mI = i;
                    mJ = j;
                }
            }
        }
        maxDX = mI;
        maxDY = mJ;
    }

    public double get(int i, int j){
        return nccMatrix.get(i, j);
    }

    public int getMaxDX(){
        return maxDX;
    }

    public int getMaxDY(){
        return maxDY;
    }
}
