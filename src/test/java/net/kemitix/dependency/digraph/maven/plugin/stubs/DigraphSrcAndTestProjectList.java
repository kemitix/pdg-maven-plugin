package net.kemitix.dependency.digraph.maven.plugin.stubs;

/**
 * Project list for the source and test project.
 *
 * @author pcampbell
 */
public class DigraphSrcAndTestProjectList extends AbstractProjectsList {

    /**
     * Default constructor.
     */
    public DigraphSrcAndTestProjectList() {
        super(new DigraphProjectStub("/src/test/projects/src-and-test"));
    }

}
