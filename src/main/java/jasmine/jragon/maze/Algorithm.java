package jasmine.jragon.maze;

import org.jetbrains.annotations.Nullable;

public enum Algorithm {
    DIJKSTRA("Dijkstra's Shortest Path"), A_STAR("A* Algorithm");

    private final String text;

    Algorithm(String UIText) {
        text = UIText;
    }

    public String getText() {
        return text;
    }

    public @Nullable Algorithm getFromText(String text) {
        for (Algorithm a : values()) {
            if (text.equals(a.text))
                return a;
        }
        return null;
    }
}
