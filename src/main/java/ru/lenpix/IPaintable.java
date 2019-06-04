package ru.lenpix;

import javafx.scene.canvas.GraphicsContext;

public interface IPaintable {
    void paint(GraphicsContext gc, boolean right);
}
