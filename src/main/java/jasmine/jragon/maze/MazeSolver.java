package jasmine.jragon.maze;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.javatuples.Triplet;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MazeSolver {
    public static Image solveMaze(Image original, Algorithm algorithm, Color startColor, Color endColor) {
        ImageParser parser = new ImageParser(original);
        long start = System.nanoTime();
        Triplet<Graph<Coordinates, DefaultEdge>, Coordinates, Coordinates> pictureStats =
                parser.parseToGraph();

        List<Coordinates> path = findPath(pictureStats, algorithm);
        ImageBuilder builder = new ImageBuilder(path, original, startColor, endColor);

        long stop = System.nanoTime();
        System.out.printf("%,d%n", stop - start);
        return null;
    }

    private static List<Coordinates> findPath(Triplet<Graph<Coordinates, DefaultEdge>, Coordinates, Coordinates> triplet,
                                 Algorithm algorithm) {
        List<Coordinates> path = switch (algorithm) {
            case DIJKSTRA -> useDijkstra(triplet);
            case A_STAR -> useAStar(triplet);
        };
        return expandCoordinateList(path);
    }

    private static List<Coordinates> useDijkstra(Triplet<Graph<Coordinates, DefaultEdge>, Coordinates, Coordinates> triplet) {
        DijkstraShortestPath<Coordinates, DefaultEdge> shortestPath = new DijkstraShortestPath<>(triplet.getValue0());

        return shortestPath.getPath(
                triplet.getValue1(),
                triplet.getValue2()
        ).getVertexList();
    }

    private static List<Coordinates> useAStar(Triplet<Graph<Coordinates, DefaultEdge>, Coordinates, Coordinates> triplet) {
        final Coordinates start = triplet.getValue1();
        final Coordinates end = triplet.getValue2();

        AStarShortestPath<Coordinates, DefaultEdge> shortestPath = new AStarShortestPath<>(
                triplet.getValue0(),
                Coordinates::distanceToPoint
        );

        List<Coordinates> path = shortestPath.getPath(start, end)
                .getVertexList();
        System.out.println(path);
        return path;
    }

    private static List<Coordinates> expandCoordinateList(List<Coordinates> path) {
        Set<Coordinates> fullPathList = new LinkedHashSet<>();
        int end = path.size() - 1;
        for (int i = 0; i < end; i++) {
            fullPathList.addAll(findCoordinatesBetweenVertices(path.get(i), path.get(i + 1)));
        }
        return new ArrayList<>(fullPathList);
    }

    private static List<Coordinates> findCoordinatesBetweenVertices(Coordinates start, Coordinates end) {
        List<Coordinates> coordinatesList = new ArrayList<>();
        coordinatesList.add(start);
        int endPoint;

        if (Math.abs(start.x() - end.x()) > 1) {
            int y = end.y();
            if (start.x() < end.x()) {
                endPoint = end.x();
                for (int i = start.x() + 1; i < endPoint; i++) {
                    coordinatesList.add(new Coordinates(i, y));
                }
            } else {
                endPoint = start.x() - 1;
                for (int i = endPoint; i > end.x(); i--) {
                    coordinatesList.add(new Coordinates(i, y));
                }
            }
        } else if (Math.abs(start.y() - end.y()) > 1) {
            int x = end.x();
            if (start.y() < end.y()) {
                endPoint = end.y();
                for (int i = start.y() + 1; i < endPoint; i++) {
                    coordinatesList.add(new Coordinates(x, i));
                }
            } else {
                endPoint = start.y() - 1;
                for (int i = endPoint; i > end.y(); i--) {
                    coordinatesList.add(new Coordinates(x, i));
                }
            }
        }
        coordinatesList.add(end);
        return coordinatesList;
    }
}
