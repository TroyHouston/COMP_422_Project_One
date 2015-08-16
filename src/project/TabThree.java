package project;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.core.Instances;
import weka.core.Utils;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by troyr on 8/7/2015.
 */
public class TabThree extends GridPane {

    FaceDetection main;
    String[] featureNames = {"gMean", "gStD", "nMean", "nStD", "eMean", "eStD", "gMoment", "hh", "hh", "hh"};
    final TabThree tabThis = this;

    public TabThree(Stage primaryStage) {

        main = new FaceDetection();

        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(25, 25, 25, 25));

        // Title
        Text sceneTitle = new Text("FACE DETECTION");
        sceneTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        HBox hTitle = new HBox(10);
        hTitle.getChildren().add(sceneTitle);
        hTitle.setAlignment(Pos.CENTER_LEFT);
        this.add(hTitle, 0, 0);

        TextArea resultsBox = new TextArea();
        resultsBox.setPrefSize(600, 500);

        this.add(resultsBox, 0, 2);

        TextArea rocCurve = new TextArea();
        rocCurve.setPrefSize(600, 500);

        this.add(rocCurve, 1, 2);

        ArrayList<CheckBox> cba = new ArrayList<CheckBox>();
        for (int i = 0; i < 10; i++) {
            CheckBox cb = new CheckBox(featureNames[i]);
            cb.setSelected(true);
            cba.add(cb);
        }

        HBox fileRow1 = new HBox(10);
        fileRow1.getChildren().add(new Label("Features To Use: "));
        for (CheckBox cb : cba) {
            fileRow1.getChildren().add(cb);
        }
        this.add(fileRow1, 0, 1, 2, 1);

        Button loadButton = new Button();
        loadButton.setText("Load, Train & Test");
        loadButton.setPrefSize(1220, 30);
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int featuresToUse[] = new int[cba.size()];
                int index = 0;
                for (CheckBox cb : cba) {
                    featuresToUse[index] = (cb.isSelected()) ? 1 : 0;
                    index++;
                }

                main.loadData(featuresToUse);
                main.trainBayesClassifier();

                resultsBox.setText(main.getResults() + "\n\n" + main.getMatrix());

                final SwingNode swingNode = new SwingNode();
                createSwingContent(swingNode);
                tabThis.add(swingNode, 1, 2);
            }
        });

        this.add(loadButton, 0, 3, 2, 1);

    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Instances result = main.getROCResults();
                ThresholdCurve tc = main.getROC();

                // plot curve
                ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
                vmc.setROCString("(Area under ROC = " + Utils.doubleToString(tc.getROCArea(result), 4) + ")");
                vmc.setName(result.relationName());
                PlotData2D tempd = new PlotData2D(result);
                tempd.setPlotName(result.relationName());
                tempd.addInstanceNumberAttribute();
                // specify which points are connected
                boolean[] cp = new boolean[result.numInstances()];
                for (int n = 1; n < cp.length; n++)
                    cp[n] = true;
                try {
                    tempd.setConnectPoints(cp);
                    // add plot
                    vmc.addPlot(tempd);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //swingNode.setContent(new JButton("Click me!"));
                //JTextArea rocCurve = new JTextArea("Hello");
                //rocCurve.setPreferredSize(new Dimension(600,500));
                vmc.setPreferredSize(new Dimension(600,500));
                swingNode.setContent(vmc);
            }
        });
    }

}
