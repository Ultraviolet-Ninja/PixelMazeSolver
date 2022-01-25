package jasmine.jragon.maze;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class ImageParser {
    private final int width, height;
    private final Image image;

    private Color pathColor;

    public ImageParser(@NotNull Image image) {
        this.image = image;
        width = (int) image.getWidth();
        height = (int) image.getHeight();
        pathColor = Color.WHITE;
    }

    public Triplet<Graph<Coordinates, DefaultEdge>, Coordinates, Coordinates> parseToGraph() {
        PixelReader reader = image.getPixelReader();
        List<List<Color>> colors = parseColors(reader);
        Color currentPathColor = getPathColor(colors.get(0));
        if (!pathColor.equals(currentPathColor))
            pathColor = currentPathColor;
        return translateToGraph(colors);
    }

    private static Color getPathColor(List<Color> colors) {
        Map<Color, Long> firstRow = colors
                .stream()
                .collect(groupingBy(identity(), counting()));

        Map.Entry<Color, Long> minEntry = null;

        for (Map.Entry<Color, Long> entry : firstRow.entrySet()) {
            if (minEntry == null || entry.getValue().compareTo(minEntry.getValue()) < 0) {
                minEntry = entry;
            }
        }

        assert minEntry != null;
        return minEntry.getKey();
    }

    private List<List<Color>> parseColors(PixelReader reader) {
        return IntStream.range(0, height)
                .parallel()
                .mapToObj(index -> {
                    List<Color> colors = new ArrayList<>();
                    for (int x = 0; x < height; x++) {
                        colors.add(reader.getColor(x, index));
                    }
                    return colors;
                })
                .toList();
    }

    private Triplet<Graph<Coordinates, DefaultEdge>, Coordinates, Coordinates> translateToGraph(
            List<List<Color>> colors) {
        int end = colors.size() - 1;
        Coordinates startingPoint = new Coordinates(0, colors.get(0).indexOf(pathColor));
        Coordinates endingPoint = new Coordinates(end, colors.get(end).indexOf(pathColor));
        Graph<Coordinates, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        graph.addVertex(startingPoint);
        graph.addVertex(endingPoint);

        for (int j = 1; j < end; j++) {
            for (int i = 1; i < width - 1; i++) {
                if (isNotAHallway(i, j, colors)) {
                    inspectPathConnections(graph, colors, i, j);
                }
            }
        }

        graph.addEdge(startingPoint, startingPoint.add(1, 0));
        graph.addEdge(endingPoint, endingPoint.add(-1, 0));
        return new Triplet<>(graph, startingPoint, endingPoint);
    }

    private void inspectPathConnections(Graph<Coordinates, DefaultEdge> graph, List<List<Color>> colors,
                                        int x, int y) {
        Coordinates currentPoint = new Coordinates(x, y);
        graph.addVertex(currentPoint);
        int tempY = y + 1;
        if (colors.get(tempY).get(x).equals(pathColor)) {
            while (isInBounds(x, tempY) && !isNotAHallway(x, tempY, colors)) tempY++;
            if (isInBounds(x, tempY)) {
                Coordinates southConnection = new Coordinates(x, tempY);
                graph.addVertex(southConnection);
                graph.addEdge(currentPoint, southConnection);
            }
        }

        int tempX = x + 1;
        if (colors.get(y).get(tempX).equals(pathColor)) {
            while (isInBounds(tempX, y) && !isNotAHallway(tempX, y, colors)) tempX++;
            if (isInBounds(tempX, y)) {
                Coordinates eastConnection = new Coordinates(tempX, y);
                graph.addVertex(eastConnection);
                graph.addEdge(currentPoint, eastConnection);
            }
        }
    }

    private boolean isNotAHallway(int x, int y, List<List<Color>> colors) {
        Color colorAbove = colors.get(y - 1).get(x),
                colorBelow = colors.get(y + 1).get(x),
                colorToRight = colors.get(y).get(x + 1),
                colorToLeft = colors.get(y).get(x - 1);

        long count = Stream.of(colorAbove, colorBelow, colorToRight, colorToLeft)
                .filter(color -> color.equals(pathColor))
                .count();
        if (count != 2)
            return true;

        boolean hasVerticalHallway = colorAbove.equals(pathColor) && colorBelow.equals(pathColor);
        boolean hasHorizontalHallway = colorToRight.equals(pathColor) && colorToLeft.equals(pathColor);

        return hasHorizontalHallway == hasVerticalHallway;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < width - 1 && y < height - 1;
    }
}
