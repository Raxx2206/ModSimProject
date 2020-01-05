import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Population;
import model.blob.BlobFactory;

public class Main extends Application{

    public static void main( String[] args ) {
        launch( args );
    }

    @Override
    public void start( Stage stage ) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation( getClass().getResource("gui/view/mainWindow.fxml") );
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
