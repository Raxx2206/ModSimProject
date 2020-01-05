package model.blob;

import core.Blob;
import core.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BlobFactory {
    private static final Random random = new Random();

    //Blob probabilities for generating
    private double probabilityNormal        = 0.0;
    private double probabilityGreedy        = 1.0;
    //mutation probabilities
    private double probabilityMutationSpeed = 0.5;
    private double probabilityMutationSense = 0.5;
    private double probabilityMutationSize  = 0.5;

    @Contract(pure = true)
    private BlobFactory() {

    }

    @Contract(pure = true)
    public static BlobFactory getInstance() {
        return Inner.instance;
    }

    public Blob next( int energy, double speed, double size, double sense, Point2D pos ) {
        //generate random blob types
        double value = random.nextDouble();

        if ( value <= probabilityNormal )
            return new NormalBlob( energy, speed, size, sense, pos );
        else if ( value <= probabilityGreedy )
            return new GreedyBlob( energy, speed, size, sense, pos );

        return new NormalBlob( BlobPoint.randomPoint() );
    }

    public Blob next() {
        return next( Blob.BASIC_ENERGY, Blob.BASIC_SPEED, Blob.BASIC_SIZE, Blob.BASIC_SENSE, BlobPoint.randomPoint() );
    }

    //mutation of an old blob
    public Blob next( @NotNull Blob oldBlob ) {
        double newSpeed = mutation( probabilityMutationSpeed, oldBlob.getSpeed() );
        double newSize  = mutation( probabilityMutationSize, oldBlob.getSize() );
        double newSense = mutation( probabilityMutationSense, oldBlob.getSense() );

        newSpeed = getRidOfDecimals( newSpeed );
        newSize  = getRidOfDecimals( newSize );
        newSense = getRidOfDecimals( newSense );

        if ( newSpeed < 0.5 )
            newSpeed = 0.5;

        if ( newSize < 0.5 )
            newSize = 0.5;

        if ( newSense < 0.5 )
            newSense = 0.5;

        return next( Blob.BASIC_ENERGY, newSpeed, newSize, newSense, BlobPoint.randomPoint() );
    }

    //calc next value
    private double mutation( double probability, double oldBlobValue ) {
        double value = random.nextDouble();

        return (random.nextDouble() <= probability) ?
               (oldBlobValue + (value - 0.5)) :            //new value between - .5 / + .5
               oldBlobValue;
    }

    private double getRidOfDecimals( double oldValue ) {
        oldValue = Math.round( oldValue * 10 );
        oldValue = oldValue / 10;
        return oldValue;
    }

    public double getProbabilityNormal() {
        return probabilityNormal;
    }

    public void setProbabilityNormal( double probabilityNormal ) {
        this.probabilityNormal = probabilityNormal;
    }

    public double getProbabilityGreedy() {
        return probabilityGreedy;
    }

    public void setProbabilityGreedy( double probabilityGreedy ) {
        this.probabilityGreedy = probabilityGreedy;
    }

    public double getProbabilityMutationSpeed() {
        return probabilityMutationSpeed;
    }

    public void setProbabilityMutationSpeed( double probabilityMutationSpeed ) {
        this.probabilityMutationSpeed = probabilityMutationSpeed;
    }

    public double getProbabilityMutationSense() {
        return probabilityMutationSense;
    }

    public void setProbabilityMutationSense( double probabilityMutationSense ) {
        this.probabilityMutationSense = probabilityMutationSense;
    }

    public double getProbabilityMutationSize() {
        return probabilityMutationSize;
    }

    public void setProbabilityMutationSize( double probabilityMutationSize ) {
        this.probabilityMutationSize = probabilityMutationSize;
    }

    public void set( Double normalBlobProbability,
                     Double greedyProbability,
                     Double mutationSpeedProbability,
                     Double mutationSizeProbability,
                     Double mutationSenseProbability ) {
        setProbabilityNormal( normalBlobProbability );
        setProbabilityGreedy( greedyProbability );
        setProbabilityMutationSpeed( mutationSpeedProbability );
        setProbabilityMutationSize( mutationSizeProbability );
        setProbabilityMutationSense( mutationSenseProbability );
    }

    private static class Inner {
        private static BlobFactory instance = new BlobFactory();
    }
}