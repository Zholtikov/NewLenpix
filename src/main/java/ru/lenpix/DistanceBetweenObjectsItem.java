package ru.lenpix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class DistanceBetweenObjectsItem implements IPaintable {
    private final int x1;
    private final int y1;
    private final double distance1;
    private final int x2;
    private final int y2;
    private final double distance2;
    private final double distance;

    public DistanceBetweenObjectsItem(int x1, int y1, double distance1, int x2, int y2, double distance2, double distance) {
        this.x1 = x1;
        this.y1 = y1;
        this.distance1 = distance1;
        this.x2 = x2;
        this.y2 = y2;
        this.distance2 = distance2;
        this.distance = distance;
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(Color.web("#4bf221"));
        gc.fillText(Double.toString(distance1), x1 - 10, y1 - 20);
        gc.fillOval(x1 - 4, y1 - 4, 8.0, 8.0);

        gc.setFill(Color.web("#4bf221"));
        gc.fillText(Double.toString(distance2), x2 - 10, y2 - 20);
        gc.fillOval(x2 - 4, y2 - 4, 8.0, 8.0);

        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.strokeLine(x1, y1, x2, y2);
        gc.setFill(Color.web("#4bf221"));
        if (Math.abs(y2 - y1) < 5){
            gc.fillText(Double.toString(distance), (x1 + x2) / 2, (y1 + y2) / 2 + 10);
        } else {
            gc.fillText(Double.toString(distance), (x1 + x2) / 2 + 10, (y1 + y2) / 2);
        }
    }
}
