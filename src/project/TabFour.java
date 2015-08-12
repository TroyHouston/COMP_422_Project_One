package project;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by troyr on 8/7/2015.
 */
public class TabFour extends GridPane {

    DataMining main;

    public TabFour(Stage primaryStage) {

        main = new DataMining();

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

        // Table
        TableView dataTable1 = new TableView();
        dataTable1.setEditable(false);
        //ScrollBar hScroll = findScrollBar( dataTable1, Orientation.HORIZONTAL);
        //ScrollBar vSCroll = findScrollBar( dataTable1, Orientation.VERTICAL);

        TableColumn firstNameCol = new TableColumn("Class ID");
        TableColumn lastNameCol = new TableColumn("Feature 1");
        TableColumn emailCol = new TableColumn("Feature 2");

        dataTable1.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        this.add(dataTable1,0,1);
    }
}
