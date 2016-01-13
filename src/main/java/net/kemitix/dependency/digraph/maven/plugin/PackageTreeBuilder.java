package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

/**
 * Interface for building a tree of packages.
 *
 * @author pcampbell
 */
interface PackageTreeBuilder {

    /**
     * Initialise the builder and set the base package.
     *
     * @param basePackage the base package for the tree
     */
    void init(String basePackage);

    /**
     * Add the package(s) to the list of known packages.
     *
     * @param thePackages the packages to add
     */
    void addPackages(String... thePackages);

    /**
     * Returns the tree of packages.
     *
     * @return the package tree
     */
    Node<String> getTree();

}
