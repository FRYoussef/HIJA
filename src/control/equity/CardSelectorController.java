package control.equity;

import control.ObserverPatron.HandlerObserver;
import control.ObserverPatron.OPlayerCards;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.representation.Suit;
import model.representation.game.Deck;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import model.representation.Card;

public class CardSelectorController {
	
	private static final String SELECTED_CARD = "selected";

	@FXML
    private GridPane _cardGrid;
	@FXML
    private Button _acceptButton;
	@FXML
    private Button _cancelButton;
	
    
    private ArrayList<Card> allCards = new ArrayList<Card>();//Auxiliar array used to maintain the original order of the cards
    private ArrayList<Card> selectedCards = new ArrayList<Card>();//Cards selected for the player
    private HashSet<String> hsCardsSet = new HashSet<String>();//Id of the cards clicked
    private int cardsSelected = 0;//Cards already clicked
    private Deck deck;//The total deck, in order to know wich cards are already selected by other players
    private int numCards;//Total cards to select
    private int numPlayer;//Num of the player, -1 for the board
	private ArrayList<Card> cards;

	
	 public CardSelectorController(Deck deck, ArrayList<Card> cards, int numCards, int numPlayer) throws Exception{
		 
		 this.deck = deck;	
		 this.numCards = numCards;
		 this.numPlayer = numPlayer;
		 this.cards = cards;
		 
		try{ 
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 13; j++)
					allCards.add(new Card(j, Suit.getFromInt(i)));
			}
			 Platform.runLater(() -> {
				 for (int i = 0; i < 4; i++) {
					 for (int j = 0; j < 13; j++) {
						 AnchorPane pane = new AnchorPane();
						 ImageView img = new ImageView("resources/cards/" + allCards.get(i*13+j).toString() + ".png");
						 pane.setId(allCards.get(i*13+j).toString());
						 img.setFitHeight(110);
						 img.setFitWidth(67);
						 pane.getChildren().add(img);
						 AnchorPane.setTopAnchor(img, 5.0d);
						 AnchorPane.setRightAnchor(img, 5.0d);
						 AnchorPane.setBottomAnchor(img, 5.0d);
						 AnchorPane.setLeftAnchor(img, 5.0d);
						 _cardGrid.add(pane, j, i);

						 if(deck.contains(allCards.get(i*13+j)))
						 	pane.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickImg);

						 else {
							 pane.setDisable(true);
						 }
					 }
				 }
			 });
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	 }

	 private void onClickImg(MouseEvent mouseEvent){
		 AnchorPane pane = ((AnchorPane)mouseEvent.getSource());
		 String id = pane.getId();

		 if(hsCardsSet.contains(id)){
			 hsCardsSet.remove(id);
			 pane.getStyleClass().clear();
			 cardsSelected--;
		 }
		 else if(cardsSelected < numCards){
			 hsCardsSet.add(id);
			 pane.getStyleClass().add(SELECTED_CARD);
			 cardsSelected++;
		 }
	 }

	public void onClickAccept(MouseEvent mouseEvent) {
		Platform.runLater(() -> {
	      	if(cardsSelected != numCards)
	      		return;

        	for(String aux : hsCardsSet){
        	      try {
        	      	Card c = new Card((Card.charToValue(aux.charAt(0))),Suit.getFromChar(aux.charAt(1)));
					selectedCards.add(c);
					deck.removeCard(c);
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
			HandlerObserver.getoSolution().notifyPlayerCards(new OPlayerCards(selectedCards, this.numPlayer));
	        Stage stage = (Stage)_acceptButton.getScene().getWindow();
	 		stage.close();
		});
	}
	    
	public void onClickCancel(MouseEvent mouseEvent) {
		Platform.runLater(() -> {
			clear();
			Stage stage = (Stage)_cancelButton.getScene().getWindow();
			stage.close();
		});
    }

	private void clear(){
		for (String s : hsCardsSet) {
			Card c = null;
			try {
				c = new Card(Card.charToValue(s.charAt(0)), Suit.getFromChar(s.charAt(1)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			_cardGrid.getChildren().get(c.getSuit().ordinal() *Card.NUM_CARDS+ c.getValue()).getStyleClass().clear();
		}
		hsCardsSet.clear();
		selectedCards.clear();
		cardsSelected = 0;
	}

	private void selectPlayerCards(ArrayList<Card> cards){
	 	if(cards != null){
			for (Card c: cards){
				_cardGrid.getChildren().get(c.getSuit().ordinal()*Card.NUM_CARDS+c.getValue()).getStyleClass().clear();
				_cardGrid.getChildren().get(c.getSuit().ordinal()*Card.NUM_CARDS+c.getValue()).getStyleClass().add(SELECTED_CARD);
				_cardGrid.getChildren().get(c.getSuit().ordinal()*Card.NUM_CARDS+c.getValue()).setDisable(false);
				_cardGrid.getChildren().get(c.getSuit().ordinal()*Card.NUM_CARDS+c.getValue()).setEffect(null);
				hsCardsSet.add(allCards.get(c.getSuit().ordinal() * Card.NUM_CARDS + c.getValue()) + "");
				cardsSelected++;
			}
		}
	}

	private void outCards() throws Exception {
		for (int i = 0; i < Suit.NUM_SUIT; i++) {
			for (int j = 0; j < Card.NUM_CARDS; j++) {
				if(!deck.contains(new Card(j, Suit.getFromInt(i)))){
					_cardGrid.getChildren().get(i*Card.NUM_CARDS+j).setDisable(true);
				}
				else {
					_cardGrid.getChildren().get(i * Card.NUM_CARDS + j).setDisable(false);
					_cardGrid.getChildren().get(i*Card.NUM_CARDS+j).getStyleClass().clear();
				}
			}
		}
	}

	public void update(int numPlayer, int numCards, ArrayList<Card> cards){
		Platform.runLater(() -> {
			this.numPlayer = numPlayer;
			this.cards = cards;
			this.numCards = numCards;
			try {
				clear();
				outCards();
				selectPlayerCards(this.cards);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
