package com.efimchick.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class DomainApiDesignTests {

    private Graph<String> undirected;
    private Graph<String> directed;

    @Before
    public void init() {
        undirected = Graph.createUndirected();
        directed = Graph.createDirected();
    }

    @Test
    public void apiDesignTest() {

        undirected = Graph.createUndirected();
        directed = Graph.createDirected();

        Stream.of(undirected, directed)
                .forEach(graph -> {
                    graph.addVertex("a");
                    graph.addVertex("b");
                    graph.addVertex("c");

                    graph.addEdge("a", "b");
                    graph.addEdge("b", "c");
                    graph.addEdge("c", "a");
                });

        assertEquals(2, directed.getPath("a", "c").size());
        assertEquals(1, undirected.getPath("a", "c").size());
    }

    @Test
    public void addVertexTest() {
        Stream.of(directed, undirected)
                .forEach(graph -> {
                    assertTrue(graph.addVertex("a"));
                    assertTrue(graph.addVertex("b"));
                    assertTrue(graph.addVertex("c"));

                    assertFalse(graph.addVertex("a"));
                    assertFalse(graph.addVertex("b"));
                    assertFalse(graph.addVertex("c"));
                });
    }

    @Test
    public void addEdgeTest() {
        Stream.of(directed, undirected)
                .forEach(graph -> {
                    assertTrue(graph.addVertex("a"));
                    assertTrue(graph.addVertex("b"));
                    assertTrue(graph.addVertex("c"));

                    assertFalse(graph.addVertex("a"));
                    assertFalse(graph.addVertex("b"));
                    assertFalse(graph.addVertex("c"));
                });

        Stream.of(directed, undirected)
                .forEach(graph -> {
                    assertTrue(graph.addEdge("a", "b"));
                    assertTrue(graph.addEdge("b", "c"));
                    assertTrue(graph.addEdge("c", "a"));

                    assertFalse(graph.addEdge("a", "b"));
                    assertFalse(graph.addEdge("b", "c"));
                    assertFalse(graph.addEdge("c", "a"));
                });

        assertFalse(undirected.addEdge("b", "a"));
        assertFalse(undirected.addEdge("c", "b"));
        assertFalse(undirected.addEdge("a", "c"));

        assertTrue(directed.addEdge("b", "a"));
        assertTrue(directed.addEdge("c", "b"));
        assertTrue(directed.addEdge("a", "c"));
    }
}
