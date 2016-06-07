package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

/**
 * Interface for generating a {@link DotFileFormat}.
 *
 * @author pcampbell
 */
interface DotFileFormatFactory {

    /**
     * Creates a {@link DotFileFormat} implementation for the given format.
     *
     * @param format the format selector
     * @param base   the base package
     *
     * @return the dot file format generator
     */
    DotFileFormat create(String format, Node<PackageData> base);

}
