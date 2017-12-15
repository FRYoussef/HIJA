package control.equity;



import java.util.ArrayList;
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
    private Deck deck;
    

    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    public Card getCd1() {
        return cd1;
    }

    public Card getCd2() {
        return cd2;
    }
    
    public void setCd1(Card card) {
        this.cd1 = card;
        System.out.println(cd1.toString());
        _ivCard1.setImage(new Image("resources/cards/" + cd1.toString() + ".png")); 
    }

    public void setCd2(Card card) {
    	this.cd2 = card;
    	_ivCard2.setImage(new Image("resources/cards/" + cd2.toString() + ".png"));
    }

    public void onClickCard(MouseEvent mouseEvent) {
        //card selection
    	Platform.runLater(() -> {
    	try {
    	 ArrayList<Card> cards = new ArrayList<Card>();
    	 cards.add(cd1);
    	 cards.add(cd2);
    	 CardSelectorController cardSelector = new CardSelectorController(this.deck, cards, 2, this.numPlayer);
         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/cardSelector.fxml"));
         fxmlLoader.setController(cardSelector);
         Stage stage = new Stage();
         stage.setScene(new Scene(fxmlLoader.load()));
         stage.setTitle("Card Selector");
         stage.setResizable(false);
         stage.show();
    	 }
        catch (Exception e) {
            e.printStackTrace();
        }
    	 });
    }

    public void onClickFold(ActionEvent actionEvent) {
        //notify the end of this player

        _btFold.setDisable(true);
        _lbEquity.setText(OUT_TEXT);
    }

    public void clear(){
        Platform.runLater(() -> {
            _lbEquity.setText(EQUITY+DEFAULT_EQUITY);
            if(_btFold.isDisabled())
                _btFold.setDisable(false);
            _ivCard1.setImage(null);
            _ivCard2.setImage(null);
        });
    }
    
    public void setDeck(Deck deck){
    	this.deck = deck;
    }

    @Override
    public int hashCode() {
        return numPlayer;
    }
    
    private DecimalFormat df = new DecimalFormat("#.##########");
    public void writeEquity(double equity){
    	Platform.runLater(()->{
    		String number = df.format(equity*100.f);
    		_lbEquity.setText(EQUITY + System.lineSeparator() + number + '%');
    	});
    }

}
