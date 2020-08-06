package sample;

public class ReportsConsultantSched {
    private final String name;
    private final String start;
    private final String end;

    public ReportsConsultantSched(String name, String start, String end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
