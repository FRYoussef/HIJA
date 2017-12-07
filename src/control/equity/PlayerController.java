package control.equity;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.representation.Card;


public class PlayerController {

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

    private int numPlayer;
    private Card cd1 = null;
    private Card cd2 = null;

    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    public Card getCd1() {
        return cd1;
    }

    public Card getCd2() {
        return cd2;
    }

    public void onClickCard(MouseEvent mouseEvent) {
        //card selection
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

    @Override
    public int hashCode() {
        return numPlayer;
    }
}
