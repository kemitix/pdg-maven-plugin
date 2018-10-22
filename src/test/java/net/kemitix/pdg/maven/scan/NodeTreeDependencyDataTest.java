package net.kemitix.pdg.maven.scan;

import net.kemitix.node.Node;
import net.kemitix.pdg.maven.DependencyData;
import net.kemitix.pdg.maven.digraph.PackageData;
import org.apache.maven.plugin.logging.Log;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class NodeTreeDependencyDataTest implements WithAssertions {

    private final Log log = mock(Log.class);
    private final DependencyData data = DigraphFactory.newDependencyData("net.kemitix");

    @Test
    void shouldReturnTheSetBaseNode() {
        //when
        final Node<PackageData> baseNode = data.getBaseNode();
        //then
        assertThat(baseNode.getData()
                           .getName()).isEqualTo("kemitix");
    }

    @Test
    void shouldCreateDescendantNodes() {
        //given
        final Node<PackageData> baseNode = data.getBaseNode();
        //when
        data.addDependency("net.kemitix.alpha", "net.kemitix.beta");
        //then
        assertThat(baseNode.findChild(PackageData.newInstance("alpha"))).isNotEmpty();
        assertThat(baseNode.findChild(PackageData.newInstance("beta"))).isNotEmpty();
    }

    @Test
    @Disabled("no assert() or fail()")
    void shouldLogDebugTree() {
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
