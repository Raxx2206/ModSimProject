import model.Population;

public class Main {

    public static void main( String[] args ) {

        Population population = Population.getInstance();

        System.out.println( population );

        population.startPopulation( 2 );

        System.out.println( population );
    }
}
