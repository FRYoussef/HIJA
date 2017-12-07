package control.equity;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Ellipse;

import java.util.ArrayList;

public class EquityController {

    private static final String NUM_PLAYERS_TEXT = "Num Players: ";

    @FXML
    private MenuButton _mbtNumPlayers;
    @FXML
    private Label _lbNumSimu;
    @FXML
    private HBox _hbBoardCards;
    @FXML
    private Ellipse _eBoard;
    @FXML
    private AnchorPane _pBoard;

    private ArrayList<PlayerController> alPlayerController = null;
    private int numPlayers;

    public EquityController() {
        numPlayers = 8;
        alPlayerController = new ArrayList<>(numPlayers);
        addPlayers();
    }

    /**
     * It adds all players to the board
     */
    private void addPlayers(){
    	double t = Math.PI;
    	double increment = (2*Math.PI)/numPlayers;
    	for (int i = 0; i < numPlayers; i++) {
            //ellipse pratitions
    		double tempT = t;
            Platform.runLater(() -> addPlayer(tempT));
            t += increment;
        }
    }

    /**
     * It adds a player to the board and catch the controller
     * @param x position to the pane
     * @param y position to the pane
     */
    private void addPlayer(double angleT) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane root = fxmlLoader.load(getClass().getResource("../../view/playerPane.fxml").openStream());
            double x = _eBoard.getRadiusX()*Math.sin(angleT) + _pBoard.getWidth()/2 - root.getPrefWidth()/1.8*(Math.sin(angleT)) - 30;
        	double y = _eBoard.getRadiusY()*Math.cos(angleT) + _pBoard.getHeight()/2 - root.getPrefHeight()/2*(1 + Math.pow(Math.cos(angleT), 3)/1.2) - 10;
            root.setLayoutX(x);
            root.setLayoutY(y);
            _pBoard.getChildren().add(root);
            alPlayerController.add((PlayerController) fxmlLoader.getController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double divideEllipse(){
        return 0.0d;
    }


    public void onClickNumPlayers(ActionEvent actionEvent) {
        numPlayers = Integer.parseInt(((MenuItem)actionEvent.getSource()).getText());
        _mbtNumPlayers.setText(NUM_PLAYERS_TEXT + numPlayers);
        addPlayers();
    }
}
