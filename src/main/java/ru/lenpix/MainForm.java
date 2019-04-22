package ru.lenpix;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;


public class MainForm extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Image leftImage, rightImage;
    private boolean showLeft = true;

    @FXML
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
            throw new UncheckedIOException(e);
        }
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        List<File> selected = new FileChooser().showOpenMultipleDialog(primaryStage);
        if (selected != null && selected.size() == 2) {
            leftImage = new Image(selected.get(0).toURI().toString());
            rightImage = new Image(selected.get(1).toURI().toString());
            repaintCanvas();
        }
    }

    @FXML
    public void changeImageOnCanvas(ActionEvent event) {
        showLeft = !showLeft;
        repaintCanvas();
    }

    private void repaintCanvas() {
        drawImageOnCanvas((showLeft ? leftImage : rightImage));
    }

    private void drawImageOnCanvas(Image image) {
        canvas.setWidth(image.getWidth());
        canvas.setHeight(image.getHeight());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
    }
}