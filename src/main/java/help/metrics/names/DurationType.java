package help.metrics.names;

public enum DurationType {
    TEST("test"),
    QUEUE("queue"),
    UI("ui"),
    REST("rest"),
    OTHER("other");

    private final String name;
    DurationType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
