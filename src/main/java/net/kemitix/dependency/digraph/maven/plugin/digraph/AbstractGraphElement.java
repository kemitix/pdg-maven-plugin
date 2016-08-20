package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.AccessLevel;
import lombok.Getter;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;

/**
 * Common base for any element to be added to a graph.
 *
 * @author pcampbell
 */
abstract class AbstractGraphElement implements GraphElement {

    @Getter(AccessLevel.PROTECTED)
    private final DotFileFormat dotFileFormat;

    protected AbstractGraphElement(final DotFileFormat dotFileFormat) {
        this.dotFileFormat = dotFileFormat;
    }
}
