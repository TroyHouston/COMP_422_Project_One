package project;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import javafx.scene.control.TextArea;

import javax.swing.*;
import java.awt.*;

/**
 * Created by troyr on 8/7/2015.
 */
public class TabFour extends GridPane {

    private DataMining main;

    public TabFour(Stage primaryStage) {

        main = new DataMining();
        int filesToLoad[] = {1,1,1,1,1,1};
        main.loadData(filesToLoad);
        main.trainJ48Classifier();

        // Text/Title
        // Button one --- button two
        // Tableview --- text area

        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(25, 25, 25, 25));
//        ColumnConstraints col1 = new ColumnConstraints();
//        col1.setFillWidth(true);
//        ColumnConstraints col2 = new ColumnConstraints();
//        col2.setPrefWidth(500);
//        ColumnConstraints col3 = new ColumnConstraints();
//        col3.setPrefWidth(500);
//        this.getColumnConstraints().addAll(col1, col2, col3);
//
//        RowConstraints row1 = new RowConstraints();
//        row1.setPrefHeight(50);
//        RowConstraints row2 = new RowConstraints();
//        row2.setPrefHeight(0);
//        RowConstraints row3 = new RowConstraints();
//        row3.setPrefHeight(30);
//        RowConstraints row4 = new RowConstraints();
//        row4.setPrefHeight(550);
//        this.getRowConstraints().addAll(row1, row2, row3, row4);

        // Title
        Text sceneTitle = new Text("DATA MINING");
        sceneTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        HBox hTitle = new HBox(10);
        hTitle.getChildren().add(sceneTitle);
        hTitle.setAlignment(Pos.CENTER_LEFT);
        this.add(hTitle, 0, 0);

        TextArea results = new TextArea();
        results.setFont(new Font("Courier New", 14));
results.setPrefSize(600, 250);
        results.setText(main.results);

        TextArea results2 = new TextArea();
        results2.setFont(new Font("Courier New", 14));
        results2.setPrefSize(600, 250);
        results2.setText(main.results);

        this.add(results, 0, 1);
        this.add(results2,1,1);

        createSwingWindow();


    }

    /**
     * Have a button to open this window....
     */
    private void createSwingWindow() {
        // display classifier
        final javax.swing.JFrame jf = new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
        jf.setSize(900,900);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv = new TreeVisualizer(null, main.getGraph(), new PlaceNode2());
        tv.setDoubleBuffered(true);
        // Overload the jf.paintcomponent method to repaint a stored treevis on resize.
        jf.getContentPane().add(tv, BorderLayout.CENTER);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                jf.dispose();
            }
        });

        jf.setVisible(true);
        tv.fitToScreen();

    }

}
