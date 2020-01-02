import model.Population;
import model.blob.BlobFactory;

public class Main {

    public static void main( String[] args ) {

        Population population = Population.getInstance();

        population.startPopulation( 100 );

        System.out.println( population );
    }
}
