package net.kemitix.dependency.digraph.maven.plugin.stubs;

/**
 * Project list for the source and test project.
 *
 * @author pcampbell
 */
public class DigraphSrcOnlyProjectList extends AbstractProjectsList {

    /**
     * Default constructor.
     */
    public DigraphSrcOnlyProjectList() {
        super(new DigraphProjectStub("/src/test/projects/src-only"));
    }

}
