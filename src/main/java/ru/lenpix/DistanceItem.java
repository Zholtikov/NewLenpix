package ru.lenpix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DistanceItem implements IPaintable {
    private final int x;
    private final int y;
    private final double distance;

    public DistanceItem(int x, int y, double distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    @Override
    public void paintOnPrimaryCanvas(GraphicsContext gc) {
        // TODO(vlad) избавиться от констант, подправить координаты
        gc.setFill(Color.RED);
        gc.fillOval(x - 4, y - 4, 8.0, 8.0);
        gc.setFont(Font.font(15));
        gc.fillText(Double.toString(distance), x - 10, y - 20);
    }

    @Override
    public void paintOnSecondaryCanvas(GraphicsContext gc, int dx, int dy) {
        gc.setFill(Color.RED);
        gc.fillOval(x - 4 - dx, y - 4 - dy, 8.0, 8.0);
        gc.setFont(Font.font(10));
        gc.fillText(Double.toString(distance), x - 10 - dx, y - 20 - dy);

    }
}
