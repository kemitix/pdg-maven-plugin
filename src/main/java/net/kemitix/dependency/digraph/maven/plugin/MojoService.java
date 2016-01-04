package net.kemitix.dependency.digraph.maven.plugin;

/**
 * Interface for Mojo services.
 *
 * @author pcampbell
 */
public interface MojoService {

    /**
     * Sets the mojo.
     *
     * @param mojo the mojo
     */
    void setMojo(DigraphMojo mojo);

    /**
     * Gets the mojo.
     *
     * @return the mojo
     */
    DigraphMojo getMojo();

}
