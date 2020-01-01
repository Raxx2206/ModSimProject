package model;

import core.Blob;
import model.blob.BlobPoint;
import model.blob.GreedyBlob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Population extends ArrayList<Blob> {
    //TODO remove, just temporary solution
    private static final int   DAYS = 100;
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
             add( new GreedyBlob( BlobPoint.randomPoint() ) );

        // main loop
        for (int i = 0; i < DAYS && size() > 0; i++) {
            actionPhase();

            //TODO for debug
            System.out.println( "=====================Day: " + i + "=====================" );
            forEach( System.out::println );
            System.out.println( '\n' );

            liveOrDeathPhase();

        }

    }

    /**
     * every simulation object have it's time to do something like moving and eating
     */
    public void actionPhase() {
        field.generateRandomFood( 100 );
        List<Blob>     k = new ArrayList<>( this );
        Iterator<Blob> kIter;
        Blob           currentBlob;

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

        int counter = 0;
        // born new blobs
        for (Blob blob : this) {
            if ( blob.getFoodCounter() > 1 )
                ++counter;
            blob.resetEnergy();
            blob.setAtHome( false );
            blob.resetFood();
        }

        for (int i = 0; i < counter; i++)
             add( new GreedyBlob( BlobPoint.randomPoint() ) );
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
