package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.then;

import net.kemitix.node.Node;

/**
 * Tests for {@link NodeTreeDependencyData}.
 *
 * @author pcampbell
 */
public class NodeTreeDependencyDataTest {

    private DependencyData data;

    @Mock
    private Log log;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        data = DigraphFactory.newDependencyData("net.kemitix");
    }

    @Test
    public void shouldReturnTheSetBaseNode() {
        //when
        Node<PackageData> baseNode = data.getBaseNode();
        //then
        assertThat(baseNode.getData().get().getName(), is("kemitix"));
    }

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
        //when
        data.debugLog(log);
        //then
        then(log).should().info("kemitix");
        then(log).should().info("  alpha");
        then(log).should().info("  beta");
    }
}
