package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;

/**
 * A key, value property.
 *
 * @author pcampbell
 */
@Getter
public class PropertyElement extends AbstractGraphElement {

    private final String name;

    private final String value;

    /**
     * Constructor.
     *
     * @param name   the name of the property
     * @param value  the value of the property
     * @param format the output format
     */
    public PropertyElement(
            final String name, final String value, final DotFileFormat format) {
        super(format);
        this.name = name;
        this.value = value;
    }

    @Override
    public final String render() {
        return getFormat().render(this);
    }
}
