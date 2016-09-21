package net.kemitix.dependency.digraph.maven.plugin;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import net.kemitix.node.Node;

/**
 * Default implementation of the {@link DotFileFormatFactory}.
 *
 * @author pcampbell
 */
@Immutable
class DefaultDotFileFormatFactory implements DotFileFormatFactory {

    private final NodePathGenerator nodePathGenerator;

    @Inject
    DefaultDotFileFormatFactory(final NodePathGenerator nodePathGenerator) {
        this.nodePathGenerator = nodePathGenerator;
    }

    @Override
    public DotFileFormat create(
            final String format, final Node<PackageData> base) {
        DotFileFormat reportFormat;
        switch (format) {
        case "simple":
            reportFormat = new DotFileFormatSimple(base, nodePathGenerator);
            break;
        case "nested":
        default:
            reportFormat = new DotFileFormatNested(base, nodePathGenerator);
            break;
        }
        return reportFormat;
    }
}
