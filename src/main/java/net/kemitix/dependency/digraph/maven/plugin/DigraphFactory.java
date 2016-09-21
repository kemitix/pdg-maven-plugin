package net.kemitix.dependency.digraph.maven.plugin;

import javax.annotation.concurrent.Immutable;

/**
 * Factory class for Digraph objects.
 *
 * @author pcampbell
 */
@Immutable
final class DigraphFactory {

    private DigraphFactory() {
    }

    static DependencyData newDependencyData(final String basePackage) {
        return NodeTreeDependencyData.newInstance(basePackage);
    }

}
