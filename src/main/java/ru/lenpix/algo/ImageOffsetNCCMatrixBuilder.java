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

    public DoubleMatrix create() {
//        if (left.getWidth() != right.getWidth() || left.getHeight() != right.getHeight()) {
//            throw new IllegalStateException("left and right images have different dimensions");
//        }

        DoubleMatrix leftImageBrightness = brightnessMatrix(this.left);
        DoubleMatrix rightImageBrightness = brightnessMatrix(this.right);

        int x = (int) upperLeftCorner.getX();
        int y = (int) upperLeftCorner.getY();

        NormalizedCrossCorrelation ncc = new NormalizedCrossCorrelation(
                leftImageBrightness,
                rightImageBrightness,
                squareSize,
                x, y);

        int dxMax = squareSize * 2;
        int dyMax = squareSize * 2;
        DoubleMatrix nccMatrix = DoubleMatrix.createNew(dxMax, dyMax);

        int progress = 0;
        for (int dx = 0; dx < nccMatrix.getHeight(); dx++) {
            for (int dy = 0; dy < nccMatrix.getWidth(); dy++) {
                nccMatrix.set(dx, dy, ncc.apply(dx, dy));
                progress += 1;
            }

            System.out.println(dx * nccMatrix.getWidth() + "/" + nccMatrix.getWidth() * nccMatrix.getHeight());
        }

        return nccMatrix;
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
