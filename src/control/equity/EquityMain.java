package control.equity;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EquityMain extends Application {

    @Override
    public void start(Stage primaryStage){
        try
        {
            AnchorPane root = FXMLLoader.load(getClass().getResource("../../view/equity.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Equity Calculator");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	       @Override
        	       public void handle(WindowEvent e) {
        	          Platform.exit();
        	          System.exit(0);
        	       }
        	    });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) { launch(args); }

}
