package ru.lenpix.algo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class ImageOffsetNCCMatrixBuilder {
    private Image left;
    private Image right;
    private Point2D upperLeftCorner;
    private int squareSize;

    public ImageOffsetNCCMatrixBuilder setLeftImage(Image image) {
        this.left = image;
        return this;
    }

    public ImageOffsetNCCMatrixBuilder setRightImage(Image image) {
        this.right = image;
        return this;
    }

    public ImageOffsetNCCMatrixBuilder setSquareSize(int squareSize) {
        this.squareSize = squareSize;
        return this;
    }

    public ImageOffsetNCCMatrixBuilder setUpperLeftCornerPoint(Point2D point) {
        this.upperLeftCorner = point;
        return this;
    }

    public ImageOffsetNCCMatrix create() {
        DoubleMatrix leftImageBrightness = brightnessMatrix(this.left);
        DoubleMatrix rightImageBrightness = brightnessMatrix(this.right);

        int x = (int) upperLeftCorner.getX();
        int y = (int) upperLeftCorner.getY();

        NormalizedCrossCorrelation ncc = new NormalizedCrossCorrelation(
                leftImageBrightness,
                rightImageBrightness,
                squareSize,
                x, y);

        int minWidth = Math.min(leftImageBrightness.getWidth(), rightImageBrightness.getWidth());
        int minHeight = Math.min(leftImageBrightness.getHeight(), rightImageBrightness.getHeight());

        int dxMax = (minWidth - x - squareSize) / 3;
        int dxMin = (x) / 3;
        int dyMax = (minHeight - y - squareSize) / 3;
        int dyMin = (y) / 3;
        DoubleSquareMap nccMatrix = new DoubleSquareMap(-dxMin, dxMax, -dyMin, dyMax);

        for (int dx = nccMatrix.getMinX(); dx <= nccMatrix.getMaxX(); dx++) {
            for (int dy = nccMatrix.getMinY(); dy <= nccMatrix.getMaxY(); dy++) {
                nccMatrix.set(dx, dy, ncc.apply(dx, dy));
            }
        }

        return new ImageOffsetNCCMatrix(nccMatrix);
    }

    private DoubleMatrix brightnessMatrix(Image image) {
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();

        DoubleMatrix brightnessMatrix = DoubleMatrix.createNew(w, h);

        PixelReader pixelReader = image.getPixelReader();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                brightnessMatrix.set(i, j, pixelReader.getColor(i, j).grayscale().getRed());
            }
        }

        return brightnessMatrix;
    }
}
