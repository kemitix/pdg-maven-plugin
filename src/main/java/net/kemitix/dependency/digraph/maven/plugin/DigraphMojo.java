package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Says "Hi" to the user.
 */
@Mojo(name = "sayhi")
public class DigraphMojo extends AbstractMojo {

    @Override
    public void execute() {
        getLog().info("Hello, world.");
    }

}
