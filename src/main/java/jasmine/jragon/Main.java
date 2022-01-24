package jasmine.jragon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import static javafx.scene.input.KeyCode.H;
import static javafx.scene.input.KeyCode.O;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root, 1000, 700);
        KeyCombination saveImage = new KeyCodeCombination(S, CONTROL_DOWN);
        KeyCombination openImage = new KeyCodeCombination(O, CONTROL_DOWN);
        KeyCombination help = new KeyCodeCombination(H, CONTROL_DOWN);

        scene.addEventFilter(
                KeyEvent.KEY_PRESSED,
                event -> {
                    if(saveImage.match(event)) controller.saveImage();
                }
        );

        scene.addEventFilter(
                KeyEvent.KEY_PRESSED,
                event -> {
                    if(openImage.match(event)) controller.loadImage();
                }
        );

        scene.addEventFilter(
                KeyEvent.KEY_PRESSED,
                event -> {
                    if(help.match(event)) controller.displayHelp();
                }
        );

        primaryStage.setTitle("Pixel Maze Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
