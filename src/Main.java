import javafx.application.Application;
import javafx.stage.Stage;
import model.Population;
import model.blob.BlobFactory;

public class Main extends Application {

    public static void main( String[] args ) {

        Population population = Population.getInstance();

        population.startPopulation( 100 );

        System.out.println( population );

//        launch( args );
    }

    @Override
    public void start( Stage stage ) throws Exception {
        System.out.println("Hello");
    }
}
