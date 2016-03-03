package net.kemitix.dependency.digraph.maven.plugin.digraph;

/**
 * Interface for elements that have a label.
 *
 * @author pcampbell
 */
public interface HasLabel {

    /**
     * Sets the label.
     *
     * @param label the label
     */
    void setLabel(String label);

    /**
     * Returns the label.
     *
     * @return the label
     */
    String getLabel();

}
