package project.issue.tracker.database.db;

public final class Pair {

    private String first, second;

    public Pair() {
        this.first = null;
        this.second = null;
    }

    public Pair(final String first_, final String second_) {
        this.first = first_;
        this.second = second_;
    }

    public final String getFirst() {
        return this.first;
    }

    public final void setFirst(final String first_) {
        this.first = first_;
    }

    public final String getSecond() {
        return this.second;
    }

    public final void setSecond(final String second_) {
        this.second = second_;
    }

    @Override
    public String toString() {
        return "Pair{" + "first=" + first + ", second=" + second + '}';
    }
}
