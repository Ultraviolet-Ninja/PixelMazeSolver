package jasmine.jragon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings("ConstantConditions")
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("main.fxml"));
        primaryStage.setTitle("Pixel Maze Solver");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }
}
