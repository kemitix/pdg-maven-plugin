package net.kemitix.pdg.maven.scan;

import net.kemitix.node.Node;
import net.kemitix.pdg.maven.DependencyData;
import net.kemitix.pdg.maven.digraph.PackageData;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

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
        assertThat(baseNode.getData()
                           .getName()).isEqualTo("kemitix");
    }

    @Test
    public void shouldCreateDescendantNodes() {
        //given
        final Node<PackageData> baseNode = data.getBaseNode();
        //when
        data.addDependency("net.kemitix.alpha", "net.kemitix.beta");
        //then
        assertThat(baseNode.findChild(PackageData.newInstance("alpha"))).isNotEmpty();
        assertThat(baseNode.findChild(PackageData.newInstance("beta"))).isNotEmpty();
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
        then(log).should()
                 .info("kemitix");
        then(log).should()
                 .info("  alpha");
        then(log).should()
                 .info("  beta");
    }
}
