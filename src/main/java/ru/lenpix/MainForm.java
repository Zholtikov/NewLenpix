package ru.lenpix;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.lenpix.algo.DoubleMatrix;
import ru.lenpix.algo.ImageOffsetNCCMatrixBuilder;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;


public class MainForm extends Application {

    public static void main(String[] args) {
        launch(args);
    }



    private int dx = 0, dy = 0;
    private Image leftImage, rightImage;
    private boolean overlayWithRightImage = false;

    @FXML
    private Stage primaryStage;

    @FXML
    private Canvas canvas;

    @FXML
    private Label coordinatesInfoLabel;

    @FXML
    public CheckBox overlayWithRightImageCheckbox;

    @FXML
    private Label displacementStatusLabel;

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

    /**
     * Вызывается, когда все объекты формы сконструированы.
     */
    public void initialize() {
        updateControllersThatRequireImages();
    }

    @FXML
    private void openFilesButtonHandler(ActionEvent event) {
        List<File> selected = new FileChooser().showOpenMultipleDialog(primaryStage);
        if (selected != null && selected.size() == 2) {
            Image leftImage = new Image(selected.get(0).toURI().toString());
            Image rightImage = new Image(selected.get(1).toURI().toString());

            /*// Для работы корректной работы нам нужны только изображения одного размера
            if (leftImage.getWidth() == rightImage.getWidth() && leftImage.getHeight() == rightImage.getHeight()) {
                return;
            }*/

            this.leftImage = leftImage;
            this.rightImage = rightImage;
            repaintCanvas();

            updateControllersThatRequireImages();
        }
    }

    @FXML
    public void overlayWithRightImageCheckboxHandler(ActionEvent event) {
        overlayWithRightImage = !overlayWithRightImage;
        repaintCanvas();

        updateDisplacementStatus();
    }

    private void repaintCanvas() {
        canvas.requestFocus();

        canvas.setWidth(leftImage.getWidth());
        canvas.setHeight(leftImage.getHeight());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawImageOnCanvas(gc, leftImage, 0, 0);
        if (overlayWithRightImage) drawImageOnCanvas(gc, rightImage, dx, dy);
    }

    private void drawImageOnCanvas(GraphicsContext gc, Image image, int dx, int dy) {
        gc.save();
        gc.setGlobalBlendMode(BlendMode.OVERLAY);
        gc.translate(dx, dy);
        gc.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
        gc.restore();
    }

    public void canvasOnMouseClickedHandler(MouseEvent mouseEvent) {
        System.out.println("start");

        double xO = mouseEvent.getX();
        double yO = mouseEvent.getY();

        int squareSize = 100;

        // Вычисляем левый угол квадрата, в который ткнул юзер
        int x = (int) (xO - squareSize / 2);
        int y = (int) (yO - squareSize / 2);

        DoubleMatrix doubleMatrix = new ImageOffsetNCCMatrixBuilder()
                .setLeftImage(leftImage)
                .setRightImage(rightImage)
                .setSquareSize(squareSize)
                .setUpperLeftCornerPoint(new Point2D(x, y))
                .create();

        int mI = 0, mJ = 0;
        for (int i = 0; i < doubleMatrix.getWidth(); i++) {
            for (int j = 0; j < doubleMatrix.getHeight(); j++) {
                if (doubleMatrix.get(mI, mJ) < doubleMatrix.get(i, j)) {
                    mI = i;
                    mJ = j;
                }
            }
        }

        System.out.println(mI + " " + mJ);

        dx = -(mI - doubleMatrix.getWidth() / 2);
        dy = -(mJ - doubleMatrix.getHeight() / 2);
        System.out.println(dx + " " + dy);

        updateDisplacementStatus();
        repaintCanvas();

        System.out.println("finish");
    }

    public void canvasOnMouseMovedHandler(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        coordinatesInfoLabel.setText(x + ":" + y);
    }

    public void canvasOnMouseExitedHandler(MouseEvent mouseEvent) {
        coordinatesInfoLabel.setText("");
    }

    public void onKeyboardKeyPressedAction(KeyEvent keyEvent) {
        if (!overlayWithRightImage)
            return;

        // Left
        if (keyEvent.getCode() == KeyCode.A)
            dx--;
        // Right
        if (keyEvent.getCode() == KeyCode.D)
            dx++;
        // Up
        if (keyEvent.getCode() == KeyCode.W)
            dy--;
        // Down
        if (keyEvent.getCode() == KeyCode.S)
            dy++;

        repaintCanvas();

        updateDisplacementStatus();
    }

    private void updateControllersThatRequireImages() {
        overlayWithRightImageCheckbox.setDisable(!isImagesLoaded());
    }

    private boolean isImagesLoaded() {
        return leftImage != null && rightImage != null;
    }

    private void updateDisplacementStatus() {
        if (overlayWithRightImage) {
            displacementStatusLabel.setText(dx + ":" + dy);
        } else {
            displacementStatusLabel.setText("");
        }

    }
}