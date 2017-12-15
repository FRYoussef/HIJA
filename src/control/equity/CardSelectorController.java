package control.equity;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.representation.Suit;
import model.representation.game.Deck;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import model.representation.Card;

public class CardSelectorController {
	
	private static final String SELECTED_CARD = "selectedCard";

	@FXML
    private GridPane _cardGrid;
	@FXML
    private Button _acceptButton;
	@FXML
    private Button _cancelButton;
	
    
    private ArrayList<Card> allCards = new ArrayList<Card>();//Auxiliar array used to maintain the original order of the cards
    private ArrayList<Card> selectedCards = new ArrayList<Card>();//Cards selected for the player
    private HashSet<String> hscardsSet = new HashSet<String>();//Id of the cards clicked
    private int cardsSelected = 0;//Cards already clicked
    private Deck deck;//The total deck, in order to know wich cards are already selected by other players
    private int numCards;//Total cards to select
    private int numPlayer;//Num of the player, -1 for the board

	
	 public CardSelectorController(Deck deck, ArrayList<Card> cards, int numCards, int numPlayer) throws Exception{
		 
		 this.deck = deck;	
		 this.numCards = numCards;
		 this.numPlayer = numPlayer;
		 
		 //to let the user change his own cards selected before
		 for(int i = 0; i < cards.size(); i++) {
			 deck.replaceCard(cards.get(i));
		 }
		 
		try{ 
		for (int i = 0; i < 4; i++) {
			 for (int j = 0; j < 13; j++) {
				 allCards.add(new Card(j,Suit.getFromInt(i)));
			    }
    }
		 Platform.runLater(() -> {
			 for (int i = 0; i < 4; i++) {
				 for (int j = 0; j < 13; j++) {
					 ImageView img = new ImageView("resources/cards/" + allCards.get(j+13*i).toString() + ".png"); 
					 img.setFitHeight(100);
					 img.setFitWidth(70);
					 img.setId(allCards.get(j+13*i).toString());
					 _cardGrid.add(img,j,i); 
					 if(deck.contains(allCards.get(j+13*i))){
					 img.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				     
					     @Override
					     public void handle(MouseEvent event) {
					    	ImageView img = ((ImageView)event.getSource());
					        String id = img.getId();
					        
					        if(hscardsSet.contains(id)){
					        	hscardsSet.remove(id);
					        	img.getStyleClass().clear();
					        	img.setEffect(null);
					        	cardsSelected--;
					        }
					        else if(cardsSelected < numCards){
					        	hscardsSet.add(id);
					        	ColorAdjust colorAdjust = new ColorAdjust();
					        	colorAdjust.setContrast(0.1);
					        	colorAdjust.setHue(-0.05);
					        	colorAdjust.setBrightness(0.1);
					        	colorAdjust.setSaturation(0.2);
					        	img.setEffect(colorAdjust);
					        	cardsSelected++;
					        }
					     }
					});
					 }else{
						 img.setDisable(true);
						 img.getStyleClass().add(SELECTED_CARD);
					 }
					
				    }
			 }
		 });
		}
		 catch (Exception e) {
	            e.printStackTrace();
	        } 
	 }
	
	public void onClickAccept(MouseEvent mouseEvent) {
		Platform.runLater(() -> {
	      
			char idSplit[] = new char[this.numCards];
			int i = 0;
			Iterator<String> iterator = hscardsSet.iterator();
        	while(iterator.hasNext()){
        	      String aux = iterator.next();
        	      idSplit = aux.toCharArray();
        	      aux.getChars(0, this.numCards, idSplit, 0);
        	      try {
					selectedCards.add(new Card((Card.charToValue(idSplit[0])),Suit.getFromChar(idSplit[1])));
					deck.removeCard(new Card((Card.charToValue(idSplit[0])),Suit.getFromChar(idSplit[1])));
				} catch (Exception e) {
					e.printStackTrace();
				}
        	      i++;
        	}
			PlayerObserver.getoSolution().notifySolution(selectedCards, this.deck, this.numPlayer);
	        Stage stage = (Stage)_acceptButton.getScene().getWindow();
	 		stage.close();
		});
	}
	    
	public void onClickCancel(MouseEvent mouseEvent) {
		Platform.runLater(() -> {
		Stage stage = (Stage)_cancelButton.getScene().getWindow();
		stage.close();
		});
    }

}
