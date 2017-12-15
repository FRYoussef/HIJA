package control.equity;



import java.text.DecimalFormat;
import java.util.ArrayList;

import control.ObserverPatron.HandlerObserver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.representation.Card;
import model.representation.game.Deck;


public class PlayerController{

    private static final DecimalFormat DF = new DecimalFormat("#.###");
    private static final String DEFAULT_EQUITY = "0%";
    private static final String EQUITY = "Equity: ";
    private static final String OUT_TEXT = "Out";

    @FXML
    private ImageView _ivCard1;
    @FXML
    private ImageView _ivCard2;
    @FXML
    private Label _lbEquity;
    @FXML
    private Button _btFold;
    @FXML
    private AnchorPane _playerAnchor;

    private int numPlayer;
    private Card cd1 = null;
    private Card cd2 = null;
    private Deck deck = null;
    private Stage stageCardsSelec = null;
    private CardSelectorController cs;
    

    public void init(int numPlayer, Card card1, Card card2, Stage stageCardsSelec, Deck deck, CardSelectorController cs){
        this.numPlayer = numPlayer;
        this.cd1 = card1;
        this.cd2 = card2;
        this.stageCardsSelec = stageCardsSelec;
        this.deck = deck;
        this.cs = cs;
        if(cd1 != null)
            _ivCard1.setImage(new Image("resources/cards/" + cd1.toString() + ".png"));
        if(cd2 != null)
            _ivCard2.setImage(new Image("resources/cards/" + cd2.toString() + ".png"));
    }

    public Card getCd1() {
        return cd1;
    }

    public Card getCd2() {
        return cd2;
    }

    public void setCards(Card card1, Card card2){
        this.cd1 = card1;
        this.cd2 = card2;

        if(cd1 != null) _ivCard1.setImage(new Image("resources/cards/" + cd1.toString() + ".png"));
        else            _ivCard1.setImage(null);

        if(cd2 != null) _ivCard2.setImage(new Image("resources/cards/" + cd2.toString() + ".png"));
        else            _ivCard2.setImage(null);
    }

    public void onClickCard(MouseEvent mouseEvent) {
        //card selection
    	Platform.runLater(() -> {
            try {
                if(!stageCardsSelec.isShowing()) {
                    ArrayList<Card> cards = null;
                    if(cd1 != null){
                        cards = new ArrayList<>(2);
                        cards.add(cd1);
                        cards.add(cd2);
                    }
                    cs.update(numPlayer, cards);
                    stageCardsSelec.show();
                }
             }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
    	 });
    }

    public void onClickFold(ActionEvent actionEvent) {
        //notify the end of this player
        _playerAnchor.setDisable(true);
        _lbEquity.setText(OUT_TEXT);
        HandlerObserver.getoSolution().notifyFold(numPlayer);
    }

    public void clear(){
        Platform.runLater(() -> {
            cd1 = null;
            cd2 = null;
            _lbEquity.setText(EQUITY+DEFAULT_EQUITY);
            _playerAnchor.setDisable(false);
            if(_btFold.isDisabled())
                _btFold.setDisable(false);
            _ivCard1.setImage(null);
            _ivCard2.setImage(null);
        });
    }

    @Override
    public int hashCode() {
        return numPlayer;
    }

    public void disableForSim(boolean b){
        _ivCard1.setDisable(b);
        _ivCard2.setDisable(b);
        _btFold.setDisable(b);
    }

    public void writeEquity(double equity){
    	Platform.runLater(()->{
    		String number = DF.format(equity*100.f);
    		_lbEquity.setText(EQUITY + number + '%');
    	});
    }

}
