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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.lenpix.algo.ImageOffsetNCCMatrix;
import ru.lenpix.algo.ImageOffsetNCCMatrixBuilder;
import ru.lenpix.algo.NCCInterpolation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;


public class MainForm extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    private int dx = 0, dy = 0;
    private Image leftImage, rightImage;
    private boolean overlayWithRightImage = false;
    private ModeType mode = ModeType.NONE;
    private List<IPaintable> items = new ArrayList<>();

    private Point2D point1Helper = new Point2D(0, 0);
    private double distancePoint1Helper;

    @FXML
    private Stage primaryStage;

    @FXML
    private Canvas canvas;

    @FXML
    private Canvas canvasRight;

    @FXML
    private Label coordinatesInfoLabel;

    @FXML
    public CheckBox overlayWithRightImageCheckbox;

    @FXML
    private Label displacementStatusLabel;

    /**
     * фокус
     */
    @FXML
    private TextField focusField;

    /**
     * дистанция между камерами в cантиметрах
     */
    @FXML
    private TextField baseField;

    /**
     * линейный размер матрицы в миллиметрах по одной из сторон ( ширине )
     */
    @FXML
    private TextField photoMatrixWidthField;

    /**
     * линейный размер матрицы в миллиметрах по одной из сторон ( высоте )
     */
    @FXML
    private TextField photoMatrixHeightField;

    /**
     * Текст отчёта
     */
    @FXML
    private TextArea reportField;

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
    private void openFilesLeftButtonHandler(ActionEvent event) {
        List<File> selected = new FileChooser().showOpenMultipleDialog(primaryStage);
        if (selected != null && selected.size() == 1) {

            /*// Для работы корректной работы нам нужны только изображения одного размера
            if (leftImage.getWidth() == rightImage.getWidth() && leftImage.getHeight() == rightImage.getHeight()) {
                return;
            }*/

            this.leftImage = new Image(selected.get(0).toURI().toString());
            items.clear();
            reportField.clear();
            repaintPrimaryCanvas();


        }
    }

    @FXML
    private void openFilesRightButtonHandler(ActionEvent event) {
        List<File> selected = new FileChooser().showOpenMultipleDialog(primaryStage);
        if (selected != null && selected.size() == 1) {

            /*// Для работы корректной работы нам нужны только изображения одного размера
            if (leftImage.getWidth() == rightImage.getWidth() && leftImage.getHeight() == rightImage.getHeight()) {
                return;
            }*/

            this.rightImage = new Image(selected.get(0).toURI().toString());
            items.clear();
            reportField.clear();
            repaintSecondaryCanvas();
            updateControllersThatRequireImages();
        }
    }

    @FXML
    public void overlayWithRightImageCheckboxHandler(ActionEvent event) {
        overlayWithRightImage = !overlayWithRightImage;
        repaintPrimaryCanvas();
        repaintSecondaryCanvas();
        updateDisplacementStatus();
    }

    private void repaintPrimaryCanvas() {
        canvas.requestFocus();

        canvas.setWidth(leftImage.getWidth());
        canvas.setHeight(leftImage.getHeight());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawImageOnCanvas(gc, leftImage, 0, 0);
        if (overlayWithRightImage) drawImageOnCanvas(gc, rightImage, dx, dy);
        for (IPaintable item : items) {
            item.paintOnPrimaryCanvas(gc);
        }
    }

    private void repaintSecondaryCanvas() {
        canvasRight.requestFocus();

        canvasRight.setWidth(rightImage.getWidth());
        canvasRight.setHeight(rightImage.getHeight());

        GraphicsContext gc = canvasRight.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasRight.getWidth(), canvasRight.getHeight());
        drawImageOnRightCanvas(gc, rightImage, 0, 0);
        for (IPaintable item : items) {
            item.paintOnSecondaryCanvas(gc, dx, dy);
        }
    }

    private void drawImageOnCanvas(GraphicsContext gc, Image image, int dx, int dy) {
        gc.save();
        gc.setGlobalBlendMode(BlendMode.OVERLAY);
        gc.translate(dx, dy);
        gc.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
        gc.restore();
    }

    private void drawImageOnRightCanvas(GraphicsContext gc, Image image, int dx, int dy) {
        gc.save();
        gc.setGlobalBlendMode(BlendMode.OVERLAY);
        gc.translate(dx, dy);
        gc.drawImage(image, 0, 0, canvasRight.getWidth(), canvasRight.getHeight());
        gc.restore();
    }

    public void canvasOnMouseClickedHandler(MouseEvent mouseEvent) {
        double xO = mouseEvent.getX();
        double yO = mouseEvent.getY();

        // дистанция между камерами в мм
        double l = Double.parseDouble(baseField.getText());

        // фокус в мм
        double f = Double.parseDouble(focusField.getText());

        //линейный размер матрицы в миллиметрах по одной из сторон ( ширине )
        double width = Double.parseDouble(photoMatrixWidthField.getText());

        // линейный размер матрицы в миллиметрах по одной из сторон ( ширине )
        double height = Double.parseDouble(photoMatrixHeightField.getText());

        //ширина пикселя
        double pixelWSize = width / (int) rightImage.getWidth();

        //высота пикселя
        double pixelHSize = height / (int) rightImage.getHeight();

        //главная точка (центр)
        double xCenter = rightImage.getWidth() / 2;
        double yCenter = rightImage.getHeight() / 2;

        if (mode == ModeType.DISTANCE) {
            calcDistanceMode(xO, yO, pixelWSize, l, f);
            mode = ModeType.NONE;
        }

        if (mode == ModeType.OBJECTS_DISTANCE_PART_TWO) {
            calcObjectsDistancePartTwo(xO, yO, pixelWSize, pixelHSize, l, f, xCenter, yCenter);
            mode = ModeType.NONE;
        }

        if (mode == ModeType.OBJECTS_DISTANCE_PART_ONE) {
            calcObjectsDistancePartOne(xO, yO, pixelWSize, l, f);
            mode = ModeType.OBJECTS_DISTANCE_PART_TWO;
        }

        if (mode == ModeType.OBJECT_SIZE_PART_TWO) {
            calcSizeObjectMode(xO, yO, pixelWSize, pixelHSize, l, f);
            mode = ModeType.NONE;
        }

        if (mode == ModeType.OBJECT_SIZE_PART_ONE) {
            point1Helper = new Point2D(xO, yO);
            mode = ModeType.OBJECT_SIZE_PART_TWO;
        }

        updateDisplacementStatus();
        repaintPrimaryCanvas();
        repaintSecondaryCanvas();
    }

    private void calcDisplacement(double userX, double userY) {
        int squareSize = 100;

        // Вычисляем левый угол квадрата, в который ткнул юзер
        int x = (int) (userX - squareSize / 2);
        int y = (int) (userY - squareSize / 2);

        if (x < 0 || y < 0 || x + squareSize >= leftImage.getWidth() || y + squareSize >= leftImage.getHeight())
            return;

        ImageOffsetNCCMatrix matrix = new ImageOffsetNCCMatrixBuilder()
                .setLeftImage(leftImage)
                .setRightImage(rightImage)
                .setSquareSize(squareSize)
                .setUpperLeftCornerPoint(new Point2D(x, y))
                .create();

        dx = -matrix.getMaxDX();
        dy = -matrix.getMaxDY();

        NCCInterpolation interpolation = new NCCInterpolation(matrix);
        dx += interpolation.getInterDX();
        dy += interpolation.getInterDY();
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

        repaintPrimaryCanvas();

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

    private void addItem(IPaintable item) {
        items.add(item);

        repaintPrimaryCanvas();
        repaintSecondaryCanvas();
    }


    public void distanceModeButtonHandler(ActionEvent actionEvent) {
        mode = ModeType.DISTANCE;
    }

    public void distanceBetweenObjectsModeButtonHandler(ActionEvent actionEvent) {
        mode = ModeType.OBJECTS_DISTANCE_PART_ONE;
    }

    public void realSizeObjectModeButtonHandler(ActionEvent actionEvent) {
        mode = ModeType.OBJECT_SIZE_PART_ONE;
    }

    public void reportButtonHandler(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(reportField.getText());
            }
        }
    }

    public void CancelButtonHandler(ActionEvent actionEvent) {
        items.remove(items.size()-1);

        repaintPrimaryCanvas();
        repaintSecondaryCanvas();

        String[] array = reportField.getText().split(System.lineSeparator());
        StringBuilder textToSet = new StringBuilder();
        for(int i=0; i<array.length-1; i++){
            textToSet.append(array[i]).append("\n");
        }
        reportField.setText(textToSet.toString());
    }

    private enum ModeType {
        NONE,
        DISTANCE,
        OBJECTS_DISTANCE_PART_ONE,
        OBJECTS_DISTANCE_PART_TWO,
        OBJECT_SIZE_PART_ONE,
        OBJECT_SIZE_PART_TWO
    }

    private void calcDistanceMode(double xO, double yO, double pixelWSize, double l, double f) {
        calcDisplacement(xO, yO);
        double deltaX = Math.abs(dx) * pixelWSize / 1000;
        double distance = ((l / 1000) * (f / 1000) / deltaX);

        addItem(new DistanceItem((int) xO, (int) yO, distance));
        reportField.appendText(items.size() + ") Дистанция до объекта N" + " равняется " + distance + " метров" + System.lineSeparator());

    }

    private void calcObjectsDistancePartOne(double xO, double yO, double pixelWSize, double l, double f) {
        calcDisplacement(xO, yO);
        point1Helper = new Point2D(xO, yO);
        double deltaX = Math.abs(dx) * pixelWSize / 1000;
        distancePoint1Helper = (l / 1000 * f / 1000 / deltaX);
    }

    private void calcObjectsDistancePartTwo(double xO, double yO, double pixelWSize, double pixelHSize, double l, double f, double xCenter, double yCenter) {
        calcDisplacement(xO, yO);
        double deltaX = Math.abs(dx) * pixelWSize / 1000;
        double distance2Point = (l / 1000 * f / 1000 / deltaX);
        double newX1 = distancePoint1Helper * (point1Helper.getX() - xCenter) * pixelWSize / f;
        double newX2 = distance2Point * (xO - xCenter) * pixelWSize / f;
        double newY1 = distancePoint1Helper * (point1Helper.getY() - yCenter) * pixelHSize / f;
        double newY2 = distance2Point * (yO - yCenter) * pixelHSize / f;
        double result = Math.sqrt(
                Math.pow(newX2 - newX1, 2) +
                        Math.pow(newY2 - newY1, 2) +
                        Math.pow(distance2Point - distancePoint1Helper, 2));

        addItem(new DistanceBetweenObjectsItem(
                (int) point1Helper.getX(), (int) point1Helper.getY(), distancePoint1Helper,
                (int) xO, (int) yO, distance2Point, result));
        reportField.appendText(items.size() + ") Дистанция между объектом A и объектом B равняется " + result + " метров" + System.lineSeparator()
                + "  Дистанция до объекта A" + " равняется " + distancePoint1Helper + " метров" + System.lineSeparator()
                + "  Дистанция до объекта B" + " равняется " + distance2Point + " метров" + System.lineSeparator());
    }

    private void calcSizeObjectMode(double xO, double yO, double pixelWSize, double pixelHSize, double l, double f) {
        calcDisplacement(Math.abs(xO + point1Helper.getX()) / 2, Math.abs(yO + point1Helper.getY() / 2));
        double deltaX = Math.abs(dx) * pixelWSize / 1000;
        double distance = ((l / 1000) * (f / 1000) / deltaX);
        double realWidth = distance * (Math.abs(xO - point1Helper.getX())) * pixelWSize / f;
        double realHeight = distance * (Math.abs(yO - point1Helper.getY())) * pixelHSize / f;

        addItem(new ObjectSizeItem(
                (int) point1Helper.getX(),
                (int) point1Helper.getY(),
                (int) xO, (int) yO,
                distance, realWidth, realHeight));

        reportField.appendText(items.size() + ") Размеры объекта M:  ширина =" + realWidth +
                " метров; высота = " + realHeight + " метров" + System.lineSeparator() +
                "  Дистанция до объекта M" + " равняется " + distance + " метров" + System.lineSeparator());
    }
}