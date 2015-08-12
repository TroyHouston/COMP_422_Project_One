package project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by troyr on 8/7/2015.
 */
public class TabOne extends  GridPane{

    String currentImage1;

    public TabOne(String imageFile, Stage primaryStage) {

        this.currentImage1 = imageFile;
        // Create grid layout
        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(25, 25, 25, 25));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setFillWidth(true);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(500);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(500);
        this.getColumnConstraints().addAll(col1, col2, col3);

        RowConstraints row1 = new RowConstraints();
        row1.setPrefHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPrefHeight(0);
        RowConstraints row3 = new RowConstraints();
        row3.setPrefHeight(30);
        RowConstraints row4 = new RowConstraints();
        row4.setPrefHeight(550);
        this.getRowConstraints().addAll(row1, row2, row3, row4);

        // Title
        Text scenetitle = new Text("IMAGE PREPROCESSING AND BASIC OPERATORS");
        scenetitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        HBox hTitle = new HBox(10);
        hTitle.getChildren().add(scenetitle);
        hTitle.setAlignment(Pos.CENTER_LEFT);
        this.add(hTitle, 0, 0, 4, 1);

        // ~~~~~~~~~~~~~~~~~~~ IMAGE VIEWS ~~~~~~~~~~~~~~~~~~~~~~

        // Before Image
        Label original = new Label("Original Image");
        original.setFont(Font.font("Rockwell", FontWeight.BOLD, 18));
        HBox hbO = new HBox(10);
        hbO.getChildren().add(original);
        hbO.setAlignment(Pos.CENTER);
        this.add(hbO, 1, 2);
        ImageView beforeImage = new ImageView();
        HBox hbOI = new HBox(0);
        hbOI.getChildren().add(beforeImage);
        hbOI.setAlignment(Pos.TOP_CENTER);
        this.add(hbOI, 1, 3);

        // After Image
        Label altered = new Label("Altered Image");
        altered.setFont(Font.font("Rockwell", FontWeight.BOLD, 18));
        HBox hbA = new HBox(10);
        hbA.getChildren().add(altered);
        hbA.setAlignment(Pos.CENTER);
        this.add(hbA, 2, 2);
        ImageView afterImage = new ImageView();
        HBox hbAI = new HBox(0);
        hbAI.getChildren().add(afterImage);
        hbAI.setAlignment(Pos.TOP_CENTER);
        this.add(hbAI, 2, 3);

