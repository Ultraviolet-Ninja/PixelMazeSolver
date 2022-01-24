module PixelMazeSolver {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jgrapht.core;
    requires javatuples;
    requires MaterialFX;
    requires org.jetbrains.annotations;

    opens jasmine.jragon to javafx.fxml;

    exports jasmine.jragon;
    exports jasmine.jragon.maze;
    opens jasmine.jragon.maze to javafx.fxml;
}