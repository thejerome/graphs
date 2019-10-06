package com.efimchick.graph;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

public class GetPathTests {

    @Test
    public void getPathDirectedGraphTest() {
        final Graph<String> graph = Graph.createDirected();
        fillGraph(graph, "sample.graph", Function.identity());

        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 12; j++) {
                if (i == j) continue;
                checkPath(graph, String.valueOf(i), String.valueOf(j));
            }
        }
    }

    @Test
    public void getPathUndirectedGraphTest() {
        final Graph<String> graph = Graph.createUndirected();
        fillGraph(graph, "sample.graph", Function.identity());

        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 12; j++) {
                if (i == j) continue;
                checkPath(graph, String.valueOf(i), String.valueOf(j));
            }
        }
    }

    @Test
    public void getPathDirectedGraphIntTest() {
        final Graph<Integer> graph = Graph.createDirected();
        fillGraph(graph, "sample.graph", Integer::parseInt);

        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 12; j++) {
                if (i == j) continue;
                checkPath(graph, i, j);
            }
        }
    }

    @Test
    public void getPathUndirectedGraphIntTest() {
        final Graph<Integer> graph = Graph.createUndirected();
        fillGraph(graph, "sample.graph", Integer::parseInt);

        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 12; j++) {
                if (i == j) continue;
                checkPath(graph, i, j);
            }
        }
    }

    @Test
    public void getPathRingUndirectedTest() {
        final Graph<Integer> graph = Graph.createUndirected();
        fillGraph(graph, "ring.graph", Integer::parseInt);

        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 12; j++) {
                if (i == j) continue;
                checkPath(graph, i, j);
            }
        }
    }

    @Test
    public void getPathRingDirectedTest() {
        final Graph<Integer> graph = Graph.createDirected();
        fillGraph(graph, "ring.graph", Integer::parseInt);

        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 12; j++) {
                if (i == j) continue;
                checkPath(graph, i, j);
            }
        }
    }

    @Test(expected = GraphException.class)
    public void getPathForestUndirectedTest() {
        final Graph<Integer> graph = Graph.createUndirected();
        fillGraph(graph, "forest.graph", Integer::parseInt);

        graph.getPath(1, 6);
    }

    @Test(expected = GraphException.class)
    public void getPathChainDirectedTest1() {
        final Graph<Integer> graph = Graph.createDirected();
        fillGraph(graph, "chain.graph", Integer::parseInt);
        graph.getPath(4, 1);
    }

    @Test
    public void getPathChainDirectedTest2() {
        final Graph<Integer> graph = Graph.createDirected();
        fillGraph(graph, "chain.graph", Integer::parseInt);
        assertNotNull(graph.getPath(1, 4));
    }

    @Test
    public void getPathChainUndirectedTest1() {
        final Graph<Integer> graph = Graph.createUndirected();
        fillGraph(graph, "chain.graph", Integer::parseInt);
        assertNotNull(graph.getPath(4, 1));
    }

    @Test
    public void getPathChainUndirectedTest2() {
        final Graph<Integer> graph = Graph.createUndirected();
        fillGraph(graph, "chain.graph", Integer::parseInt);
        assertNotNull(graph.getPath(1, 4));
    }

    private <T> void checkPath(Graph<T> graph, T start, T end) {
        final List<Edge<T>> path = graph.getPath(start, end);
        assertNotNull(path);
        assertFalse(path.isEmpty());
        assertEquals(start, path.get(0).start);
        assertEquals(end, path.get(path.size() - 1).end);
        for (Edge<T> edge : path) {
            assertTrue(graph.edgeExists(edge.start, edge.end));
        }
        for (int i = 0; i < path.size() - 1; i++) {
            final T currentEdgeEnd = path.get(i).end;
            final T nextEdgeStart = path.get(i + 1).start;
            assertEquals(currentEdgeEnd, nextEdgeStart);
        }

    }

    private <T> void fillGraph(Graph<T> graph, final String resourceName, final Function<String, T> parseFunction) {
        readResource(resourceName)
                .forEach(line -> {
                            final String[] adjList = line.split(":");
                            final T start = parseFunction.apply(adjList[0]);
                            graph.addVertex(start);
                            if (adjList.length == 2) {
                                Arrays.stream(adjList[1].split(","))
                                        .map(String::trim)
                                        .filter(s -> !s.isEmpty())
                                        .map(parseFunction)
                                        .forEach(vertex -> {
                                            graph.addVertex(vertex);
                                            graph.addEdge(start, vertex);
                                        });
                            }
                        }
                );
    }

    private Stream<String> readResource(final String resourceName) {
        return new BufferedReader(
                new InputStreamReader(
                        requireNonNull(getClass().getClassLoader().getResourceAsStream(resourceName)))
        ).lines();
    }
}
