package net.kemitix.pdg.maven.stubs;

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
