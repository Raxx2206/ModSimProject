package model.blob;

import core.Blob;
import core.Point2D;

import java.util.Random;

public class BlobFactory {
    private static final Random random = new Random();

    private double probabilityNormal        = 0.7;
    private double probabilityGreedy        = 1.0;
    private double probabilityMutationSpeed = 0.5;
    private double probabilityMutationSense = 0.5;
    private double probabilityMutationSize  = 0.5;

    public Blob next( int energy, double speed, double size, double sense, Point2D pos ) {
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

    public Blob next( Blob oldBlob ) {
        double newSpeed = mutation( probabilityMutationSpeed, oldBlob.getSpeed() );
        double newSize  = mutation( probabilityMutationSize, oldBlob.getSize() );
        double newSense = mutation( probabilityMutationSense, oldBlob.getSense() );

        return next( Blob.BASIC_ENERGY, newSpeed, newSize, newSense, BlobPoint.randomPoint() );
    }

    public double mutation( double probability, double oldBlobValue ) {
        double value = (double) random.nextInt( 11 )/10;

        return (random.nextDouble() <= probability) ?
               (oldBlobValue + (value - 0.5 )) :
               oldBlobValue;
    }
}