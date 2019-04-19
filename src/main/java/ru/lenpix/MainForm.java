package ru.lenpix;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Пример javafx-окошечка.
 */
public class MainForm extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Image leftImage, rightImage;
    private boolean showLeft = true;
    private Stage primaryStage;

    @FXML
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("main.fxml"));

            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        List<File> files = new FileChooser().showOpenMultipleDialog(primaryStage);
        if (files != null && files.size() == 2) {
            leftImage = new Image(files.get(0).toURI().toString());
            rightImage = new Image(files.get(1).toURI().toString());
            repaintCanvas();
        }
    }

    @FXML
    public void changeImageOnCanvas(ActionEvent event) {
        showLeft = !showLeft;
        repaintCanvas();
    }

    private void repaintCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (leftImage  == null || rightImage == null) {
            gc.setFill(Color.BLACK);
            gc.fill();
        }

        drawImageOnCanvas((showLeft ? leftImage : rightImage));
    }

    private void drawImageOnCanvas(Image image) {
        canvas.setWidth(image.getWidth());
        canvas.setHeight(image.getHeight());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
    }
}