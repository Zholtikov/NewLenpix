package ru.lenpix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ObjectSizeItem implements IPaintable {
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final double distance;
    private final double realWidth;
    private final double realHeight;


    public ObjectSizeItem(int x1, int y1, int x2, int y2,
                          double distance,
                          double realWidth,
                          double realHeight) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.distance = distance;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
    }

    @Override
    public void paintOnPrimaryCanvas(GraphicsContext gc) {
        // TODO(vlad) избавиться от констант

        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.strokeLine(x1, y1, x2, y1);
        gc.strokeLine(x2, y1, x2, y2);
        gc.strokeLine(x2, y2, x1, y2);
        gc.strokeLine(x1, y2, x1, y1);
        gc.fillOval((x2 + x1) / 2 - 4, (y1 + y2) / 2 - 4, 8.0, 8.0);
        gc.setFill(Color.web("#4bf221"));
        gc.fillText(Double.toString(realWidth), Math.min(x1, x2), Math.min(y1, y2) - 5);
        gc.fillText(Double.toString(realHeight), Math.max(x1, x2) + 5, (y1 + y2) / 2);
        gc.fillText(Double.toString(distance), (x2 + x1) / 2 - 10, (y1 + y2) / 2 - 10);
    }


    @Override
    public void paintOnSecondaryCanvas(GraphicsContext gc, int dx, int dy) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.strokeLine(x1 - dx, y1, x2 - dx, y1);
        gc.strokeLine(x2 - dx, y1, x2 - dx, y2);
        gc.strokeLine(x2 - dx, y2, x1 - dx, y2);
        gc.strokeLine(x1 - dx, y2, x1 - dx, y1);
        gc.fillOval(((x2 - dx + x1 - dx) / 2) - 4, (y1 + y2) / 2 - 4, 8.0, 8.0);
        gc.setFill(Color.web("#4bf221"));
        gc.fillText(Double.toString(realWidth), Math.min(x1, x2) - dx, Math.min(y1, y2) - 5);
        gc.fillText(Double.toString(realHeight), Math.max(x1, x2) + 5 - dx, (y1 + y2) / 2);
        gc.fillText(Double.toString(distance), (x2 + x1) / 2 - (10 + dx), (y1 + y2) / 2 - 10);
    }
}
