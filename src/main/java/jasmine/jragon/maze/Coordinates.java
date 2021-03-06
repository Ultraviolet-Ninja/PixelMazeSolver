package jasmine.jragon.maze;

public record Coordinates(int x, int y) implements Comparable<Coordinates> {
    private static final int HASHING_NUMBER = 5501;

    public Coordinates add(Coordinates vector) {
        return new Coordinates(this.x + vector.x, this.y + vector.y);
    }

    public Coordinates add(int x, int y) {
        return new Coordinates(this.x + x, this.y + y);
    }

    @Override
    public int hashCode() {
        return x * HASHING_NUMBER + y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Coordinates o) {
        if (o == null) return -1;

        int xComparison = Integer.compare(x, o.x);
        if (xComparison != 0) return xComparison;

        return Integer.compare(y, o.y);
    }

    public double distanceToPoint(int x, int y) {
        return Math.abs(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    public double distanceToPoint(Coordinates other) {
        return distanceToPoint(other.x, other.y);
    }
}
