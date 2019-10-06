package com.efimchick.graph;

class UndirectedGraph<T> extends GenericGraph<T> {

    @Override
    protected boolean fillAdjLists(final T start, final T end) {
        return adjLists.get(start).add(end) && adjLists.get(end).add(start);
    }
}
