package model;

import core.Blob;
import gui.Monitoring;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.blob.BlobFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Population extends ArrayList<Blob> {
    private final Field           field;
    private final BlobFactory     blobFactory;
    private       IntegerProperty currentDay;
    private       Monitoring      monitoring;

    /**
     * constructor population
     */
    private Population() {
        super( 1000 );

        field       = Field.getInstance();
        blobFactory = BlobFactory.getInstance();
        monitoring  = Monitoring.getInstance();
        currentDay  = new SimpleIntegerProperty( this, "currentDay" );
    }

    /**
     * get singleton reference
     *
     * @return instance
     */
    public static Population getInstance() {
        return Inner.population;
    }

    /**
     * start the simulation
     *
     * @param startPopulationSize starting population size
     */
    public void startPopulation( int startPopulationSize, int days ) {
        // reset day counter
        currentDay.set( 0 );
        clear();

        // create start population
        for (int i = 0; i < startPopulationSize; i++) {
            add( blobFactory.next() );
        }

        // main loop
        for (int i = 0; i < days && size() > 0; i++) {
            currentDay.set(i);
            monitoring.populationSize(currentDay.get(), size());
            monitoring.blobs(Inner.population, currentDay.get());

            actionPhase();
            liveOrDeathPhase();
        }

        monitoring.mutationDistribution( this );
    }

    /**
     * every simulation object have it's time to do something like moving and eating
     */
    private void actionPhase() {
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
    private void liveOrDeathPhase() {
        List<Blob> mutationBlobs = new ArrayList<>();

        // remove death blobs
        removeIf( blob -> blob.getFoodCounter() < 1 || !blob.isAtHome() );

        // born new blobs
        for (Blob blob : this) {
            if ( blob.getFoodCounter() >= 2 )
                mutationBlobs.add( blob );

            blob.resetEnergy();
            blob.setAtHome( false );
            blob.resetFood();
        }
        mutationBlobs.forEach( blob -> add( blobFactory.next( blob ) ) );

//        forEach( blob -> System.out.println( "Speed: " + blob.getSpeed() + " Size : " + blob.getSize() + " Sense: " + blob.getSense() ) );
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder( 32 );

        forEach( blob -> builder.append( "[ " ).append( blob ).append( " ]\n" ) );

        return builder.toString();
    }

    public IntegerProperty getCurrentDay() {
        return currentDay;
    }

    public IntegerProperty currentDayProperty() {
        return currentDay;
    }

    /**
     * class for singleton
     */
    private static class Inner {
        private static Population population = new Population();
    }
}
