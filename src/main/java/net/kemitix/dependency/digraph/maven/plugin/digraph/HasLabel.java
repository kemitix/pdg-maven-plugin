package net.kemitix.dependency.digraph.maven.plugin.digraph;

/**
 * Interface for elements that have a label.
 *
 * @author pcampbell
 */
public interface HasLabel {

    /**
     * Returns the label.
     *
     * @return the label
     */
    String getLabel();

    /**
     * Sets the label.
     *
     * @param label the label
     */
    void setLabel(String label);

}
