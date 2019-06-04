package ru.lenpix;

import javafx.scene.canvas.GraphicsContext;

public interface IPaintable {

    /**
     * Отрисовать на первичном канвасе.
     */
    void paintOnPrimaryCanvas(GraphicsContext gc);

    /**
     * Отрисовать на вторичном канвасе.
     */
    void paintOnSecondaryCanvas(GraphicsContext gc, int dx, int dy);
}
