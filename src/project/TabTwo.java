package project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
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
public class TabTwo extends GridPane {

    String currentImage2;
    Slider tSlider;
    ImageView afterImage;

    public TabTwo(String imagefile, Stage primaryStage) {

        this.currentImage2 = imagefile;

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
        Text scenetitle = new Text("MINING IMAGE DATA");
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
        Label altered = new Label("Galaxy Map");
        altered.setFont(Font.font("Rockwell", FontWeight.BOLD, 18));
        HBox hbA = new HBox(10);
        hbA.getChildren().add(altered);
        hbA.setAlignment(Pos.CENTER);
        this.add(hbA, 2, 2);
        afterImage = new ImageView();
        HBox hbAI = new HBox(0);
        hbAI.getChildren().add(afterImage);
        hbAI.setAlignment(Pos.TOP_CENTER);
        this.add(hbAI, 2, 3);

        BufferedImage tifInput = null;
        try {
            tifInput = ImageIO.read(new File("assets/" + currentImage2));
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

        Label ieLabel = new Label("Galaxy Mining:");
        ieLabel.setFont(Font.font("Rockwell", FontWeight.BOLD, 16));
        functionsBox.getChildren().add(ieLabel);

        // ~~~~~~~~~~~~~~~~~~~~ RADIO BUTTONS ~~~~~~~~~~~~~~~~~~~~~~~~~


        SplitPane split2 = new SplitPane();
        functionsBox.getChildren().add(split2);

        Label fLabel = new Label("Filter Size:");
        fLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        functionsBox.getChildren().add(fLabel);

        final ToggleGroup group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("3 x 3");
        rb1.setUserData(3);
        rb1.setToggleGroup(group);
        rb1.setSelected(true);
        rb1.requestFocus();
        functionsBox.getChildren().add(rb1);

        RadioButton rb2 = new RadioButton("5 x 5");
        rb2.setUserData(5);
        rb2.setToggleGroup(group);
        functionsBox.getChildren().add(rb2);

        RadioButton rb3 = new RadioButton("7 x 7");
        rb3.setUserData(7);
        rb3.setToggleGroup(group);
        functionsBox.getChildren().add(rb3);

        RadioButton rb4 = new RadioButton("9 x 9");
        rb4.setUserData(9);
        rb4.setToggleGroup(group);
        functionsBox.getChildren().add(rb4);

        RadioButton rb5 = new RadioButton("11 x 11");
        rb5.setUserData(11);
        rb5.setToggleGroup(group);
        functionsBox.getChildren().add(rb5);

        SplitPane split3 = new SplitPane();
        functionsBox.getChildren().add(split3);

        // ~~~~~~~~~~~~~~~~Slider ~~~~~~~~~~~~~~~~~~~~~~~~~~~

        Label tLabel = new Label("Threshold:");
        tLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        functionsBox.getChildren().add(tLabel);

        tSlider = new Slider(35, 255, 90);
        tSlider.setOrientation(Orientation.VERTICAL);
        tSlider.setShowTickLabels(true);
        tSlider.setMinorTickCount(5);
        tSlider.setMajorTickUnit(55);
        tSlider.setPrefHeight(240);
        tSlider.setShowTickMarks(true);
        functionsBox.getChildren().add(tSlider);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    double value = tSlider.getValue();
                    int selected = (int) group.getSelectedToggle().getUserData();
                    afterImage.setImage(SwingFXUtils.toFXImage(ImageMining.start(currentImage2, selected, value), null));
                }
            }
        });

        tSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_toggle, Number new_toggle) {
                double value = tSlider.getValue();
                int selected = (int) group.getSelectedToggle().getUserData();
                afterImage.setImage(SwingFXUtils.toFXImage(ImageMining.start(currentImage2, selected, value), null));
            }
        });

        double value = tSlider.getValue();
        int selected = (int) group.getSelectedToggle().getUserData();
        afterImage.setImage(SwingFXUtils.toFXImage(ImageMining.start(currentImage2, selected, value), null));


    }
}
