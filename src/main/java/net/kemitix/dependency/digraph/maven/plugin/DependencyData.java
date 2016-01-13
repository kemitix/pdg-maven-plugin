package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

/**
 * Interface for storing package and class dependency data.
 *
 * @author pcampbell
 */
interface DependencyData {

    /**
     * Records a dependency between the user class and the imported class.
     *
     * @param user     the package that is using the import
     * @param imported the package that contains the class that is being
     *                 imported
     */
    void addDependency(String user, String imported);

    /**
     * Sets the base package.
     *
     * @param basePackage the base package within which to report
     */
    void setBasePackage(final String basePackage);

    /**
     * Returns the base node.
     *
     * @return the base node
     */
    Node<PackageData> getBaseNode();

}
