package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;
import org.apache.maven.plugin.logging.Log;

import javax.inject.Inject;

/**
 * Abstract Mojo Service.
 *
 * <p>
 * Services to be injected should extend this class, thus allowing them to be
 * given access to the Mojo.
 *
 * @author pcampbell
 */
abstract class AbstractMojoService implements MojoService {

    @Inject
    @Getter
    private DigraphMojo mojo;

    @Override
    public Log getLog() {
        return mojo.getLog();
    }

}
