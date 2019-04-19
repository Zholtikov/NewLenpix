package ru.lenpix;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Пример javafx-окошечка.
 */
public class MainForm extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FlowPane root = new FlowPane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(300);
        primaryStage.setHeight(250);
        primaryStage.show();
    }
}
