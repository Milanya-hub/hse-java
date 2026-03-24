package hse.java.commander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class CommanderApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CommanderApplication.class.getResource("commander-ui.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        MainController ctrl = fxmlLoader.getController();
        String home = System.getProperty("user.home");
        if (ctrl != null && home != null) {
            ctrl.setInitialDirs(Paths.get(home), Paths.get(home));
        }
        stage.setTitle("Commander");
        stage.setScene(scene);
        stage.show();
        System.out.println("Hello world");
    }
}
