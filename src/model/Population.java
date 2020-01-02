package model;

import core.Blob;
import model.blob.BlobFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Population extends ArrayList<Blob> {
    //TODO remove, just temporary solution
    private static final int         DAYS        = 100;
    private final static int         SIZE        = 1000;
    private              Field       field;
    private              BlobFactory blobFactory = new BlobFactory();

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
             add( blobFactory.next() );

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
        List<Blob> mutationBlobs = new ArrayList<>();

        // remove death blobs
        removeIf( blob -> blob.getFoodCounter() < 1 || !blob.isAtHome() );

        // born new blobs
        for (Blob blob : this) {
            blob.resetEnergy();
            blob.setAtHome( false );
            blob.resetFood();

            if ( blob.getFoodCounter() >= 2 )
                mutationBlobs.add( blob );
        }
        mutationBlobs.forEach( blob -> add( blobFactory.next( blob ) ) );
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
