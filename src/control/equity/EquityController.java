package control.equity;

import control.ObserverPatron.OPlayerCards;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Ellipse;

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

	private static final String[] GAME_MODES_TEXT = {"Texas hold'em" , "Omaha"}; 
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
    private static final int DEFAULT_STOP_LIMIT = 0;

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
    @FXML
    private ComboBox<String> _cbGameMode;
    
    private ArrayList<PlayerController> alPlayerController = null;
    private int numPlayers;
    private int remainPlayers;
    private int phase = 0;
    private EquityProcessor equityProcessor;
    private Stage stageCardsSelec = null;
    private CardSelectorController selectorController = null;
    private int stopLimit = DEFAULT_STOP_LIMIT;


    public EquityController() {
        numPlayers = 6;
        remainPlayers = numPlayers;
        alPlayerController = new ArrayList<PlayerController>(numPlayers);
	    equityProcessor = new EquityProcessor(MAX_PLAYERS_HE);
        initializeCBGameMode();
	    addPlayers();
	    createStageSelector();
        HandlerObserver.init();
        HandlerObserver.addObserver(this);
    }

    private void initializeCBGameMode(){
    	Platform.runLater(() ->{
    		_cbGameMode.getItems().removeAll(_cbGameMode.getItems());
        	_cbGameMode.getItems().addAll(GAME_MODES_TEXT);
        	_cbGameMode.getSelectionModel().select(0);
        	_cbGameMode.valueProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!oldValue.equals(newValue)){
				        _pBoard.getChildren().remove(_pBoard.getChildren().size()- alPlayerController.size(), _pBoard.getChildren().size());
				        alPlayerController.clear();
				        equityProcessor.removeAllPlayers();
				        if(newValue.equals(GAME_MODES_TEXT[0]))
				        	selectorController.setNumCards(HE_NUM_CARDS);
				        else if(newValue.equals(GAME_MODES_TEXT[1])){
				        	if(numPlayers > 6){
				        		numPlayers = 6; 
				        		_mbtNumPlayers.setText(NUM_PLAYERS_TEXT + MAX_PLAYERS_OMAHA);
				        	}
				        	selectorController.setNumCards(OMAHA_NUM_CARDS);
				        }
				        addPlayers();
					}
				}  
            });
    	});
    	
    }
    private void createStageSelector(){
        try {
            selectorController = new CardSelectorController(equityProcessor.getDeck(), null,
                    HE_NUM_CARDS, -1);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/cardSelector.fxml"));
            fxmlLoader.setController(selectorController);
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
            AnchorPane root = null;
            if(_cbGameMode.getValue().equals(GAME_MODES_TEXT[0]))
            	root = fxmlLoader.load(getClass().getResource("../../view/TexasPlayerPane.fxml").openStream());
            else if(_cbGameMode.getValue().equals(GAME_MODES_TEXT[1]))
            	root = fxmlLoader.load(getClass().getResource("../../view/OmahaPlayerPane.fxml").openStream());
            double x = _eBoard.getRadiusX()*Math.sin(angleT) + _pBoard.getWidth()/2 - root.getPrefWidth()/1.8*(Math.sin(angleT)) - 30;
        	double y = _eBoard.getRadiusY()*Math.cos(angleT) + _pBoard.getHeight()/2 - root.getPrefHeight()/2*(1 + Math.pow(Math.cos(angleT), 3)/1.2) - 10;
            root.setLayoutX(x);
            root.setLayoutY(y);
            _pBoard.getChildren().add(root);
            ((PlayerController) fxmlLoader.getController()).init(player, null, stageCardsSelec, equityProcessor.getDeck(), selectorController);
            alPlayerController.add(fxmlLoader.getController());
            equityProcessor.addPlayer(new Player(player));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickNumPlayers(ActionEvent actionEvent) {
    	int numPlayersSelected = Integer.parseInt(((MenuItem)actionEvent.getSource()).getText());
    	if(evaluateNumPlayers(numPlayersSelected)){
        numPlayers = Integer.parseInt(((MenuItem)actionEvent.getSource()).getText());
        remainPlayers = numPlayers;
        _mbtNumPlayers.setText(NUM_PLAYERS_TEXT + numPlayers);
        _pBoard.getChildren().remove(_pBoard.getChildren().size()- alPlayerController.size(), _pBoard.getChildren().size());
        alPlayerController.clear();
        equityProcessor.removeAllPlayers();
        addPlayers();
    	}
    }

    private boolean evaluateNumPlayers(int numPlayersSelected){
    	if(_cbGameMode.getValue().equals(GAME_MODES_TEXT[1]) && numPlayersSelected > 6){
    		_mbtNumPlayers.setText(NUM_PLAYERS_TEXT + numPlayers);
    		writeTA("Maximum number of players in " + _cbGameMode.getValue() + " is 6 players.");
    		return false;
    	}
    	else
    		return true; 
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
            for (int i = equityProcessor.numCardsBoard(); i < finalRemainCards; i++)
                equityProcessor.getRandomBoardCard();
            addBoardCards(equityProcessor.getBoardCards());
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
            if(remainPlayers == 0)
                return;
            if (!evaluatePhase())
                return;
            if(remainPlayers == 1){
                //only 1 execution
                for(Integer i : equityProcessor.getHmPlayer().keySet())
                    alPlayerController.get(i).writeEquity(1d);
                return;
            }
            if(phase == PHASE_RIVER){
                equityProcessor.calculateFinalEquity(numPlayers);
                return;
            }

            try{
                if(!_tfStopLimit.getText().equals(""))
                    stopLimit = Integer.parseInt(_tfStopLimit.getText());
            }catch (Exception e){
                e.printStackTrace();
                writeTA("Please, write a correct number");
            }
            disbledForSim(true);
            equityProcessor.calculateEquity(numPlayers);
        });
    }

    private void disbledForSim(boolean b){
        _btCalculate.setDisable(b);
        _btPhase.setDisable(b);
        _btStop.setDisable(!b);
        _hbBoardCards.setDisable(b);
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
            _taLog.clear();
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
            for(PlayerController c : alPlayerController) {
                try {
                    equityProcessor.addPlayer(new Player(c.getNumPlayer()));
                } catch (Exception e) {
                    e.printStackTrace();
                    writeTA(e.getMessage());
                }
            }
            equityProcessor.clearBoard();
        });
    }

    private void writeTA(String text){
        Platform.runLater(() -> _taLog.appendText(text + "\n"));
    }

    private void addBoardCards(ArrayList<Card> cards){
        Platform.runLater(() -> {
            if(cards == null)
                return;
            for (int i = 0; i < cards.size(); i++)
                ((ImageView)_hbBoardCards.getChildren().get(i)).setImage(new Image("resources/cards/" + cards.get(i) + ".png"));
        });
    }

	@Override
	 /**
     * It receives the notify with the solution of the CardSelector, which includes
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
			    if((Integer)arg1 >= stopLimit && stopLimit > 0){
			        equityProcessor.stopThreads();
			        disbledForSim(false);
                }
				String number = String.format("%,d", arg1);
				this._lbNumSimu.setText(NUM_SIMULATIONS_TEXT + number);
			});
		}

		else if (sol.getState() == OSolution.NOTIFY_PLAYER_CARDS)
		{
            try {
                OPlayerCards pc = (OPlayerCards)arg1;
                if(pc.getNumPlayer() != -1)
                {
                    alPlayerController.get(pc.getNumPlayer()).setCards(pc.getCards());
                    equityProcessor.addPlayerCards(pc.getNumPlayer(), pc.getCards().toArray(new Card[pc.getCards().size()]));
                }
                else
                {
                    equityProcessor.addBoardCards(pc.getCards());
                    addBoardCards(equityProcessor.getBoardCards());
                }
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

    public void onClickBoardCards(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            int remainC = 0;

            if(phase == PHASE_PREFLOP)    return;
            else if(phase == PHASE_FLOP)  remainC = 3;
            else if(phase == PHASE_TURN)  remainC = 4;
            else if(phase == PHASE_RIVER) remainC = 5;

            if(remainC > 0 && !stageCardsSelec.isShowing()){
                selectorController.update(-1, remainC, equityProcessor.getBoardCards());
                stageCardsSelec.show();
            }
        });
    }
}
