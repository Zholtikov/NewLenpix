package ru.lenpix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DistanceItem implements IPaintable {
    private final int x;
    private final int y;
    private final int dx;
    private final int dy;
    private final double distance;

    public DistanceItem(int x, int y, double distance, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.distance = distance;
    }

    @Override
    public void paint(GraphicsContext gc, boolean right) {
        gc.setFill(Color.RED);
        if (right){
            gc.fillText(Double.toString(distance), x - 10 - dx, y - 20);
            gc.fillOval(x - 4 - dx, y - 4, 8.0, 8.0);
        } else {
            gc.fillText(Double.toString(distance), x - 10, y - 20);
            gc.fillOval(x - 4, y - 4, 8.0, 8.0);
        }
    }
}
