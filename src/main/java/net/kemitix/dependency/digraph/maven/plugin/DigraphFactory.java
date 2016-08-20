package net.kemitix.dependency.digraph.maven.plugin;

/**
 * Factory class for Digraph objects.
 *
 * @author pcampbell
 */
final class DigraphFactory {

    private DigraphFactory() {
    }

    static DependencyData newDependencyData(final String basePackage) {
        return NodeTreeDependencyData.newInstance(basePackage);
    }

}
