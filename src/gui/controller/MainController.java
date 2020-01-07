package gui.controller;

import gui.Monitoring;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Population;
import model.blob.BlobFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private BarChart<String, Integer>   chartMutationSize;
    @FXML
    private BarChart<String, Integer>   chartMutationSense;
    @FXML
    private BarChart<String, Integer>   chartBlobTypeGreedy;
    @FXML
    private BarChart<String, Integer>   chartBlobTypeNormal;
    @FXML
    private BarChart<String, Integer>   chartMutationSpeed;
    @FXML
    private TextField                   inMutationSpeed;
    @FXML
    private TextField                   inMutationSize;
    @FXML
    private TextField                   inMutationSense;
    @FXML
    private Button                      btStartSimulation;
    @FXML
    private TextField                   inAmountDays;
    @FXML
    private LineChart<Integer, Integer> chartPopulationSize;
    private BlobFactory                 blobFactory;
    private Population                  population;
    @FXML
    private Label                       lbCurrentDay;
    @FXML
    private TextField                   inPopulation;
    @FXML
    private TextField                   inNormalBlob;
    @FXML
    private TextField                   inGreedyBlob;
    private Monitoring                  monitoring;

    @Override
    public void initialize( URL url, ResourceBundle resourceBundle ) {
//        inNormalBlob.textProperty().addListener( ( observableValue, s, t1 ) -> System.out.println(s + " " + t1) );
        blobFactory = BlobFactory.getInstance();
        population  = Population.getInstance();
        monitoring  = Monitoring.getInstance();

        lbCurrentDay.textProperty().bind( population.getCurrentDay().asString() );

        monitoring.setCharts( chartPopulationSize,
                              chartBlobTypeNormal,
                              chartBlobTypeGreedy,
                              chartMutationSpeed,
                              chartMutationSize,
                              chartMutationSense );
    }

    public void startSimulation( ActionEvent actionEvent ) {
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle( "Information" );
//        alert.setContentText( "Please check your inputs." );
//        alert.showAndWait();

        monitoring.clear();

        blobFactory.set( Double.valueOf( inNormalBlob.getText() ),
                         Double.valueOf( inGreedyBlob.getText() ),
                         Double.valueOf( inMutationSpeed.getText() ),
                         Double.valueOf( inMutationSize.getText() ),
                         Double.valueOf( inMutationSense.getText() ) );

        population.startPopulation( Integer.parseInt( inPopulation.getText() ),
                                    Integer.parseInt( inAmountDays.getText() ) );

    }
}
