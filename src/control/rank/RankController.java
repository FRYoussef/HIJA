package control.rank;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.representation.rank.Rank;

import java.util.HashSet;

public class RankController {
    private static final int NUM_ROW = 13;
    private static final int NUM_COL = 13;
    private static final String PAIR_CELL = "pairCell";
    private static final String SUIT_CELL = "suitedCell";
    private static final String OFFSUIT_CELL = "offSuitedCell";
    private static final String SELECTED_CELL = "selectedCell";
    private static final int MAX_TA_LENGTH = 8000;
    private static final String SEPARATOR = "\n----------------------------------\n";

    @FXML
    private GridPane _gpCouples;
    @FXML
    private TextArea _taLog;
    @FXML
    private Button _btBroadway;
    @FXML
    private Button _btSuited;
    @FXML
    private Button _btPairs;
    @FXML
    private Button _btAll;

    private HashSet<String> hsCouples = null;

    public RankController() {
        drawColorCells();
        hsCouples = new HashSet<>();
    }

    /**
     * It draws the color of all cells
     */
    private void drawColorCells(){
        Platform.runLater(() -> {
            for (int i = 0; i < NUM_ROW; i++) {
                for (int j = 0; j < NUM_COL; j++) {
                    _gpCouples.getChildren().get(i*NUM_ROW+j).getStyleClass().clear();
                    if( i == j)
                        _gpCouples.getChildren().get(i*NUM_ROW+j).getStyleClass().add(PAIR_CELL);
                    else if( i > j)
                        _gpCouples.getChildren().get(i*NUM_ROW+j).getStyleClass().add(OFFSUIT_CELL);
                    else if( i < j)
                        _gpCouples.getChildren().get(i*NUM_ROW+j).getStyleClass().add(SUIT_CELL);
                }
            }
        });
    }

    @FXML
    public void onClickCouple(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            Node source = (Node)mouseEvent.getSource() ;
            Integer c = GridPane.getColumnIndex(source);
            Integer r = GridPane.getRowIndex(source);
            String text = ((Label)_gpCouples.getChildren().get(r*NUM_ROW+c)).getText();
            _gpCouples.getChildren().get(r*NUM_ROW+c).getStyleClass().clear();
            if(hsCouples.contains(text)){
                hsCouples.remove(text);
                if(r == c)
                    _gpCouples.getChildren().get(r*NUM_ROW+c).getStyleClass().add(PAIR_CELL);
                else if(r > c)
                    _gpCouples.getChildren().get(r*NUM_ROW+c).getStyleClass().add(OFFSUIT_CELL);
                else if(r < c)
                    _gpCouples.getChildren().get(r*NUM_ROW+c).getStyleClass().add(SUIT_CELL);
            }
            else {
                _gpCouples.getChildren().get(r*NUM_ROW+c).getStyleClass().add(SELECTED_CELL);
                hsCouples.add(text);
            }
        });
    }

    @FXML
    public void onClickSelectAll(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            for (int i = 0; i < NUM_ROW; i++) {
                for (int j = 0; j < NUM_COL; j++) {
                    _gpCouples.getChildren().get(i*NUM_ROW+j).getStyleClass().clear();
                    _gpCouples.getChildren().get(i*NUM_ROW+j).getStyleClass().add(SELECTED_CELL);
                    hsCouples.add(((Label)_gpCouples.getChildren().get(i*NUM_ROW+j)).getText());
                }
            }
            _btAll.setDisable(true);
            _btBroadway.setDisable(true);
            _btPairs.setDisable(true);
            _btSuited.setDisable(true);
        });
    }

    @FXML
    public void onClickClearAll(ActionEvent actionEvent) {
        hsCouples.clear();
        drawColorCells();
        _btAll.setDisable(false);
        _btBroadway.setDisable(false);
        _btPairs.setDisable(false);
        _btSuited.setDisable(false);
    }

    @FXML
    public void onClickGenerate(ActionEvent actionEvent) {
        if(hsCouples.size() > 0)
            writeTextArea(Rank.getRanks(hsCouples.toArray(new String[hsCouples.size()])) + SEPARATOR);
        else
            writeTextArea("You have not selected any items, please choose one.\n\n");
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

    public void onClickSuited(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            for (int i = 0; i < NUM_ROW; i++) {
                for (int j = i+1; j < NUM_COL; j++) {
                    _gpCouples.getChildren().get(i*NUM_ROW+j).getStyleClass().clear();
                    _gpCouples.getChildren().get(i*NUM_ROW+j).getStyleClass().add(SELECTED_CELL);
                    hsCouples.add(((Label)_gpCouples.getChildren().get(i*NUM_ROW+j)).getText());
                }
            }
            _btSuited.setDisable(true);
            if(hsCouples.size() == NUM_COL*NUM_ROW)
                _btAll.setDisable(true);
        });
    }

    public void onClickBroadway(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            for (int i = 0; i < NUM_ROW; i++) {
                for (int j = 0; j < i; j++) {
                    _gpCouples.getChildren().get(i*NUM_ROW+j).getStyleClass().clear();
                    _gpCouples.getChildren().get(i*NUM_ROW+j).getStyleClass().add(SELECTED_CELL);
                    hsCouples.add(((Label)_gpCouples.getChildren().get(i*NUM_ROW+j)).getText());
                }
            }
            _btBroadway.setDisable(true);
            if(hsCouples.size() == NUM_COL*NUM_ROW)
                _btAll.setDisable(true);
        });
    }

    public void onClickPairs(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            for (int j = 0; j < NUM_COL; j++) {
                _gpCouples.getChildren().get(j*NUM_ROW+j).getStyleClass().clear();
                _gpCouples.getChildren().get(j*NUM_ROW+j).getStyleClass().add(SELECTED_CELL);
                hsCouples.add(((Label)_gpCouples.getChildren().get(j*NUM_ROW+j)).getText());
            }
            _btPairs.setDisable(true);
            if(hsCouples.size() == NUM_COL*NUM_ROW)
                _btAll.setDisable(true);
        });
    }
}
