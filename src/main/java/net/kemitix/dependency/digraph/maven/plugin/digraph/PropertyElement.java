package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;

/**
 * A key, value property.
 *
 * @author pcampbell
 */
@Getter
public class PropertyElement extends GraphElement {

    private final String name;

    private final String value;

    /**
     * Constructor.
     *
     * @param name  the name of the property
     * @param value the value of the property
     */
    public PropertyElement(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

}
