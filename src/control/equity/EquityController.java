package control.equity;

import control.ObserverPatron.OPlayerCards;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Ellipse;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import control.ObserverPatron.OSolution;
import javafx.stage.Stage;
import model.representation.Card;
import model.representation.Player;
import model.processor.EquityProcessor;
import control.ObserverPatron.HandlerObserver;

public class EquityController implements Observer {

    private static final String NUM_PLAYERS_TEXT = "Num Players: ";
    private static final String NUM_SIMULATIONS_TEXT = "Num. Simulations: ";
    private static final String[] PHASES = {"The PreFlop", "The Flop", "The Turn", "The River"};
    private static final int PHASE_PREFLOP = 0;
    private static final int PHASE_FLOP = 1;
    private static final int PHASE_TURN = 2;
    private static final int PHASE_RIVER = 3;
    private static final int MAX_PLAYERS_HE = 8;
    private static final int MAX_PLAYERS_OMAHA = 6;
    private static final int HE_NUM_CARDS = 2;
    private static final int OMAHA_NUM_CARDS = 4;
    private static final int DEFAULT_STOP_LIMIT = 2000000;

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
    @FXML
    private TextArea _taLog;
    @FXML
    private Button _btCalculate;
    @FXML
    private TextField _tfStopLimit;
    @FXML
    private Button _btStop;
    @FXML
    private Button _btPhase;

    private ArrayList<PlayerController> alPlayerController = null;
    private int numPlayers;
    private int remainPlayers;
    private int phase = 0;
    private EquityProcessor equityProcessor;
    private Stage stageCardsSelec = null;
    private CardSelectorController cs = null;
    private int stopLimit = DEFAULT_STOP_LIMIT;


    public EquityController() {
        numPlayers = 6;
        remainPlayers = numPlayers;
        alPlayerController = new ArrayList<PlayerController>(numPlayers);
	    equityProcessor = new EquityProcessor(MAX_PLAYERS_HE);
        addPlayers();
	    createStageSelector();
        HandlerObserver.init();
        HandlerObserver.addObserver(this);
    }

    private void createStageSelector(){
        try {
            cs = new CardSelectorController(equityProcessor.getDeck(), null,
                    HE_NUM_CARDS, -1);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/cardSelector.fxml"));
            fxmlLoader.setController(cs);
            stageCardsSelec = new Stage();
            stageCardsSelec.setScene(new Scene(fxmlLoader.load()));
            stageCardsSelec.setTitle("Card Selector");
            stageCardsSelec.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            ((PlayerController) fxmlLoader.getController()).init(player, null, null, stageCardsSelec, equityProcessor.getDeck(), cs);
            alPlayerController.add(fxmlLoader.getController());
            equityProcessor.addPlayer(new Player(player));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickNumPlayers(ActionEvent actionEvent) {
        numPlayers = Integer.parseInt(((MenuItem)actionEvent.getSource()).getText());
        remainPlayers = numPlayers;
        _mbtNumPlayers.setText(NUM_PLAYERS_TEXT + numPlayers);
        _pBoard.getChildren().remove(_pBoard.getChildren().size()- alPlayerController.size(), _pBoard.getChildren().size());
        alPlayerController.clear();
        equityProcessor.removeAllPlayers();
        addPlayers();
    }

    private boolean evaluatePhase(){
        int remainCards = 0;
        if(phase == PHASE_PREFLOP && !equityProcessor.isPlayersGetCards(remainPlayers)){
            writeTA("You should to introduce the cards of the players");
            return false;
        }
        else if(phase == PHASE_FLOP)
            remainCards = 3;

        else if(phase == PHASE_TURN)
            remainCards = 4;

        else if(phase == PHASE_RIVER)
            remainCards = 5;

        int finalRemainCards = remainCards;

        try{
            for (int i = equityProcessor.numCardsBoard(); i < finalRemainCards; i++) {
                Card c = equityProcessor.getRandomBoardCard();
                ((ImageView)_hbBoardCards.getChildren().get(i)).setImage(new Image("resources/cards/" + c + ".png"));
                _hbBoardCards.getChildren().get(i).setDisable(true);
            }
        }catch (Exception e){
            e.printStackTrace();
            writeTA(e.getMessage());
        }

        return true;
    }

    public void onClickNextPhase(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            if(!evaluatePhase())
                return;

            phase = ++phase %PHASES.length;
            _lTitle.setText(PHASES[phase]);

            if(phase == 0)
                clear();
            else if(phase == 1)
                _mbtNumPlayers.setDisable(true);
        });
    }

