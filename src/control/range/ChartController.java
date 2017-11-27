package control.range;


import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import model.representation.Stat;
public class ChartController{

	 @FXML
	 private PieChart _pcPlays;
     @FXML
     private PieChart _pcDraws;

	 private ArrayList<Stat> playStats;
	 private ArrayList<Stat> drawStats;
	 
	 public ChartController(ArrayList<Stat> playStats, ArrayList<Stat> drawStats) throws IOException {
         changeStats(playStats, drawStats);
	 }

	 public void changeStats(ArrayList<Stat> playStats, ArrayList<Stat> drawStats){
         this.playStats = playStats;
        this.drawStats = drawStats;

         if(this.drawStats.isEmpty())
             this.drawStats.add(new Stat("No Draws", 100));

         if(this.playStats.isEmpty())
             this.playStats.add(new Stat("No Plays", 100));

         changeChart(true, playStats);
         changeChart(false, drawStats);
     }

	 private void changeChart(boolean plays, ArrayList<Stat> stats){
         Platform.runLater(() ->
         {
             ArrayList<PieChart.Data> statsList = new ArrayList<>(stats.size());
             for(Stat s : stats)
                 statsList.add(new PieChart.Data(s.toString(), s.getValue()));


             ObservableList<Data> list = FXCollections.observableArrayList(statsList);
             if(plays)
                 _pcPlays.setData(list);
             else {
                 _pcDraws.setData(list);
                 int totalDraws = 0;
                 for(Stat s: stats)
                     totalDraws += s.getValue();
                 _pcDraws.setTitle("Draw Stats, Total: " + totalDraws);
             }

             list.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName())));
         });
     }
}
