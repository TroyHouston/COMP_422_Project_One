package project;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import weka.gui.treevisualizer.Node;
import weka.gui.treevisualizer.NodePlace;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeDisplayListener;
import weka.gui.treevisualizer.TreeVisualizer;
import javafx.scene.control.TextArea;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

/**
 * Created by troyr on 8/7/2015.
 */
public class TabFour extends GridPane {

    private DataMining mine1;
    private DataMining mine2;
    private TextArea results1;
    private TextArea results2;
    private TextArea matrix1;
    private TextArea matrix2;

    private ArrayList<CheckBox> cba1;
    private ArrayList<CheckBox> cba2;
    private String[] files = {"fac", "fou", "kar", "mor", "pix", "zer"};

    public TabFour(Stage primaryStage) {

        mine1 = new DataMining();
        mine2 = new DataMining();

        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(25, 25, 25, 25));

        // Title
        Text sceneTitle = new Text("DATA MINING");
        sceneTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        HBox hTitle = new HBox(10);
        hTitle.getChildren().add(sceneTitle);
        hTitle.setAlignment(Pos.CENTER_LEFT);
        this.add(hTitle, 0, 0);

        results1 = new TextArea();
        results1.setFont(new Font("Courier New", 14));
        results1.setPrefSize(600, 250);

        results2 = new TextArea();
        results2.setFont(new Font("Courier New", 14));
        results2.setPrefSize(600, 250);

        matrix1 = new TextArea();
        matrix1.setFont(new Font("Courier New", 14));
        matrix1.setPrefSize(600, 250);

        matrix2 = new TextArea();
        matrix2.setFont(new Font("Courier New", 14));
        matrix2.setPrefSize(600, 250);

        Button showTree1 = new Button("Show Decision Tree");
        showTree1.setPrefSize(600,30);
        showTree1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Do noise cancellation method
                if (mine1.getMatrix() != null)
            	    createSwingWindow(mine1);
            }
        });

        Button showTree2 = new Button("Show Decision Tree");
        showTree2.setPrefSize(600,30);
        showTree2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Do noise cancellation method
                if (mine2.getMatrix() != null)
            	    createSwingWindow(mine2);
            }
        });

        this.add(results1, 0, 3);
        this.add(results2, 1, 3);
        this.add(matrix1, 0, 4);
        this.add(matrix2, 1, 4);
        this.add(showTree1, 0, 5);
        this.add(showTree2, 1, 5);


        cba1 = new ArrayList<CheckBox>();
        for (int i = 0; i < 6; i++) {
        	CheckBox cb = new CheckBox(files[i]);
        	cb.setSelected(true);
        	cba1.add(cb);
        }

        HBox fileRow1 = new HBox(10);
        fileRow1.getChildren().add(new Label("Feature Sets To Use: "));
        for (CheckBox cb : cba1) {
        	fileRow1.getChildren().add(cb);
        }
        this.add(fileRow1, 0, 2);

        cba2 = new ArrayList<CheckBox>();
        for (int i = 0; i < 6; i++) {
        	cba2.add(new CheckBox(files[i]));
        }

        HBox fileRow2 = new HBox(10);
        fileRow2.getChildren().add(new Label("Feature Sets To Use: "));
        for (CheckBox cb : cba2) {
        	fileRow2.getChildren().add(cb);
        }
        this.add(fileRow2, 1, 2);

        Button loadButton1 = new Button("Load, Train & Test");
        loadButton1.setPrefSize(600,30);
        loadButton1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	int filesToLoad[] = new int[6];
            	int index = 0;
            	for (CheckBox cb : cba1) {
            		filesToLoad[index] = (cb.isSelected()) ? 1 : 0;;
            		index++;
            	}

                mine1.loadData(filesToLoad);
                mine1.trainJ48Classifier();

                results1.setText(mine1.getResults());
                matrix1.setText(mine1.getMatrix());

            }
        });;

        Button loadButton2 = new Button("Load, Train & Test");
        loadButton2.setPrefSize(600,30);
        loadButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	int filesToLoad[] = new int[6];
            	int index = 0;
            	for (CheckBox cb : cba2) {
            		filesToLoad[index] = (cb.isSelected()) ? 1 : 0;;
            		index++;
            	}

                mine2.loadData(filesToLoad);
                mine2.trainJ48Classifier();

                results2.setText(mine2.getResults());
                matrix2.setText(mine2.getMatrix());
            }
        });;

        this.add(loadButton1, 0, 6);
        this.add(loadButton2, 1, 6);

    }

    /**
     * Have a button to open this window....
     */
    private void createSwingWindow(final DataMining mine) {
        // display classifier
        final javax.swing.JFrame jf = new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
        jf.setSize(1800,900);
        jf.getContentPane().setLayout(new BorderLayout());
        final TreeVisualizer tv = new TreeVisualizer(null, mine.getGraph(), new PlaceNode2());
        tv.setDoubleBuffered(true);
        // Overload the jf.paintcomponent method to repaint a stored treevis on resize.
        jf.getContentPane().add(tv, BorderLayout.CENTER);

        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                jf.dispose();
            }
        });
        jf.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                tv.fitToScreen();
            }
        });

        jf.setVisible(true);
        tv.fitToScreen();

    }
}