    public void onClickCalculate(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            if (!evaluatePhase())
                return;

            try{
                if(!_tfStopLimit.getText().equals(""))
                    stopLimit = Integer.parseInt(_tfStopLimit.getText());
            }catch (Exception e){
                e.printStackTrace();
                writeTA("Please, write a correct number");
            }
            disbledForSim(true);
            equityProcessor.calculateEquity();
        });
    }

    private void disbledForSim(boolean b){
        _btCalculate.setDisable(b);
        _btPhase.setDisable(b);
        for(PlayerController p : alPlayerController)
            p.disableForSim(b);
    }

    public void onClickClear(ActionEvent actionEvent) {
        clear();
    }

    public void clear(){
        Platform.runLater(() -> {
   	        equityProcessor.stopThreads();
   	        disbledForSim(false);
            _mbtNumPlayers.setDisable(false);
            _lbNumSimu.setText(NUM_SIMULATIONS_TEXT + 0);
            phase = 0;
            remainPlayers = numPlayers;
            _lTitle.setText(PHASES[phase]);
            for (PlayerController pc : alPlayerController)
                pc.clear();
            for(Node n : _hbBoardCards.getChildren()) {
                n.setDisable(false);
                ((ImageView)n).setImage(null);
            }
            equityProcessor.removeAllPlayers();
        });
    }

    private void writeTA(String text){
        Platform.runLater(() -> _taLog.appendText(text + "\n"));
    }

	@Override
	 /**
     * It recibe the notify with the solution of the CardSelector, wich includes
     * the cards selected, the player who choose the cards and the deck after changes
     */
	public void update(Observable arg0, Object arg1) {
        OSolution sol = (OSolution) arg0;

		if(sol.getState() == OSolution.NOTIFY_EQUITY)
		{
			double [] equities = (double[]) arg1;
			for(PlayerController p : this.alPlayerController)
				p.writeEquity(equities[p.hashCode()]);
		}

		else if(sol.getState() == OSolution.NOTIFY_SIM)
		{
			Platform.runLater(()->{
			    if((Integer)arg1 >= stopLimit){
			        equityProcessor.stopThreads();
			        disbledForSim(false);
                }
				String number = String.format("%,d", arg1);
				this._lbNumSimu.setText(NUM_SIMULATIONS_TEXT + number);
			});
		}

		else if (sol.getState() == OSolution.NOTIFY_PLAYER_CARDS)
		{
			OPlayerCards pc = (OPlayerCards)arg1;
			alPlayerController.get(pc.getNumPlayer()).setCards(pc.getCards().get(0), pc.getCards().get(1));
            try {
                equityProcessor.addPlayerCards(pc.getNumPlayer(), pc.getCards().toArray(new Card[pc.getCards().size()]));
            } catch (Exception e) {
                e.printStackTrace();
                writeTA(e.getMessage());
            }
        }

		else if( sol.getState() == OSolution.NOTIFY_FOLD)
		{
            try {
                remainPlayers--;
                equityProcessor.removePlayer((Integer)arg1);
            } catch (Exception e) {
                e.printStackTrace();
                writeTA(e.getMessage());
            }
        }
	}

    public void onClickStop(ActionEvent actionEvent) {
        equityProcessor.stopThreads();
        Platform.runLater(() -> disbledForSim(false));
    }
}
