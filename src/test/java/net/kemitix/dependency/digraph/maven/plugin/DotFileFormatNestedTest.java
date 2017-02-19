package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;
import net.kemitix.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link DotFileFormatNested}.
 *
 * @author pcampbell
 */
public class DotFileFormatNestedTest {

    private DotFileFormat dotFileFormat;

    private DependencyData dependencyData;

    private NodePathGenerator nodePathGenerator;

    @Mock
    private GraphFilter graphFilter;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dependencyData = DigraphFactory.newDependencyData("test");
        nodePathGenerator = new DefaultNodePathGenerator();
        dotFileFormat = new DotFileFormatNested(dependencyData.getBaseNode(), nodePathGenerator, graphFilter);
        /// default behaviour without any filters is to include
        given(graphFilter.filterNodes(any())).willReturn(true);
    }

    /**
     * Test that the intermediary node "test" is created.
     */
    @Test
    public void shouldCreateTestNode() {
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
    public void shouldGenerateReport() {
        //given
        dependencyData.addDependency("test.nested", "test.other");
        val expected =
                Arrays.asList("digraph{", "compound=\"true\"", "node[shape=\"box\"]", "subgraph \"cluster_test\"{",
                              "label=\"test\"", "\"_test\"[label=\"\",style=\"invis\",width=0]", "\"nested\"",
                              "\"other\"", "}", "\"nested\"->\"other\"}"
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
    public void shouldOnlyIncludeUsingPackage() {
        //given
        dependencyData.addDependency("test.nested", "tested.other");
        val expected =
                Arrays.asList("digraph{", "compound=\"true\"", "node[shape=\"box\"]", "subgraph \"cluster_test\"{",
                              "label=\"test\"", "\"_test\"[label=\"\",style=\"invis\",width=0]", "\"nested\"", "}}"
                             );
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
    public void shouldOnlyIncludeUsedPackage() {
        //given
        dependencyData.addDependency("tested.nested", "test.other");
        val expected =
                Arrays.asList("digraph{", "compound=\"true\"", "node[shape=\"box\"]", "subgraph \"cluster_test\"{",
                              "label=\"test\"", "\"_test\"[label=\"\",style=\"invis\",width=0]", "\"other\"", "}}"
                             );
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertThat(report).containsExactlyElementsOf(expected);
    }

    /**
     * Test that nested packages are included within their parent package
     * cluster.
     */
    @Test
    public void shouldNestPackages() {
        //given
        dependencyData.addDependency("test.nested", "test.other");
        dependencyData.addDependency("test.nested", "test.other.more");
        dependencyData.addDependency("test.other", "test.yetmore");
        val expected =
                Arrays.asList("digraph{", "compound=\"true\"", "node[shape=\"box\"]", "subgraph \"cluster_test\"{",
                              "label=\"test\"", "\"_test\"[label=\"\",style=\"invis\",width=0]", "\"nested\"",
                              "subgraph \"clusterother\"{", "label=\"other\"",
                              "\"other\"[label=\"\",style=\"invis\",width=0]", "\"other.more\"[label=\"more\"]", "}",
                              "\"yetmore\"", "}", "\"nested\"->\"other.more\"",
                              "\"nested\"->\"other\"[lhead=\"clusterother\"]",
                              "\"other\"->\"yetmore\"[ltail=\"clusterother\"]}"
                             );
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertThat(report).containsExactlyElementsOf(expected);
    }

    /**
     * The dummy package node within a package cluster should have the correct
     * id.
     */
    @Test
    public void shouldNestGrandChildParentDummyNode() {
        //given
        dependencyData.addDependency("test.one", "test.child.inter.leaf");
        val expected =
                Arrays.asList("digraph{", "compound=\"true\"", "node[shape=\"box\"]", "subgraph \"cluster_test\"{",
                              "label=\"test\"", "\"_test\"[label=\"\",style=\"invis\",width=0]",
                              "subgraph \"clusterchild\"{", "label=\"child\"",
                              "\"child\"[label=\"\",style=\"invis\",width=0]", "subgraph \"clusterchild_inter\"{",
                              "label=\"inter\"", "\"child_inter\"[label=\"\",style=\"invis\",width=0]",
                              "\"child.inter.leaf\"[label=\"leaf\"]", "}", "}", "\"one\"", "}",
                              "\"one\"->\"child.inter.leaf\"" + "}"
                             );
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertThat(report).containsExactlyElementsOf(expected);
    }

    /**
     * The 'lhead' suffix is not required when the tail is a child of the head.
     */
    @Test
    public void shouldNotIncludeLHeadWhenTailIsChildOfHead() {
        //given
        dependencyData.addDependency("test.one.two", "test.one");
        dotFileFormat = new DotFileFormatNested(dependencyData.getBaseNode(), nodePathGenerator, graphFilter);
        //when
        val report = dotFileFormat.renderReport();
        //then
        assertThat(report).contains("\"one.two\"->\"one\"")
                          .doesNotContain("lhead=\"clusterone\"");
    }

    /**
     * The 'ltailf' suffix is not required when the head is a child of the
     * tail.
     */
    @Test
    public void shouldNotIncludeLTailWhenHeadIsChildOfTailf() {
        //given
        dependencyData.addDependency("test.one", "test.one.two");
        dotFileFormat = new DotFileFormatNested(dependencyData.getBaseNode(), nodePathGenerator, graphFilter);
        //when
        val report = dotFileFormat.renderReport();
        //then
        assertThat(report).contains("\"one\"->\"one.two\"")
                          .doesNotContain("ltail=\"clusterone\"");
    }

    @Test
    public void shouldExcludePackage() {
        //given
        dependencyData.addDependency("test.one", "test.two");
        dependencyData.addDependency("test.one", "test.three");
        dependencyData.addDependency("test.three", "test.four");
        dependencyData.addDependency("test.one", "test.four");
        dotFileFormat = new DotFileFormatNested(dependencyData.getBaseNode(), nodePathGenerator,
                                                GraphFilter.of("three", "", nodePathGenerator)
        );
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertThat(report).doesNotContain("three")
                          .contains("\"one\"")
                          .contains("\"two\"")
                          .contains("\"four\"");
    }

    @Test
    public void shouldIncludePackage() {
        //given
        dependencyData.addDependency("test.one", "test.two");
        dependencyData.addDependency("test.one", "test.three");
        dependencyData.addDependency("test.three", "test.four");
        dependencyData.addDependency("test.one", "test.four");
        dotFileFormat = new DotFileFormatNested(dependencyData.getBaseNode(), nodePathGenerator,
                                                GraphFilter.of("", "three", nodePathGenerator)
        );
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertThat(report).doesNotContain("\"one\"->\"four\"")
                          .doesNotContain("\"one\"->\"two\"")
                          .doesNotContain("two")
                          .doesNotContain("four")
                          .contains("\"one\"")
                          .contains("\"three\"")
                          .contains("\"four\"")
                          .contains("\"one\"->\"three\"")
                          .contains("\"three\"->\"four\"}");
    }
}
