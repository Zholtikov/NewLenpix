package ru.lenpix.algo;

public class ImageOffsetNCCMatrix {
    private int maxDX, maxDY;
    private DoubleSquareMap nccMatrix;

    public ImageOffsetNCCMatrix(DoubleSquareMap nccMatrix) {
        this.nccMatrix = nccMatrix;

        findMaxOffset(nccMatrix);
    }

    private void findMaxOffset(DoubleSquareMap nccMatrix) {
        int mI = 0, mJ = 0;
        for (int i = nccMatrix.getMinX(); i <= nccMatrix.getMaxX(); i++) {
            for (int j = nccMatrix.getMinY(); j <= nccMatrix.getMaxY(); j++) {
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
