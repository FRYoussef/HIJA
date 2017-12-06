package control.board;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.game.*;
import model.representation.Card;
import model.representation.game.Play;

import java.util.Observable;
import java.util.Observer;


public class BoardController implements Observer {

    /** Control buttons */
    @FXML
    private TextField _tfGameNumber;
    @FXML
    private TextField _tfInputFile;
    @FXML
    private Button _btRun;

    /** Log controls */
    @FXML
    private TextArea _taLog;

    /** Cards view */
    @FXML
    private HBox _hbCards;

    /** board buttons */
    @FXML
    private Pane _pBoardButtons;

    private static final int MAX_TA_LENGTH = 8000;
    private boolean enableControlPane = true;
    private Game game;
    private Thread thGameRunner = null;

    public BoardController() {
        HandlerObserver.init();
        HandlerObserver.addObserver(this);
    }

    public Game getGame() {
        return game;
    }

    /**
     * Check if the args are correct, then run the game
     * @param actionEvent
     */
    @FXML
    public void onClickRun(ActionEvent actionEvent) {
        int game;
        try{
            game = Integer.parseInt(_tfGameNumber.getText());
        } catch (Exception e) {
            _tfGameNumber.clear();
            writeTextArea("You must to introduce a number");
            return;
        }

        _tfGameNumber.clear();
        if(game < 1 || game > 4){
            writeTextArea("Wrong game\n");
            return;
        }
        try
        {
            if(game == 1) this.game = new Cards5(_tfInputFile.getText());
            else if(game == 2) this.game = new Player1HE(_tfInputFile.getText());
            else if (game == 3) this.game = new NPlayerHE(_tfInputFile.getText());
            else this.game = new Omaha(_tfInputFile.getText());
        }
        catch (Exception e){
            writeTextArea("Input file error\n");
            _tfInputFile.clear();
            return;
        }
        _tfInputFile.clear();
        writeTextArea("Loading...\n");
        _taLog.clear();
        clearCards();
        enableControlPane();
        enablePlayerButtons(0);
        enableBoardButton(false);

        thGameRunner = new Thread(() -> {
            try
            {
                getGame().startGame();
                enableControlPane();
                enablePlayerButtons(getGame().getNumberPlayers());
                enableBoardButton(true);
            }
            catch (Exception e) {
                writeTextArea(e.getMessage() + "\n");
                e.printStackTrace();
            }
        });
        thGameRunner.setDaemon(true);
        thGameRunner.start();
    }

    /**
     * it is show the cards of the players or board
     * @param actionEvent
     */
    @FXML
    public void onClickPlayer(ActionEvent actionEvent) {
        Card c [];
        if((((Button)actionEvent.getSource()).getId()).equals("_btBoard"))
            c = game.getBoardCards();
        else
            c = game.getPlayerCards(idToInteger(((Button)actionEvent.getSource()).getId()));

        for (int i = 0; i < Play.CARDS_PER_PLAY; i++) {
            if(i < c.length) {
                ((ImageView) _hbCards.getChildren().get(i)).setImage(
                        new Image("resources/cards/" + c[i].toString() + ".png"));
            }
            else
                ((ImageView)_hbCards.getChildren().get(i)).setImage(null);
        }
    }

    private void clearCards(){
        Platform.runLater(() -> {
            for (int i = 0; i < Play.CARDS_PER_PLAY; i++) {
                ((ImageView)_hbCards.getChildren().get(i)).setImage(null);
            }
        });
    }

    private int idToInteger(String id){
        int player;
        switch (id){
            case "_btPlayer1":
                player = 1;
                break;
            case "_btPlayer2":
                player = 2;
                break;
            case "_btPlayer3":
                player = 3;
                break;
            case "_btPlayer4":
                player = 4;
                break;
            case "_btPlayer5":
                player = 5;
                break;
            case "_btPlayer6":
                player = 6;
                break;
            case "_btPlayer7":
                player = 7;
                break;
            case "_btPlayer8":
                player = 8;
                break;
            case "_btPlayer9":
                player = 9;
                break;
            default:
                player = -1;
                break;
        }
        return player;
    }

    /**
     * Enable or disabled the elements of control pane
     */
    private void enableControlPane(){
        Platform.runLater(() -> {

            if(enableControlPane){
                _tfGameNumber.setDisable(true);
                _tfInputFile.setDisable(true);
                _btRun.setDisable(true);
                enableControlPane = !enableControlPane;
            }
            else{
                _tfGameNumber.setDisable(false);
                _tfInputFile.setDisable(false);
                _btRun.setDisable(false);
                enableControlPane = !enableControlPane;
            }
        });
    }

    /**
     * Enable a number of buttons and disabled the remains
     * @param num number of buttons enabled
     */
    private void enablePlayerButtons(int num){
        Platform.runLater(() -> {
            ObservableList<Node> bts = _pBoardButtons.getChildren();

            for (int i = 0; i < 9; i++) {
                if(i < num)
                    bts.get(i).setDisable(false);
                else
                    bts.get(i).setDisable(true);
            }
        });
    }

    /**
     * Enable and disabled the board button
     */
    private void enableBoardButton(boolean b){
        Platform.runLater(() -> {
            if(b)
                _pBoardButtons.getChildren().get(9).setDisable(false);
            else
                _pBoardButtons.getChildren().get(9).setDisable(true);
        });
    }

    /**
     * Writes a text
     * @param text
     */
    private void writeTextArea(String text){
        if(_taLog.getText().length() >= MAX_TA_LENGTH)
            _taLog.clear();

        Platform.runLater(() -> _taLog.appendText(text));
    }

    @Override
    public void update(Observable o, Object arg) {
        writeTextArea((String)arg);
    }
}