        BufferedImage tifInput = null;
        try {
            tifInput = ImageIO.read(new File("assets/" + currentImage1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image = SwingFXUtils.toFXImage(tifInput, null);
        beforeImage.setImage(image);
        afterImage.setImage(image);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~~~~~~~~ LEFT-SIDE BUTTON MENU ~~~~~~~~~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        VBox functionsBox = new VBox(10);
        this.add(functionsBox, 0, 3);

        // ~~~~~~~~~~~~~~~~~~~~ RADIO BUTTONS ~~~~~~~~~~~~~~~~~~~~~~~~~
        Label images = new Label("Images:");
        images.setFont(Font.font("Rockwell", FontWeight.BOLD, 16));
        functionsBox.getChildren().add(images);

        final ToggleGroup group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("Test Pattern");
        rb1.setUserData("test-pattern.tif");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);
        rb1.requestFocus();
        functionsBox.getChildren().add(rb1);

        RadioButton rb2 = new RadioButton("Circuit Board");
        rb2.setUserData("ckt-board-saltpep.tif");
        rb2.setToggleGroup(group);
        functionsBox.getChildren().add(rb2);

        RadioButton rb3 = new RadioButton("Moon");
        rb3.setUserData("blurry-moon.tif");
        rb3.setToggleGroup(group);
        functionsBox.getChildren().add(rb3);

        SplitPane split3 = new SplitPane();
        functionsBox.getChildren().add(split3);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    BufferedImage tempTifInput = null;
                    try {
                        tempTifInput = ImageIO.read(new File("assets/" +group.getSelectedToggle().getUserData().toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final Image imageF = SwingFXUtils.toFXImage(tempTifInput, null);
                    currentImage1 = group.getSelectedToggle().getUserData().toString();
                    beforeImage.setImage(imageF);
                    afterImage.setImage(imageF);
                    primaryStage.sizeToScene();
                }
            }
        });

        Label edLabel = new Label("Edge Detection:");
        edLabel.setFont(Font.font("Rockwell", FontWeight.BOLD, 16));
        functionsBox.getChildren().add(edLabel);
        //HBox hbF = new HBox(10);
        //hbF.getChildren().add(functions);
        // t1Grid.add(hbF, 0, 5);

        // ~~~~~~~~~~~~~~~~~~~~~ FUNCTION BUTTONS ~~~~~~~~~~~~~~~~~~~~~~~

        // Edge Detection Button
        Button edBtn = new Button("Sobel Filter");
        edBtn.setPrefSize(150,25);
        functionsBox.getChildren().add(edBtn);
        edBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Do edge detect method
                afterImage.setImage(SwingFXUtils.toFXImage(ImagePreprocessing.sobelEdges(currentImage1), null));
            }
        });

        Button nc3Btn = new Button("Laplacian Filter");
        nc3Btn.setPrefSize(150,25);
        functionsBox.getChildren().add(nc3Btn);
        nc3Btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Do noise cancellation method
                afterImage.setImage(SwingFXUtils.toFXImage(ImagePreprocessing.laplacianFilter(currentImage1), null));
            }
        });

        SplitPane split1 = new SplitPane();
        functionsBox.getChildren().add(split1);

        Label ncLabel = new Label("Noise Cancellation:");
        ncLabel.setFont(Font.font("Rockwell", FontWeight.BOLD, 16));
        functionsBox.getChildren().add(ncLabel);
        final ToggleGroup groupF = new ToggleGroup();

        // Noise Cancellation Button
        Button nc1Btn = new Button("Mean Filter");
        nc1Btn.setPrefSize(150,25);
        functionsBox.getChildren().add(nc1Btn);
        nc1Btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Do noise cancellation method
                int maskSize = (int) groupF.getSelectedToggle().getUserData();
                afterImage.setImage(SwingFXUtils.toFXImage(ImagePreprocessing.noiseCancellation(currentImage1, 1, maskSize), null));
            }
        });

        // Noise Cancellation Button
        Button nc2Btn = new Button("Median Filter");
        nc2Btn.setPrefSize(150,25);
        functionsBox.getChildren().add(nc2Btn);
        nc2Btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Do noise cancellation method
                int maskSize = (int) groupF.getSelectedToggle().getUserData();
                afterImage.setImage(SwingFXUtils.toFXImage(ImagePreprocessing.noiseCancellation(currentImage1, 2, maskSize), null));
            }
        });



        RadioButton rbf1 = new RadioButton("3 x 3");
        rbf1.setUserData(3);
        rbf1.setToggleGroup(groupF);
        rbf1.setSelected(true);
        rbf1.requestFocus();
        functionsBox.getChildren().add(rbf1);

        RadioButton rbf2 = new RadioButton("5 x 5");
        rbf2.setUserData(5);
        rbf2.setToggleGroup(groupF);
        functionsBox.getChildren().add(rbf2);


        Label ieLabel = new Label("Image Enhancement:");
        ieLabel.setFont(Font.font("Rockwell", FontWeight.BOLD, 16));
        functionsBox.getChildren().add(ieLabel);

        // Image Enhancement Button
        Button ieBtn = new Button("Deblur");
        ieBtn.setPrefSize(150,25);
        functionsBox.getChildren().add(ieBtn);
        ieBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Do image enhancement method
                afterImage.setImage(SwingFXUtils.toFXImage(ImagePreprocessing.imageEnhancement(currentImage1), null));
            }
        });

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~~~~~~~~ IMAGE MENU ~~~~~~~~~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // Right-hand side menu
        // VBox imageBox = new VBox(10);
        // grid.add(imageBox, 3, 6);

        SplitPane split2 = new SplitPane();
        functionsBox.getChildren().add(split2);
    }

}
