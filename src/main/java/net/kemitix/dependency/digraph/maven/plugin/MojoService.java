package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;

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

    /**
     * Get the logger.
     *
     * @return the logger
     */
    Log getLog();

}
