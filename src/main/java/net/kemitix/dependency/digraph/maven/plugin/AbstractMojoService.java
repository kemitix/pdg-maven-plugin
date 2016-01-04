package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract Mojo Service.
 *
 * <p>
 * Services to be injected should extend this class, thus allowing them to be
 * given access to the Mojo.
 *
 * @author pcampbell
 */
public abstract class AbstractMojoService implements MojoService {

    @Setter
    @Getter
    private DigraphMojo mojo;

}
