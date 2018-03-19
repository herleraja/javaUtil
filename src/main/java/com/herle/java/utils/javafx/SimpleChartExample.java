package com.herle.java.utils.javafx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

public class SimpleChartExample extends Application {

	@Override
	public void start(Stage stage) {
		
		stage.setTitle("Top 10 country hits");
		stage.setWidth(500);
		stage.setHeight(500);
		//stage.setFullScreen(true);
		
		
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Grapefruit", 13), new PieChart.Data("Oranges", 25), new PieChart.Data("Plums", 10),
				new PieChart.Data("Pears", 22), new PieChart.Data("Apples", 30));
		
		PieChart chart = new PieChart(pieChartData);
		chart.setTitle("Top 10 country");
		//chart.setLabelLineLength(150);
		chart.setLabelsVisible(false);
		chart.setLegendSide(Side.LEFT);

		Scene scene = new Scene(new Group());
		((Group) scene.getRoot()).getChildren().add(chart);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}