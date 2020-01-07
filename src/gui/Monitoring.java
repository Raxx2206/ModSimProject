package gui;

import core.Blob;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import model.Population;
import model.blob.NormalBlob;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

public class Monitoring {

    private LineChart<Integer, Integer>      chartPopulationSize;
    private XYChart.Series<Integer, Integer> populationSizeSeries;
    private BarChart<String, Integer>        chartBlobTypeNormal, chartBlobTypeGreedy;
    private BarChart<String, Integer> chartMutationSpeed, chartMutationSize, chartMutationSense;

    private DecimalFormat decimalFormat = new DecimalFormat( "#.00" );

    private Monitoring() {
    }

    public static Monitoring getInstance() {
        return Inner.inner;
    }

    public void setCharts( LineChart<Integer, Integer> chartPopulationSize,
                           BarChart<String, Integer> chartBlobTypeNormal,
                           BarChart<String, Integer> chartBlobTypeGreedy,
                           BarChart<String, Integer> chartMutationSpeed,
                           BarChart<String, Integer> chartMutationSize,
                           BarChart<String, Integer> chartMutationSense ) {
        this.chartPopulationSize  = chartPopulationSize;
        this.populationSizeSeries = new XYChart.Series<>();
        this.chartPopulationSize.getData().add( populationSizeSeries );

        this.chartBlobTypeNormal = chartBlobTypeNormal;
        this.chartBlobTypeGreedy = chartBlobTypeGreedy;

        this.chartMutationSpeed = chartMutationSpeed;
        this.chartMutationSize  = chartMutationSize;
        this.chartMutationSense = chartMutationSense;
    }

    public void populationSize( int i, int size ) {
        populationSizeSeries.getData().add( new XYChart.Data<>( i, size ) );
    }

    public void blobs( Population population, int day ) {
        XYChart.Series<String, Integer> normal = new XYChart.Series<>();
        XYChart.Series<String, Integer> greedy = new XYChart.Series<>();

        int normalBlobCounter = 0, greedyBlobCounter = 0;

        for (Blob blob : population) {
            if ( blob.getClass() == NormalBlob.class )
                ++normalBlobCounter;
            else
                ++greedyBlobCounter;
        }

        normal.getData().add( new XYChart.Data<>( String.valueOf( day ), normalBlobCounter ) );
        greedy.getData().add( new XYChart.Data<>( String.valueOf( day ), greedyBlobCounter ) );

        chartBlobTypeNormal.getData().add( normal );
        chartBlobTypeGreedy.getData().add( greedy );
    }

    public void clear() {
        populationSizeSeries.getData().clear();
        chartBlobTypeNormal.getData().clear();
        chartBlobTypeGreedy.getData().clear();
        chartMutationSpeed.getData().clear();
    }

    public void mutationDistribution( Population population ) {
        XYChart.Series<String, Integer> histogramSpeedSeries = new XYChart.Series<>();
        XYChart.Series<String, Integer> histogramSizeSeries  = new XYChart.Series<>();
        XYChart.Series<String, Integer> histogramSenseSeries = new XYChart.Series<>();

        TreeMap<Double, Integer> treeMapSpeed = new TreeMap<>();
        TreeMap<Double, Integer> treeMapSize  = new TreeMap<>();
        TreeMap<Double, Integer> treeMapSense = new TreeMap<>();
        double                   lowerBound   = 0.5, stepSize = 0.2;

        for (Blob blob : population) {
            put( blob.getSpeed(), treeMapSpeed );
            put( blob.getSize(), treeMapSize );
            put( blob.getSense(), treeMapSense );
        }

        if ( treeMapSpeed.isEmpty() || treeMapSize.isEmpty() || treeMapSense.isEmpty() )
            return;

        createHistogramSeries( histogramSpeedSeries, treeMapSpeed, lowerBound, stepSize, treeMapSpeed.lastKey() );
        createHistogramSeries( histogramSizeSeries, treeMapSize, lowerBound, stepSize, treeMapSize.lastKey() );
        createHistogramSeries( histogramSenseSeries, treeMapSense, lowerBound, stepSize, treeMapSense.lastKey() );

        chartMutationSpeed.getData().add( histogramSpeedSeries );
        chartMutationSize.getData().add( histogramSizeSeries );
        chartMutationSense.getData().add( histogramSenseSeries );
    }

    private void createHistogramSeries( XYChart.Series<String, Integer> series,
                                        TreeMap<Double, Integer> treeMap,
                                        double lowerBound,
                                        double stepSize,
                                        double highestValue ) {
        double upperBound = lowerBound + stepSize;

        while (upperBound <= highestValue + stepSize) {
            var map = treeMap.subMap( lowerBound, upperBound );

            int counter = 0;
            for (int i : map.values())
                counter += i;

            series.getData().add( new XYChart.Data<>( decimalFormat.format( lowerBound ), counter ) );

            lowerBound = upperBound;
            upperBound = lowerBound + stepSize;
        }

    }

    private void put( double key, Map<Double, Integer> map ) {
        if ( !map.containsKey( key ) )
            map.put( key, 1 );
        else
            map.put( key, map.get( key ) + 1 );
    }

    private static class Inner {
        private static Monitoring inner = new Monitoring();
    }
}
