package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;

import net.kemitix.node.Node;
import net.kemitix.node.NodeException;
import net.kemitix.node.NodeItem;

/**
 * Builds a tree of packages.
 *
 * @author pcampbell
 */
class DefaultPackageTreeBuilder implements PackageTreeBuilder {

    private String basePackage;

    @Getter
    private Node<String> tree;

    @Override
    public void init(@NonNull final String thePackage) {
        this.basePackage = thePackage;
        this.tree = new NodeItem<>(thePackage);
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
        if (!thePackage.equals(basePackage) &&
            !(thePackage).startsWith(basePackage + ".")) {
            throw new NodeException(
                    "Package not within base package: " + thePackage);
        }
    }

    private String getRelativePackage(final String thePackage) {
        if (!thePackage.endsWith(basePackage)) {
            return thePackage.substring(basePackage.length() + 1);
        }
        return ".";
    }

}
