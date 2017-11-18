package control.rank;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.processor.RangeProcessor;
import model.representation.Card;
import model.representation.Suit;
import model.representation.game.Play;
import model.representation.rank.CoupleCards;
import model.representation.rank.Range;

import java.util.ArrayList;
import java.util.HashSet;

public class RangeController {
    private static final int NUM_ROW = 13;
    private static final int NUM_COL = 13;
    private static final String PAIR_CELL = "pairCell";
    private static final String SUIT_CELL = "suitedCell";
    private static final String OFFSUIT_CELL = "offSuitedCell";
    private static final String SELECTED_CELL = "selectedCell";
    private static final String ERROR_STYLE = "error";
    private static final String HEARTS = "hearts";
    private static final String SELECTED_HEARTS = "heartsSelected";
    private static final String CLUBS = "clubs";
    private static final String SELECTED_CLUBS = "clubsSelected";
    private static final String DIAMONDS = "diamonds";
    private static final String SELECTED_DIAMONDS = "diamondsSelected";
    private static final String SPADES = "spades";
    private static final String SELECTED_SPADES = "spadesSelected";
    private static final int MAX_TA_LENGTH = 8000;
    private static final String SEPARATOR = System.getProperty("line.separator") +
            "----------------------------------------------" + System.getProperty("line.separator");

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
    @FXML
    private Slider _slider;
    @FXML
    private TextField _tfRang;
    @FXML
    private TextField _handDistributionText;

    private HashSet<String> hsCouples = null;
    private ArrayList<String> hsCards = null;
    private RangeProcessor rP = null;
    
    public RangeController() {
        drawColorCells();
        hsCouples = new HashSet<>();
        hsCards = new ArrayList<>(Play.CARDS_PER_PLAY);
    }

    private void showRange(){
        try{
            int val = Integer.valueOf(_tfRang.getText().split("%")[0]);

            if(val < 0 || val > 100)
                throw new NumberFormatException("Not in range");

            _tfRang.getStyleClass().clear();
            _tfRang.getStyleClass().addAll("text-field", "text-input");
            _tfRang.setText(val + "%");
            _slider.setValue(val);

            drawColorCells();
            selectElemsMatrix(CoupleCards.coupleCardsToMatrix(Range.rangeToCoupleCards(Range.getRangeArraySklansky(val))));
        }catch (Exception e1){
            _tfRang.getStyleClass().add(ERROR_STYLE);
            e1.printStackTrace();
        }
    }

    @FXML
    private void onMouseReleased(MouseEvent mouseEvent){
        Platform.runLater(() -> {
            _tfRang.setText(String.valueOf((int) _slider.getValue()) + "%");
            showRange();
        });
    }

    @FXML
    private void onActionRangeText(ActionEvent actionEvent){
        Platform.runLater(() -> showRange());
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

    private void selectCard(Label lb, String s1, String s2){
        if(!hsCards.contains(lb.getText()) && hsCards.size() == Play.CARDS_PER_PLAY)
            return;

        else if(hsCards.contains(lb.getText())){
            hsCards.remove(lb.getText());
            lb.getStyleClass().clear();
            lb.getStyleClass().add(s1);
        }
        else{
            hsCards.add(lb.getText());
            lb.getStyleClass().clear();
            lb.getStyleClass().add(s2);
        }
    }

    @FXML
    public void onClickStats(ActionEvent actionEvent) {
        if(hsCards.size() < 3){
            writeTextArea("Please select more cards from the board" + SEPARATOR);
            return;
        }
        if (hsCouples.size() == 0){
            writeTextArea("Please select a range" + SEPARATOR);
            return;
        }
        try{
            HashSet<Card> hsC = new HashSet<>(hsCards.size());
            for (String st: hsCards)
                hsC.add(new Card(Card.charToValue(st.charAt(0)), Suit.getFromChar(st.charAt(1))));

            rP = new RangeProcessor(hsC, CoupleCards.toCoupleCards(hsCouples));

            Thread th = new Thread(() -> {
                try {
                    rP.run();
                    writeTextArea(rP.toString() + SEPARATOR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            th.setDaemon(true);
            th.start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickCardH(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            Label lb = (Label)mouseEvent.getSource();
            selectCard(lb, HEARTS, SELECTED_HEARTS);
        });
    }

    @FXML
    public void onClickCardC(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            Label lb = (Label)mouseEvent.getSource();
            selectCard(lb, CLUBS, SELECTED_CLUBS);
        });
    }

    @FXML
    public void onClickCardD(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            Label lb = (Label)mouseEvent.getSource();
            selectCard(lb, DIAMONDS, SELECTED_DIAMONDS);
        });
    }

    @FXML
    public void onClickCardS(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            Label lb = (Label)mouseEvent.getSource();
            selectCard(lb, SPADES, SELECTED_SPADES);
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
            updatePercentage(hsCouples.size());
        });
    }

    /**
     * it clean, hashset enable the buttons and draw the rankind default
     */
    private void clearRange(){
        hsCouples.clear();
        drawColorCells();
        _btAll.setDisable(false);
        _btBroadway.setDisable(false);
        _btPairs.setDisable(false);
        _btSuited.setDisable(false);
        updatePercentage(hsCouples.size());
    }

    @FXML
    public void onClickClearAll(ActionEvent actionEvent) {
        Platform.runLater(() -> clearRange());
    }

    @FXML
    public void onClickGenerate(ActionEvent actionEvent) {
        if(hsCouples.size() > 0)
            writeTextArea(Range.getRanks(hsCouples.toArray(new String[hsCouples.size()])) + SEPARATOR);
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
            updatePercentage(hsCouples.size());
        });
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
            updatePercentage(hsCouples.size());
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
            updatePercentage(hsCouples.size());
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
            updatePercentage(hsCouples.size());
        });
    }

    @FXML
    void onClickShowRank(ActionEvent event) {
    	String entry = this._handDistributionText.getText();
    	_handDistributionText.clear();
    	if(entry.isEmpty())
    		writeTextArea("-You haven't typed in a rank." + SEPARATOR);
    	else{
	    	EntryParser parser = new EntryParser(entry);
	    	if(!parser.parseEntry())
	    		writeTextArea("It is not a correct rank" + SEPARATOR);
	    	else{
                clearRange();
                selectElemsMatrix(CoupleCards.coupleCardsToMatrix(Range.rangeToCoupleCards(parser.getRangeEntry())));
                Platform.runLater(() -> updatePercentage(hsCouples.size()));
            }
    	}
    }

    /**
     * It select the elements in the grid
     * @param pairs
     */
    private void selectElemsMatrix(ArrayList<Pair<Integer, Integer>> pairs){
        Platform.runLater(() -> {
            for (Pair<Integer, Integer> p : pairs) {
                _gpCouples.getChildren().get(p.getKey() * NUM_ROW + p.getValue()).getStyleClass().clear();
                _gpCouples.getChildren().get(p.getKey() * NUM_ROW + p.getValue()).getStyleClass().add(SELECTED_CELL);
                hsCouples.add(((Label) _gpCouples.getChildren().get(p.getKey() * NUM_ROW + p.getValue())).getText());
            }
        });
    }

    private void updatePercentage(int num){
        _tfRang.setText( (int)Math.floor((num*100) / CoupleCards.NUM_COUPLE_CARDS) + "%");
    }


