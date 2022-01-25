package jasmine.jragon.maze;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public record ImageBuilder(List<Coordinates> path, Image image,
                           Color startColor, Color endColor) {
    public Image build() {
        List<Pair<Coordinates, Color>> colorPath = determineColorPath();

        return null;
    }

    private List<Pair<Coordinates, Color>> determineColorPath() {
        int size = path.size();
        double deltaRed = startColor.getRed() - endColor.getRed(),
                deltaGreen = startColor.getGreen() - endColor.getGreen(),
                deltaBlue = startColor.getBlue() - endColor.getBlue();

        double decrementRed = deltaRed / size,
                decrementGreen = deltaGreen / size,
                decrementBlue = deltaBlue / size;

        double redCounter = startColor.getRed(),
                greenCounter = startColor.getGreen(),
                blueCounter = startColor.getBlue();

        List<Pair<Coordinates, Color>> colorPath = new ArrayList<>();
        for (Coordinates coordinates : path) {
            colorPath.add(new Pair<>(coordinates, new Color(redCounter, greenCounter, blueCounter, 1.0)));
            redCounter -= decrementRed;
            greenCounter -= decrementGreen;
            blueCounter -= decrementBlue;
        }

        return colorPath;
    }
}
