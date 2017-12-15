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
import java.util.Observable;
import java.util.Observer;

import model.representation.Card;
import model.representation.game.Deck;
import model.processor.EquityProcessor;
import model.game.HandlerObserver;
import model.processor.EquityProcessor;

public class EquityController implements Observer {

    private static final String NUM_PLAYERS_TEXT = "Num Players: ";
    	private static final String NUM_SIMULATIONS_TEXT = "Num. Simulations: ";
    private static final String[] PHASES = {"Select The Player Card", "The PreFlop", "The Flop", "The Turn", "The River"};

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
    @FXML
    private Label _lTitle;

    private ArrayList<PlayerController> hlPlayerController = null;
    private int numPlayers;
    private int phaseCounter = 0;
    private Deck deck = new Deck();
    private EquityProcessor equityProcessor;
    public EquityController() {
        numPlayers = 6;
        hlPlayerController = new ArrayList<PlayerController>(numPlayers);
	equityProcessor = new EquityProcessor();
        addPlayers();
      
        PlayerObserver.init();
        PlayerObserver.addObserver(this);
	    
        HandlerObserver.init();
        HandlerObserver.addObserver(this);
    }

    /**
     * It adds all players to the board
     */
    private void addPlayers(){
        Platform.runLater(() -> {
            double t = Math.PI;
            double increment = (2*Math.PI)/numPlayers;
            for (int i = 0; i < numPlayers; i++) {
                double tempT = t;
                addPlayer(tempT, i);
                t += increment;
            }
        });
    }

    /**
     * It adds a player to the board and catch the controller
     * @param angleT
     */
    private void addPlayer(double angleT, int player) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane root = fxmlLoader.load(getClass().getResource("../../view/playerPane.fxml").openStream());
            double x = _eBoard.getRadiusX()*Math.sin(angleT) + _pBoard.getWidth()/2 - root.getPrefWidth()/1.8*(Math.sin(angleT)) - 30;
        	double y = _eBoard.getRadiusY()*Math.cos(angleT) + _pBoard.getHeight()/2 - root.getPrefHeight()/2*(1 + Math.pow(Math.cos(angleT), 3)/1.2) - 10;
            root.setLayoutX(x);
            root.setLayoutY(y);
            _pBoard.getChildren().add(root);
            ((PlayerController) fxmlLoader.getController()).setNumPlayer(player);
            ((PlayerController) fxmlLoader.getController()).setDeck(this.deck);
            hlPlayerController.add(fxmlLoader.getController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickNumPlayers(ActionEvent actionEvent) {
        numPlayers = Integer.parseInt(((MenuItem)actionEvent.getSource()).getText());
        _mbtNumPlayers.setText(NUM_PLAYERS_TEXT + numPlayers);
        _pBoard.getChildren().remove(_pBoard.getChildren().size()- hlPlayerController.size(), _pBoard.getChildren().size());
        hlPlayerController.clear();
        addPlayers();
    }

    public void onClickNextPhase(ActionEvent actionEvent) {
        phaseCounter = ++phaseCounter%PHASES.length;
        _lTitle.setText(PHASES[phaseCounter]);
        if(phaseCounter == 0)
            clear();
        else if(phaseCounter == 1)
            _mbtNumPlayers.setDisable(true);
    }

    public void onClickClear(ActionEvent actionEvent) {
        clear();
    }

    public void clear(){
        Platform.runLater(() -> {
   	        equityProcessor.stopThreads();
            _mbtNumPlayers.setDisable(false);
            _lbNumSimu.setText(NUM_SIMULATIONS_TEXT + 0);
            phaseCounter = 0;
            _lTitle.setText(PHASES[phaseCounter]);
            for (PlayerController pc : hlPlayerController)
                pc.clear();
        });
    }

	@Override
	 /**
     * It recibe the notify with the solution of the CardSelector, wich includes
     * the cards selected, the player who choose the cards and the deck after changes
     */
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof double[]){
			double [] equities = (double[]) arg1;
			for(PlayerController p : this.hlPlayerController)
				p.writeEquity(equities[p.hashCode()]);
				
		}
		else if(arg1 instanceof Integer){
			Platform.runLater(()->{
				String number = String.format("%,d", arg1);
				this._lbNumSimu.setText(NUM_SIMULATIONS_TEXT + number);
			});
		}
		else{
			// TODO Auto-generated method stub
			CSolution solution = (CSolution)arg1;
			ArrayList<Card> cards = solution.getCards();
			int numPlayer = solution.getNumPlayer();
			hlPlayerController.get(numPlayer).setCd1(cards.get(0));
			hlPlayerController.get(numPlayer).setCd2(cards.get(1));
			this.deck = solution.getDeck();
			hlPlayerController.get(numPlayer).setDeck(this.deck);
		}
	}
}
