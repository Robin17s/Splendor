package domain;

public class Marker {
    private final Crystal type;

    public Marker(Crystal type) {
        this.type = type;
    }

    public Crystal getType() { return type; }
}