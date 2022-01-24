package jasmine.jragon;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {
    @FXML
    private ColorPicker start, finish;

    @FXML
    private MFXButton submitButton;

    @FXML
    private ImageView originalMazeView, solveView;

    @FXML
    void loadImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Image Selector");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Image files (*.png, *.jpg)", "*.png", "*.jpg")
        );

        File file = chooser.showOpenDialog(new Stage());
        submitButton.setDisable(file == null);
        if (file == null)
            return;
        originalMazeView.setImage(new Image(String.valueOf(file)));
    }

    @FXML
    void saveImage() {
        if (solveView.getImage() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText("Solution was not created");
            alert.setContentText("Load and solve a maze before saving the solution");
            return;
        }

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Save Directory");
        chooser.showDialog(new Stage());
    }

    @FXML
    void displayHelp() {
    }

    @FXML
    private void solveMaze() {
        Image image = originalMazeView.getImage();
        if (image == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Solve Error");
            alert.setHeaderText("");
            alert.setContentText("");
            return;
        }

    }
}
