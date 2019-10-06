package com.efimchick.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

public class GetPathTests {

    @Test
    public void getPathDirectedGraphTest() throws Exception {
        final Graph<String> graph = Graph.createDirected();
        fillGraph(graph, "sample.graph", Function.identity());

        assertEquals("[1-3, 3-6, 6-11, 11-12]", graph.getPath("1", "12").toString());
        assertEquals("[1-3, 3-6, 6-11]", graph.getPath("1", "11").toString());
        assertEquals("[1-3, 3-6, 6-11, 11-10]", graph.getPath("1", "10").toString());

        assertEquals("[2-6, 6-11]", graph.getPath("2", "11").toString());
        assertEquals("[5-11, 11-12]", graph.getPath("5", "12").toString());
        assertEquals("[3-6, 6-1]", graph.getPath("3", "1").toString());
    }

    @Test
    public void getPathUndirectedGraphTest() throws Exception {
        final Graph<String> graph = Graph.createUndirected();
        fillGraph(graph, "sample.graph", Function.identity());

        assertEquals("[1-12]", graph.getPath("1", "12").toString());
        assertEquals("[1-3, 3-6, 6-11]", graph.getPath("1", "11").toString());
        assertEquals("[1-3, 3-6, 6-10]", graph.getPath("1", "10").toString());

        assertEquals("[2-6, 6-11]", graph.getPath("2", "11").toString());
        assertEquals("[5-3, 3-1, 1-12]", graph.getPath("5", "12").toString());
        assertEquals("[3-1]", graph.getPath("3", "1").toString());
    }

    @Test
    public void getPathDirectedGraphIntTest() throws Exception {
        final Graph<Integer> graph = Graph.createDirected();
        fillGraph(graph, "sample.graph", Integer::parseInt);

        assertEquals("[1-3, 3-6, 6-11, 11-12]", graph.getPath(1, 12).toString());
        assertEquals("[1-3, 3-6, 6-11]", graph.getPath(1, 11).toString());
        assertEquals("[1-3, 3-6, 6-11, 11-10]", graph.getPath(1, 10).toString());

        assertEquals("[2-6, 6-11]", graph.getPath(2, 11).toString());
        assertEquals("[5-11, 11-12]", graph.getPath(5, 12).toString());
        assertEquals("[3-6, 6-1]", graph.getPath(3, 1).toString());
    }

    @Test
    public void getPathUndirectedGraphIntTest() throws Exception {
        final Graph<Integer> graph = Graph.createUndirected();
        fillGraph(graph, "sample.graph", Integer::parseInt);

        assertEquals("[1-12]", graph.getPath(1, 12).toString());
        assertEquals("[1-3, 3-6, 6-11]", graph.getPath(1, 11).toString());
        assertEquals("[1-3, 3-6, 6-10]", graph.getPath(1, 10).toString());

        assertEquals("[2-6, 6-11]", graph.getPath(2, 11).toString());
        assertEquals("[5-3, 3-1, 1-12]", graph.getPath(5, 12).toString());
        assertEquals("[3-1]", graph.getPath(3, 1).toString());
    }

    @Test
    public void getPathRingUndirectedTest() {
        final Graph<Integer> graph = Graph.createUndirected();
        fillGraph(graph, "ring.graph", Integer::parseInt);

        assertEquals("[1-2]", graph.getPath(1, 2).toString());
        assertEquals("[1-12]", graph.getPath(1, 12).toString());
        assertEquals("[7-6, 6-5, 5-4, 4-3]", graph.getPath(7, 3).toString());
        assertEquals("[11-10, 10-9, 9-8, 8-7, 7-6, 6-5, 5-4, 4-3]", graph.getPath(11, 3).toString());
        assertEquals("[5-6]", graph.getPath(5, 6).toString());
        assertEquals("[5-4, 4-3, 3-2, 2-1, 1-12, 12-11, 11-10, 10-9, 9-8, 8-7]", graph.getPath(5, 7).toString());
    }

    @Test
    public void getPathRingDirectedTest() {
        final Graph<Integer> graph = Graph.createDirected();
        fillGraph(graph, "ring.graph", Integer::parseInt);

        assertEquals("[1-12, 12-11, 11-10, 10-9, 9-8, 8-7, 7-6, 6-5, 5-4, 4-3, 3-2]", graph.getPath(1, 2).toString());
        assertEquals("[1-12]", graph.getPath(1, 12).toString());
        assertEquals("[7-6, 6-5, 5-4, 4-3]", graph.getPath(7, 3).toString());
        assertEquals("[11-10, 10-9, 9-8, 8-7, 7-6, 6-5, 5-4, 4-3]", graph.getPath(11, 3).toString());
        assertEquals("[5-4, 4-3, 3-2, 2-1, 1-12, 12-11, 11-10, 10-9, 9-8, 8-7, 7-6]", graph.getPath(5, 6).toString());
        assertEquals("[5-4, 4-3, 3-2, 2-1, 1-12, 12-11, 11-10, 10-9, 9-8, 8-7]", graph.getPath(5, 7).toString());
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
                        getClass().getClassLoader().getResourceAsStream(resourceName))
        ).lines();
    }
}
