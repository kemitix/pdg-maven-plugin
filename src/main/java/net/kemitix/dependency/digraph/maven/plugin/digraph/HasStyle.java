package net.kemitix.dependency.digraph.maven.plugin.digraph;

/**
 * Interface for elements that have a style.
 *
 * @author pcampbell
 */
public interface HasStyle {

    /**
     * Sets the style.
     *
     * @param style the style
     */
    void setStyle(String style);

    /**
     * Returns the style.
     *
     * @return the style
     */
    String getStyle();

}
