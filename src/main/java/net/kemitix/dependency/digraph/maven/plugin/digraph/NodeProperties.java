package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Properties for the nodes that follow.
 * <p>
 * Created by pcampbell on 20/02/16.
 */
public class NodeProperties extends GraphElement {

    @Getter
    private final Set<PropertyElement> properties = new HashSet<>();

    /**
     * Adds a property to the node.
     *
     * @param propertyElement the property to add
     *
     * @return true if the property is added
     */
    public boolean add(
            final PropertyElement propertyElement) {
        return properties.add(propertyElement);
    }

}
