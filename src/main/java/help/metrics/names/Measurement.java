package help.metrics.names;

public enum Measurement {
    TAG("m_tag"),
    DURATION("m_duration");

    private final String name;
    Measurement(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
