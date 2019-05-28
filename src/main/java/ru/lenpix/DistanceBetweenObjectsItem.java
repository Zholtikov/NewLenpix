package ru.lenpix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class DistanceBetweenObjectsItem implements IPaintable {
    private final int x1;
    private final int y1;
    private final double distanceTo1Point;
    private final int x2;
    private final int y2;
    private final double distanceTo2Point;
    private final double distanceBetween;

    public DistanceBetweenObjectsItem(int x1, int y1, double distanceTo1Point, int x2, int y2, double distanceTo2Point, double distanceBetween) {
        this.x1 = x1;
        this.y1 = y1;
        this.distanceTo1Point = distanceTo1Point;
        this.x2 = x2;
        this.y2 = y2;
        this.distanceTo2Point = distanceTo2Point;
        this.distanceBetween = distanceBetween;
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(Color.web("#4bf221"));
        gc.fillText(Double.toString(distanceTo1Point), x1 + 15, y1 + 5 );
        gc.fillOval(x1 - 4, y1 - 4, 8.0, 8.0);

        gc.setFill(Color.web("#4bf221"));
        gc.fillText(Double.toString(distanceTo2Point), x2 + 15, y2 + 5);
        gc.fillOval(x2 - 4, y2 - 4, 8.0, 8.0);

        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.strokeLine(x1, y1, x2, y2);
        gc.setFill(Color.web("#4bf221"));
        if (Math.abs(y2 - y1) < 5){
            gc.fillText(Double.toString(distanceBetween), (x1 + x2) / 2, (y1 + y2) / 2 + 10);
        } else {
            gc.fillText(Double.toString(distanceBetween), (x1 + x2) / 2 + 10, (y1 + y2) / 2);
        }
    }
}
