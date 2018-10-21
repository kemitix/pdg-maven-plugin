package net.kemitix.pdg.maven;

import lombok.val;
import net.kemitix.node.Node;
import net.kemitix.pdg.maven.scan.DigraphFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DotFileFormatNested}.
 *
 * @author pcampbell
 */
public class DotFileFormatNestedTest {

    private DotFileFormat dotFileFormat;

    private DependencyData dependencyData;

    private NodePathGenerator nodePathGenerator;

    private final GraphFilter graphFilter = mock(GraphFilter.class);
    private final List<String> expected = new ArrayList<>();

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
        expectedHeader();
        line("subgraph \"cluster_test\"{");
        line("label=\"test\"");
        line("\"_test\"[label=\"\",style=\"invis\",width=0]");
        line("\"nested\"");
        line("\"other\"");
        line("}");
        line("\"nested\"->\"other\"}");
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
        expectedHeader();
        line("subgraph \"cluster_test\"{");
        line("label=\"test\"");
        line("\"_test\"[label=\"\",style=\"invis\",width=0]");
        line("\"nested\"");
        line("}}");
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
        expectedHeader();
        line("subgraph \"cluster_test\"{");
        line("label=\"test\"");
        line("\"_test\"[label=\"\",style=\"invis\",width=0]");
        line("\"other\"");
        line("}}");
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
        expectedHeader();
        line("subgraph \"cluster_test\"{");
        line("label=\"test\"");
        line("\"_test\"[label=\"\",style=\"invis\",width=0]");
        line("\"nested\"");
        line("subgraph \"clusterother\"{");
        line("label=\"other\"");
        line("\"other\"[label=\"\",style=\"invis\",width=0]");
        line("\"other.more\"[label=\"more\"]");
        line("}");
        line("\"yetmore\"");
        line("}");
        line("\"nested\"->\"other.more\"");
        line("\"nested\"->\"other\"[lhead=\"clusterother\"]");
        line("\"other\"->\"yetmore\"[ltail=\"clusterother\"]}");
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
        expectedHeader();
        line("subgraph \"cluster_test\"{");
        line("label=\"test\"");
        line("\"_test\"[label=\"\",style=\"invis\",width=0]");
        line("subgraph \"clusterchild\"{");
        line("label=\"child\"");
        line("\"child\"[label=\"\",style=\"invis\",width=0]");
        line("subgraph \"clusterchild_inter\"{");
        line("label=\"inter\"");
        line("\"child_inter\"[label=\"\",style=\"invis\",width=0]");
        line("\"child.inter.leaf\"[label=\"leaf\"]");
        line("}");
        line("}");
        line("\"one\"");
        line("}");
        line("\"one\"->\"child.inter.leaf\"" + "}");
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
        final DigraphMojo digraphMojo = new DigraphMojo();
        digraphMojo.setInclude("");
        digraphMojo.setExclude("three");
        val graphFilter = new DefaultGraphFilter(digraphMojo, nodePathGenerator);
        dotFileFormat = new DotFileFormatNested(
                new DefaultTreeFilter(graphFilter, nodePathGenerator).filterTree(dependencyData.getBaseNode()),
                nodePathGenerator, graphFilter
        );
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertSoftly(softly -> {
            softly.assertThat(report).as("exclude prohibited node").doesNotContain("\"three\"");
            softly.assertThat(report).as("exclude usage of prohibited node").doesNotContain("\"one\"->\"three\"");
            softly.assertThat(report).as("exclude usage by prohibited node").doesNotContain("\"three\"->\"four\"}");
            softly.assertThat(report).as("valid nodes: one").contains("\"one\"");
            softly.assertThat(report).as("valid nodes: two").contains("\"two\"");
            softly.assertThat(report).as("valid nodes: three").contains("\"four\"");
            softly.assertThat(report).as("include valid usages: one -> four").contains("\"one\"->\"four\"");
            softly.assertThat(report).as("include valid usages: one -> two").contains("\"one\"->\"two\"}");
        });
    }

    @Test
    public void shouldIncludePackage() {
        //given
        dependencyData.addDependency("test.one", "test.two");
        dependencyData.addDependency("test.one", "test.three");
        dependencyData.addDependency("test.three", "test.four");
        dependencyData.addDependency("test.one", "test.four");
        final DigraphMojo digraphMojo = new DigraphMojo();
        digraphMojo.setInclude("three");
        digraphMojo.setExclude("");
        val graphFilter = new DefaultGraphFilter(digraphMojo, nodePathGenerator);
        dotFileFormat = new DotFileFormatNested(
                new DefaultTreeFilter(graphFilter, nodePathGenerator).filterTree(dependencyData.getBaseNode()),
                nodePathGenerator, graphFilter
        );
        //when
        val report = dotFileFormat.renderReport()
                                  .split(System.lineSeparator());
        //then
        assertSoftly(softly -> {
            assertThat(report).as("don't include unrelated node").doesNotContain("\"two\"");
            softly.assertThat(report).as("don't include uses by or of unrelated node2")
                  .doesNotContain("\"one\"->\"four\"")
                  .doesNotContain("\"one\"->\"two\"");
            softly.assertThat(report).as("include required node").contains("\"three\"");
            softly.assertThat(report).as("include directly related nodes")
                  .contains("\"one\"")
                  .contains("\"four\"");
            softly.assertThat(report).as("include use of required node").contains("\"one\"->\"three\"");
            softly.assertThat(report).as("include use by required node").contains("\"three\"->\"four\"}");
        });
    }


    private void expectedHeader() {
        line("digraph{");
        line("compound=\"true\"");
        line("node[shape=\"box\"]");
    }

    private void line(final String line) {
        expected.add(line);
    }
}
