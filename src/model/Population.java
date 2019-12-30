package model;

import core.Blob;
import model.blob.BlobPoint;
import model.blob.NormalBlob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Population extends ArrayList<Blob> {
    //TODO remove, just temporary solution
    private static final int   DAYS = 2;
    private final static int   SIZE = 1000;
    private              Field field;

    /**
     * constructor population
     */
    private Population() {
        super( SIZE );

        field = Field.getInstance();
    }

    /**
     * get singleton reference
     *
     * @return
     */
    public static Population getInstance() {
        return Inner.population;
    }

    /**
     * start the simulation
     *
     * @param amount starting population size
     */
    public void startPopulation( int amount ) {
        // create start population
        for (int i = 0; i < amount; i++)
             add( new NormalBlob( BlobPoint.randomPoint() ) );

        // main loop
        for (int i = 0; i < DAYS && size() > 0; i++) {
            actionPhase();
            liveOrDeathPhase();
        }

    }

    /**
     * every simulation object have it's time to do something like moving and eating
     */
    public void actionPhase() {
        field.generateRandomFood( 100 );
        int            popCounter  = 0;
        List<Blob>     k           = new ArrayList<>( this );
        Iterator<Blob> kIter;
        Blob           currentBlob = null;

        while (!k.isEmpty()) {

            for (kIter = k.iterator(); kIter.hasNext(); ) {
                currentBlob = kIter.next();
                if ( currentBlob.getEnergy() > 0 && !currentBlob.isAtHome() ) {
                    currentBlob.move();
                } else {
                    kIter.remove();
                }
            }
        }
    }

    /**
     * checks the population if any creature still have energy left or not
     */
    public void liveOrDeathPhase() {
        // remove death blobs
        removeIf( blob -> blob.getFoodCounter() < 1 || !blob.isAtHome() );

        // born new blobs
        forEach( blob -> {
            if ( blob.getFoodCounter() > 1 )
                add( new NormalBlob( BlobPoint.randomPoint() ) );
        } );

        forEach( Blob::resetEnergy );
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder( 32 );

        forEach( blob -> builder.append( "[ " )
                                .append( blob )
                                .append( " ]\n" ) );

        return builder.toString();
    }

    /**
     * class for singleton
     */
    private static class Inner {
        private static Population population = new Population();
    }
}
