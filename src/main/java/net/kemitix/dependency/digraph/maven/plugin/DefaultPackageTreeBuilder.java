package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;

import net.kemitix.node.Node;
import net.kemitix.node.NodeException;
import net.kemitix.node.Nodes;

/**
 * Builds a tree of packages.
 *
 * @author pcampbell
 */
class DefaultPackageTreeBuilder implements PackageTreeBuilder {

    public static final String PACKAGE_DELIMITER = ".";

    private String basePackage;

    @Getter
    private Node<String> tree;

    @Override
    public void init(@NonNull final String thePackage) {
        this.basePackage = thePackage;
        this.tree = Nodes.unnamedRoot(thePackage);
    }

    /**
     * Add the package(s) to the list of known packages.
     *
     * @param thePackages the packages to add
     */
    @Override
    public void addPackages(final String... thePackages) {
        Arrays.asList(thePackages).forEach(this::addPackage);
    }

    private void addPackage(@NonNull final String thePackage) {
        verifyWithinBasePackage(thePackage);
        tree.createDescendantLine(
                Arrays.asList(getRelativePackage(thePackage).split("\\.")));
    }

    private void verifyWithinBasePackage(final String thePackage) {
        if (!thePackage.equals(basePackage) && !thePackage.startsWith(
                basePackage + PACKAGE_DELIMITER)) {
            throw new NodeException(
                    "Package not within base package: " + thePackage);
        }
    }

    private String getRelativePackage(final String thePackage) {
        if (!thePackage.endsWith(basePackage)) {
            return thePackage.substring(basePackage.length() + 1);
        }
        return PACKAGE_DELIMITER;
    }

}
