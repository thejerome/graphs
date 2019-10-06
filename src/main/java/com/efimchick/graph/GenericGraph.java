package com.efimchick.graph;

import static java.util.Objects.requireNonNull;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import one.util.streamex.StreamEx;

abstract class GenericGraph<T> implements Graph<T> {

    final Map<T, Set<T>> adjLists = new LinkedHashMap<>();

    @Override
    public boolean addVertex(final T vertex) {
        requireNonNull(vertex);
        if (adjLists.containsKey(vertex)) {
            return false;
        }
        adjLists.put(vertex, new LinkedHashSet<>());
        return true;
    }

    @Override
    public boolean addEdge(final T start, final T end) {
        requireNonNull(start);
        requireNonNull(end);

        requireNonNull(adjLists.get(start));
        requireNonNull(adjLists.get(end));

        return fillAdjLists(start, end);
    }

    @Override
    public boolean edgeExists(final T start, final T end) {
        requireNonNull(start);
        requireNonNull(end);

        requireNonNull(adjLists.get(start));
        requireNonNull(adjLists.get(end));

        return adjLists.get(start).contains(end);
    }

    @Override
    public List<Edge<T>> getPath(final T start, final T end) {
        requireNonNull(start);
        requireNonNull(end);

        requireNonNull(adjLists.get(start));
        requireNonNull(adjLists.get(end));

        Deque<T> path = new LinkedList<>();
        if (searchPath(start, end, path)) {
            return StreamEx.of(path.stream()).pairMap(Edge::new).collect(Collectors.toList());
        }

        throw new GraphException("No path is found between " + start + " and " + end);
    }

    protected abstract boolean fillAdjLists(final T start, final T end);

    private boolean searchPath(final T start, final T end, Deque<T> path) {
        path.addLast(start);

        if (start.equals(end)) {
            return true;
        }

        final boolean pathIsFound = adjLists.get(start).stream()
                .filter(adj -> !path.contains(adj))
                .anyMatch(adj -> searchPath(adj, end, path));

        if (!pathIsFound) {
            path.removeLast();
        }
        return pathIsFound;

    }

    @Override
    public String toString() {
        return adjLists.entrySet().stream()
                .map(e -> e.getKey().toString() + ":" + e.getValue().toString())
                .collect(Collectors.joining(";"));
    }
}
