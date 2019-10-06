package com.efimchick.graph;

import java.util.List;

public interface Graph<T> {

    boolean addVertex(T vertex);

    boolean addEdge(T start, T end);

    boolean edgeExists(T start, T end);

    List<Edge<T>> getPath(T start, T end);

    static <T> Graph<T> createUndirected() {
        return new UndirectedGraph<>();
    }

    static <T> Graph<T> createDirected() {
        return new DirectedGraph<>();
    }

}
