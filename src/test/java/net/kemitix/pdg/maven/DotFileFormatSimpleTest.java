package net.kemitix.pdg.maven;

import lombok.val;
import net.kemitix.node.Node;
import net.kemitix.pdg.maven.digraph.DotFileFormat;
import net.kemitix.pdg.maven.digraph.PackageData;
import net.kemitix.pdg.maven.scan.DigraphFactory;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DotFileFormatSimpleTest implements WithAssertions {

    private final GraphFilter graphFilter = mock(GraphFilter.class);
    private final DependencyData dependencyData = DigraphFactory.newDependencyData("test");
    private final DotFileFormat dotFileFormat =
            new DotFileFormatSimple(dependencyData.getBaseNode(), new DefaultNodePathGenerator(), graphFilter);

    @BeforeEach
    void setUp() {
        /// default behaviour without any filters is to include
        given(graphFilter.filterNodes(any())).willReturn(true);
    }

    /**
     * Test that the intermediary node "test" is created.
     */
    @Test
    void shouldCreateTestNode() {
        //given
        dependencyData.addDependency("test.nested", "test.other");
        //when
        Node<PackageData> baseNode = dependencyData.getBaseNode();
        //then
        assertThat(baseNode).isNotNull();
        assertThat(baseNode.getData()
                           .getName()).isEqualTo("test");
    }

    /**
     * Test that the report is created as expected.
     */
    @Test
    void shouldGenerateReport() {
        //given
        dependencyData.addDependency("test.nested", "test.other");
        val expected = Arrays.asList("digraph{", "compound=\"true\"", "node[shape=\"box\"]", "\"nested\"", "\"other\"",
                                     "\"nested\" -> \"other\"}"
                                    );
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertThat(report).containsExactlyElementsOf(expected);
    }

    /**
     * Test that the report only includes expected package when used package is
     * outside base package.
     */
    @Test
    void shouldOnlyIncludeUsingPackage() {
        //given
        dependencyData.addDependency("test.nested", "tested.other");
        val expected = Arrays.asList("digraph{", "compound=\"true\"", "node[shape=\"box\"]", "\"nested\"}");
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertThat(report).containsExactlyElementsOf(expected);
    }

    /**
     * Test that the report only includes expected package when using package is
     * outside base package.
     */
    @Test
    void shouldOnlyIncludeUsedPackage() {
        //given
        dependencyData.addDependency("tested.nested", "test.other");
        val expected = Arrays.asList("digraph{", "compound=\"true\"", "node[shape=\"box\"]", "\"other\"}");
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertThat(report).containsExactlyElementsOf(expected);
    }

    /**
     * Test that nested packages are included.
     */
    @Test
    void shouldHandleNestedPackages() {
        //given
        dependencyData.addDependency("test.nested", "test.other");
        dependencyData.addDependency("test.nested", "test.other.more");
        dependencyData.addDependency("test.other", "test.yetmore");
        val expected = Arrays.asList("digraph{", "compound=\"true\"", "node[shape=\"box\"]", "\"nested\"", "\"other\"",
                                     "\"other.more\"", "\"yetmore\"", "\"nested\" -> \"other.more\"",
                                     "\"nested\" -> \"other\"", "\"other\" -> \"yetmore\"}"
                                    );
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertThat(report).containsExactlyElementsOf(expected);
    }
}
