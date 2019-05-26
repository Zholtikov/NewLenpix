package ru.lenpix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DistanceItem implements IPaintable{
    private final int x;
    private final int y;
    private final double distance;

    public DistanceItem(int x, int y, double distance){
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(Color.web("#4bf221"));
        gc.fillText(Double.toString(distance), x - 10, y - 20);
        gc.fillOval(x-3, y-3, 6.0, 6.0);
    }
}
