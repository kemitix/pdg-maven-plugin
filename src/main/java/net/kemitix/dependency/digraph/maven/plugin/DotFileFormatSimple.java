package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

/**
 * Generates a dot file dependency report as a simple digraph.
 *
 * @author pcampbell
 */
class DotFileFormatSimple extends AbstractDotFileFormat {

    DotFileFormatSimple(
            final Node<PackageData> base,
            final NodePathGenerator nodePathGenerator) {
        super(base, nodePathGenerator);
    }

    @Override
    String renderNode(final Node<PackageData> node) {
        final StringBuilder render = new StringBuilder();
        if (!node.equals(getBase())) {
            render.append("\"").append(getPath(node, ".")).append("\";");
        }
        node.getChildren()
            .stream()
            .sorted(new NodePackageDataComparator())
            .forEach((n) -> render.append(this.renderNode(n)));
        return render.toString();
    }

    @Override
    String renderUsages(final Node<PackageData> node) {
        final StringBuilder render = new StringBuilder();
        render.append(renderUsage(node));
        node.getChildren()
            .stream()
            .sorted(new NodePackageDataComparator())
            .forEach((n) -> render.append(this.renderUsages(n)));
        return render.toString();
    }

    private String renderUsage(final Node<PackageData> origin) {
        final StringBuilder render = new StringBuilder();
        final String originId = getPath(origin, ".");
        origin.getData()
              .getUses()
              .stream()
              .sorted(new NodePackageDataComparator())
              .forEach((Node<PackageData> destination) -> {
                  if (destination.isChildOf(getBase())) {
                      render.append("\"")
                            .append(originId)
                            .append("\"->\"")
                            .append(getPath(destination, "."))
                            .append("\";");
                  }
              });
        return render.toString();
    }

}
