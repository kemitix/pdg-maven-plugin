package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link NodeTreeDependencyData}.
 *
 * @author pcampbell
 */
public class NodeTreeDependencyDataTest {

    /**
     * Class under test.
     */
    private NodeTreeDependencyData data;

    @Mock
    private Log log;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        data = new NodeTreeDependencyData();
    }

    /**
     * Adding a dependency and getting dump output.
     */
    @Test
    public void shouldDumpExpectedDependencies() {
        //given
        String userPackage = "user package";
        String usedPackage = "used package";
        data.addDependency(userPackage, usedPackage);
        //when
        data.dumpDependencies(log);
        //then
        verify(log, times(1)).info("Packages:");
        verify(log, times(1)).info("* user package => used package");
    }

}
