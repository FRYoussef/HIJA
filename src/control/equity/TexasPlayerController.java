package control.equity;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class TexasPlayerController extends PlayerController{
	
	@FXML
	protected ImageView _ivCard1;
    @FXML
	protected ImageView _ivCard2;
    
    public TexasPlayerController(){
    	super(); 
     }

	@Override
	protected void init() {
		picCards = new ImageView[]{_ivCard1, _ivCard2};
		if(cards != null){
			for (int i = 0; i < picCards.length; i++) {
				picCards[i].setImage(new Image("resources/cards/" + cards.get(i).toString() 
						+ ".png"));
				_gpCardSet.getChildren().add(picCards[i]);
			}
		}
	}

}
