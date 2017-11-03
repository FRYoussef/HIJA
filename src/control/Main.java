package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        try
        {
            AnchorPane root = FXMLLoader.load(getClass().getResource("../view/sample.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("view/pokerStyle.css").toExternalForm());
            primaryStage.setTitle("Poker");
            primaryStage.setScene(scene);
            primaryStage.show();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args) { launch(args); }

}
