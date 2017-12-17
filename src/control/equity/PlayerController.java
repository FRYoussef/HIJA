package control.equity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import control.ObserverPatron.HandlerObserver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.representation.Card;
import model.representation.game.Deck;

public abstract class PlayerController {
	protected static final DecimalFormat DF = new DecimalFormat("#.###");
    protected static final String DEFAULT_EQUITY = "0%";
    protected static final String EQUITY = "Equity: ";
    protected static final String OUT_TEXT = "Out";
    
    @FXML
    protected Label _lbEquity;
    @FXML
    protected Button _btFold;
    @FXML
    protected AnchorPane _playerAnchor;
    @FXML
    protected Label _lbPlayer;
    
    @FXML
    protected GridPane _gpCardSet;
    
    protected int numPlayer;
    protected Deck deck = null;
    protected Stage stageCardsSelec = null;
    protected CardSelectorController cs;
   
    protected ArrayList<Card> cards;
    protected ImageView[] picCards; 
    
    public PlayerController(){

    }
    
    public void init(int numPlayer, ArrayList<Card> cards, Stage stageCardsSelec, Deck deck, CardSelectorController cs){
        this.numPlayer = numPlayer;
        writePlayer(numPlayer);
        this.cards = cards;
        this.stageCardsSelec = stageCardsSelec;
        this.deck = deck;
        this.cs = cs;
        init();
        
     }
    public int getNumPlayer() {
        return numPlayer;
    }

    public Card getCard(int i){
    	return cards.get(i);
    }

    public void setCards(ArrayList<Card> c){
    	if(c != null){
    		for (int i = 0; i < picCards.length; i++) {
				picCards[i].setImage(new Image("resources/cards/" + c.get(i).toString() 
						+ ".png"));
			}
    	}
    	else{
    		for (int i = 0; i < picCards.length; i++)
				picCards[i].setImage(null);
    	}
    }
    
    public void disableForSim(boolean b){
    	for (ImageView i : picCards)
    		i.setDisable(b);
        _btFold.setDisable(b);
    }
    
    public void clear(){
        Platform.runLater(() -> {
        	cards = null; 
            _lbEquity.setText(EQUITY+DEFAULT_EQUITY);
            _playerAnchor.setDisable(false);
            if(_btFold.isDisabled())
                _btFold.setDisable(false);
            for (ImageView i : picCards) 
				i.setImage(null);
        });
    }
    
    protected void writePlayer(int numPlayer){
    	Platform.runLater(() ->{
        	_lbPlayer.setText("Player " + numPlayer);
        });
    }
    
    @Override
    public int hashCode() {
        return numPlayer;
    }
    
    public void writeEquity(double equity){
    	Platform.runLater(()->{
    		String number = DF.format(equity*100.f);
    		_lbEquity.setText(EQUITY + number + '%');
    	});
    }
    
    public void onClickFold(ActionEvent actionEvent) {
        //notify the end of this player
        _playerAnchor.setDisable(true);
        _lbEquity.setText(OUT_TEXT);
        HandlerObserver.getoSolution().notifyFold(numPlayer);
    }
    
    public void onClickCard(MouseEvent mouseEvent) {
        //card selection
    	Platform.runLater(() -> {
            try {
                if(!stageCardsSelec.isShowing()) {
                    ArrayList<Card> c = null;
                    if(cards != null){
                        c = new ArrayList<>(cards.size());
                        for (int i = 0; i < cards.size(); i++)
							c.add(cards.get(i));
                    }
                    cs.update(numPlayer, picCards.length, c);
                    stageCardsSelec.show();
                }
             }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
    	 });
    }
    
    protected abstract void init();
}
