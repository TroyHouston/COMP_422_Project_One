package project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
 * Created by troyr on 8/2/2015.
 */
public class UserInterface {

    Stage primaryStage;
    // Tab 1 Image
    String currentImage1 = "test-pattern.tif";
    // Tab 2 Image
    String currentImage2 = "hubble.tif";

    public UserInterface(Stage primaryStage) {

        this.primaryStage = primaryStage;
        BorderPane basePane = new BorderPane();

        // Window settings
        Scene scene = new Scene(basePane);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:assets/eye.png"));

        // ~~~~~~~~~~~~~~~~~~~ TABS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        TabPane sectionTabs = new TabPane();
        Tab tab1 = new Tab();
        tab1.setText("Section 1.0");

        tab1.setContent(new TabOne(currentImage1, primaryStage));
        tab1.setClosable(false);

        Tab tab2 = new Tab();
        tab2.setText("Section 2.1");
        tab2.setContent(new TabTwo(currentImage2, primaryStage));
        tab2.setClosable(false);

        Tab tab3 = new Tab();
        tab3.setText("Section 2.2");
        tab3.setContent(null);
        tab3.setClosable(false);

        Tab tab4 = new Tab();
        tab4.setText("Section 3.0");
        tab4.setContent(new TabFour(primaryStage));
        tab4.setClosable(false);

        sectionTabs.getTabs().add(tab1);
        sectionTabs.getTabs().add(tab2);
        sectionTabs.getTabs().add(tab3);
        sectionTabs.getTabs().add(tab4);
        basePane.setCenter(sectionTabs);

        // Show window
        primaryStage.sizeToScene();
        primaryStage.show();

    }

}
