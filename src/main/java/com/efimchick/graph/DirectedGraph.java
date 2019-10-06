package com.efimchick.graph;

class DirectedGraph<T> extends GenericGraph<T> {

    @Override
    protected boolean fillAdjLists(final T start, final T end) {
        return adjLists.get(start).add(end);
    }
}
