package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import net.kemitix.node.Node;

/**
 * Tests for {@link NodeTreeDependencyData}.
 *
 * @author pcampbell
 */
public class NodeTreeDependencyDataTest {

    /**
     * Class under test.
     */
    private DependencyData data;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        data = DigraphFactory.newDependencyData("net.kemitix");
    }

    /**
     * Set and return the base node.
     */
    @Test
    public void shouldReturnTheSetBaseNode() {
        //when
        Node<PackageData> baseNode = data.getBaseNode();
        //then
        assertThat(baseNode.getData().get().getName(), is("kemitix"));
    }

    /**
     * Adding dependency creates descendant nodes.
     */
    @Test
    public void shouldCreateDescendantNodes() {
        //given
        final Node<PackageData> baseNode = data.getBaseNode();
        //when
        data.addDependency("net.kemitix.alpha", "net.kemitix.beta");
        //then
        assertThat(baseNode.findChild(PackageData.newInstance("alpha"))
                           .isPresent(), is(true));
        assertThat(baseNode.findChild(PackageData.newInstance("beta"))
                           .isPresent(), is(true));
    }

    /**
     * Produce debug log.
     */
    @Test
    public void shouldLogDebugTree() {
        //given
        data.addDependency("net.kemitix.alpha", "net.kemitix.beta");
        Log log = mock(Log.class);
        //when
        data.debugLog(log);
        //then
        verify(log, times(1)).info("kemitix");
        verify(log, times(1)).info("  alpha");
        verify(log, times(1)).info("  beta");
    }

}
