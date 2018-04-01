import controller.ComponentFactory;
import service.IGeneralService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.AuthenticationController;

public class Launcher extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/authenticationView.fxml"));

        ComponentFactory componentFactory = ComponentFactory.instance();
        IGeneralService generalService = componentFactory.getGeneralService();
        AuthenticationController authenticationController = new AuthenticationController(generalService);

        loader.setController(authenticationController);
        primaryStage.setTitle("Bank Application");

        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