//    /**
//     * Receives ranks set and it paints them on screen one by one .
//     * @param ranks
//     */
//    private void paintRanks(ArrayList<Range> ranks){
//    	class PaintRanks implements Runnable {
//    		ArrayList<Range> ranks;
//    		PaintRanks(ArrayList<Range> r){
//    			this.ranks = r;
//    		}
//			@Override
//			public void run() {
//				for(Range r: this.ranks){
//					//couple of cards rank
//					if(r.getCoupleCards2() == null && !r.isHighRank() && !r.isRank())
//						RangeController.this.paintCouple(r);
//					//plus rank
//					else if(r.getCoupleCards2() == null && r.isHighRank())
//						RangeController.this.paintPlus(r);
//					//hyphen rank
//					else
//						RangeController.this.paintHyphen(r);
//				}
//			}
//
//    	}
//    	Platform.runLater(new PaintRanks(ranks));
//    }
//    /**
//     * It paints hyphen format rank.
//     * @param r
//     */
//    private void paintHyphen(Range r){
//    	int pivot = r.getCoupleCards1().getHiggerValue();
//    	int limit = r.getCoupleCards1().getLowerValue();
//    	int second = r.getCoupleCards2().getLowerValue();
//    	while(second <= limit){
//    		this.paint(pivot, second, r.getCoupleCards1().isOffSuited());
//    		second++;
//    	}
//    }
//    /**
//     * It paints plus format rank.
//     * @param r
//     */
//    private void paintPlus(Range r){
//    	int first = r.getCoupleCards1().getHiggerValue();
//    	int second = r.getCoupleCards1().getLowerValue();
//    	boolean suit = r.getCoupleCards1().isOffSuited();
//    	//caso pareja
//    	if(first == second){
//    		while(first <= (Card.NUM_CARDS - 1)){
//    			this.paint(first, second, suit);
//    			second++;
//    			first++;
//    		}
//    	}
//    	else{
//	    	while(second < first){
//	    		this.paint(first, second, suit);
//	    		second++;
//	    	}
//    	}
//    }
//    /**
//     * It paints a couple of cards on screen.
//     * @param r
//     */
//    private void paintCouple(Range r){
//    	int first = r.getCoupleCards1().getHiggerValue();
//		int second = r.getCoupleCards1().getLowerValue();
//		this.paint(first, second, r.getCoupleCards1().isOffSuited());
//    }
//    /**
//     * It paints a GridPane's cell
//     * @param first high card value
//     * @param second low card value
//     * @param suit true if cards are suited cards, false otherwise
//     */
//    private void paint(int first, int second, boolean suit){
//    	int row, col;
//		if(first == second){
//			row = col = Math.abs(first - (Card.NUM_CARDS - 1));
//			_gpCouples.getChildren().get(row*NUM_ROW+col).getStyleClass().add(SELECTED_CELL);
//		}
//		else{
//			if(!suit){
//				row = Math.abs(first - (Card.NUM_CARDS - 1));
//				col = row + Math.abs(first - second);
//			}
//			else{
//				row = Math.abs(first - (Card.NUM_CARDS - 1)) + (first - second);
//				col = Math.abs(first - (Card.NUM_CARDS - 1));
//			}
//			_gpCouples.getChildren().get(row*NUM_ROW+col).getStyleClass().add(SELECTED_CELL);
//		}
//    }
}
