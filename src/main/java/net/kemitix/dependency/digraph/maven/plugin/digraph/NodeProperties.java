package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;

/**
 * Properties for the nodes that follow.
 *
 * @author pcampbell
 */
public class NodeProperties extends AbstractGraphElement {

    @Getter
    private final Set<PropertyElement> properties = new HashSet<>();

    NodeProperties(final DotFileFormat format) {
        super(format);
    }

    /**
     * Adds a property to the node.
     *
     * @param propertyElement the property to add
     *
     * @return true if the property is added
     */
    public final boolean add(final PropertyElement propertyElement) {
        return properties.add(propertyElement);
    }

    @Override
    public final String render() {
        return getFormat().render(this);
    }
}
