package project;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // ~~~~~~~~~~~~~~~~~~~ WINDOW SETUP CODE ~~~~~~~~~~~~~~~~~~~~~~~~

        // Create window
        primaryStage.setTitle("COMP 422 - Project One");
        UserInterface ui = new UserInterface(primaryStage);



    }


    public static void main(String[] args) {launch(args); }
}
