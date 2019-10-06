package com.efimchick.graph;

public class Edge<T> {
    public final T start;
    public final T end;

    public Edge(final T start, final T end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return start + "-" + end;
    }
}
