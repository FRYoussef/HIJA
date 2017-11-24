package control.rank;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import model.representation.rank.Stats;
public class ChartController{

	 @FXML
	 private PieChart _statsPieChart;

	 private ArrayList<Stats> rawStats;
	 
	 public ChartController(ArrayList<Stats> rawStats){
		 this.rawStats = rawStats; 
		 Platform.runLater(() -> {
			 ArrayList<PieChart.Data> statsList = new ArrayList<PieChart.Data>(ChartController.this.rawStats.size());
			 Stats s; 
			 for(int i = 0; i < ChartController.this.rawStats.size(); i++){
				 s = ChartController.this.rawStats.get(i);
				 statsList.add(new PieChart.Data(s.getPlayAndCards(), s.getPercentage()));
			 }
			 ObservableList<Data> list = FXCollections.observableArrayList(statsList);
			 _statsPieChart.setData(list);
			 _statsPieChart.setTitle("Statistics Pie Chart");
			 
			 list.forEach(data ->
			 		data.nameProperty().bind(Bindings.concat(
			 				data.getName(), " ", data.pieValueProperty(), " %"))); 
	     });
		 
	 }
}
