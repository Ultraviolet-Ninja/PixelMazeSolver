package jasmine.jragon.maze;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public record ImageBuilder(List<Coordinates> path, Image image,
                           Color startColor, Color endColor) {
    public Image build() {
        Map<Coordinates, Color> colorMap = determineColorPath();
        return createImage(colorMap);
    }

    private Map<Coordinates, Color> determineColorPath() {
        int size = path.size();
        double deltaRed = startColor.getRed() - endColor.getRed(),
                deltaGreen = startColor.getGreen() - endColor.getGreen(),
                deltaBlue = startColor.getBlue() - endColor.getBlue();

        double incrementRed = deltaRed / size,
                incrementGreen = deltaGreen / size,
                incrementBlue = deltaBlue / size;

        double redCounter = endColor.getRed(),
                greenCounter = endColor.getGreen(),
                blueCounter = endColor.getBlue();

        Map<Coordinates, Color> colorMap = new TreeMap<>();
        for (Coordinates coordinates : path) {
            colorMap.put(coordinates, new Color(redCounter, greenCounter, blueCounter, 1.0));
            redCounter += incrementRed;
            greenCounter += incrementGreen;
            blueCounter += incrementBlue;
        }

        return colorMap;
    }

    private Image createImage(Map<Coordinates, Color> colorMap) {
        final int width = (int) image.getWidth();
        final int height = (int) image.getHeight();

        WritableImage newImage = new WritableImage(width, height);
        PixelWriter newImageWriter = newImage.getPixelWriter();
        PixelReader reader = image.getPixelReader();

        Coordinates currentPoint;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                currentPoint = new Coordinates(x, y);
                newImageWriter.setColor(x, y,
                        colorMap.getOrDefault(currentPoint, reader.getColor(x, y))
                );
            }
        }

        return newImage;
    }
}
