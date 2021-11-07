package sc.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.scene.*;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import sc.model.*;

public class ChartPane extends Group {
private PieChart chart;

public ChartPane(Map<Tipo,Float>map) {
	List<PieChart.Data> dati= new ArrayList<PieChart.Data>();
	for(Tipo t:map.keySet()) {
		dati.add(new Data(t.toString()+"\n"+String.format("%.2f€", map.get(t)),map.get(t)));	
	}
	chart= new PieChart(FXCollections.observableArrayList(dati));

	this.getChildren().add(chart);
}
}
