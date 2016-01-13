package net.kemitix.dependency.digraph.maven.plugin;

/**
 * Interface for Mojo services.
 *
 * @author pcampbell
 */
interface MojoService {

    /**
     * Gets the mojo.
     *
     * @return the mojo
     */
    DigraphMojo getMojo();

}
