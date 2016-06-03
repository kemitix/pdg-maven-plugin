package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import net.kemitix.node.Node;

/**
 * Tests for {@link DotFileFormatNested}.
 *
 * @author pcampbell
 */
public class DotFileFormatNestedTest {

    /**
     * Class under test.
     */
    private DotFileFormat dotFileFormat;

    private DependencyData dependencyData;

    private NodePathGenerator nodePathGenerator
            = new DefaultNodePathGenerator();

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dependencyData = new NodeTreeDependencyData();
        dependencyData.setBasePackage("test");
        dotFileFormat = new DotFileFormatNested(dependencyData.getBaseNode(),
                nodePathGenerator);
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
        assertThat(baseNode, is(not(nullValue())));
        assertThat(baseNode.getData().get().getName(), is("test"));
    }

    /**
     * Test that the report is created as expected.
     */
    @Test
    public void shouldGenerateReport() {
        //given
        dependencyData.addDependency("test.nested", "test.other");
        final String expected = "digraph{\n" + "compound=\"true\"\n"
                + "node[shape=\"box\"]\n" + "subgraph \"cluster_test\"{\n"
                + "label=\"test\"\n"
                + "\"_test\"[label=\"\",style=\"invis\",width=0]\n"
                + "\"nested\"\n"
                + "\"other\"\n" + "}\n"
                + "\"nested\"->\"other\"" + "}\n";
        //when
        String report = dotFileFormat.renderReport();
        //then
        assertThat(report, is(expected));
    }

    /**
     * Test that the report only includes expected package when used package is
     * outside base package.
     */
    @Test
    public void shouldOnlyIncludeUsingPackage() {
        //given
        dependencyData.addDependency("test.nested", "tested.other");
        final String expected = "digraph{\n" + "compound=\"true\"\n"
                + "node[shape=\"box\"]\n" + "subgraph \"cluster_test\"{\n"
                + "label=\"test\"\n"
                + "\"_test\"[label=\"\",style=\"invis\",width=0]\n"
                + "\"nested\"\n" + "}}\n";
        //when
        String report = dotFileFormat.renderReport();
        //then
        assertThat(report, is(expected));
    }

    /**
     * Test that the report only includes expected package when using package is
     * outside base package.
     */
    @Test
    public void shouldOnlyIncludeUsedPackage() {
        //given
        dependencyData.addDependency("tested.nested", "test.other");
        final String expected = "digraph{\n" + "compound=\"true\"\n"
                + "node[shape=\"box\"]\n" + "subgraph \"cluster_test\"{\n"
                + "label=\"test\"\n"
                + "\"_test\"[label=\"\",style=\"invis\",width=0]\n"
                + "\"other\"\n" + "}}\n";
        //when
        String report = dotFileFormat.renderReport();
        //then
        assertThat(report, is(expected));
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
        final String expected = "digraph{\n" + "compound=\"true\"\n"
                + "node[shape=\"box\"]\n" + "subgraph \"cluster_test\"{\n"
                + "label=\"test\"\n"
                + "\"_test\"[label=\"\",style=\"invis\",width=0]\n"
                + "\"nested\"\n"
                + "subgraph \"clusterother\"{\n" + "label=\"other\"\n"
                + "\"other\"[label=\"\",style=\"invis\",width=0]\n"
                + "\"other.more\"[label=\"more\"]\n" + "}\n"
                + "\"yetmore\"\n" + "}\n"
                + "\"nested\"->\"other.more\"\n"
                + "\"nested\"->\"other\"[lhead=\"clusterother\"]\n"
                + "\"other\"->\"yetmore\"[ltail=\"clusterother\"]" + "}\n";
        //when
        String report = dotFileFormat.renderReport();
        //then
        assertThat(report, is(expected));
    }

    /**
     * The dummy package node within a package cluster should have the correct
     * id.
     */
    @Test
    public void shouldNestGrandChildParentDummyNode() {
        //given
        dependencyData.addDependency("test.one", "test.child.inter.leaf");
        final String expected = "digraph{\n" + "compound=\"true\"\n"
                + "node[shape=\"box\"]\n" + "subgraph \"cluster_test\"{\n"
                + "label=\"test\"\n"
                + "\"_test\"[label=\"\",style=\"invis\",width=0]\n"
                + "subgraph \"clusterchild\"{\n" + "label=\"child\"\n"
                + "\"child\"[label=\"\",style=\"invis\",width=0]\n"
                + "subgraph \"clusterchild_inter\"{\n" + "label=\"inter\"\n"
                + "\"child_inter\"[label=\"\",style=\"invis\",width=0]\n"
                + "\"child.inter.leaf\"[label=\"leaf\"]\n" + "}\n" + "}\n"
                + "\"one\"\n" + "}\n" + "\"one\"->\"child.inter.leaf\"" + "}\n";
        //when
        String report = dotFileFormat.renderReport();
        //then
        assertThat(report, is(expected));
    }

    /**
     * The 'lhead' suffix is not required when the tail is a child of the head.
     */
    @Test
    public void shouldNotIncludeLHeadWhenTailIsChildOfHead() {
        //given
        dependencyData.addDependency("test.one.two", "test.one");
        dotFileFormat = new DotFileFormatNested(dependencyData.getBaseNode(),
                nodePathGenerator);
        //when
        String report = dotFileFormat.renderReport();
        //then
        assertThat(report, containsString("\n\"one.two\"->\"one\""));
        assertThat(report, not(containsString("lhead=\"clusterone\"")));
    }

    /**
     * The 'ltailf' suffix is not required when the head is a child of the
     * tail.
     */
    @Test
    public void shouldNotIncludeLTailWhenHeadIsChildOfTailf() {
        //given
        dependencyData.addDependency("test.one", "test.one.two");
        dotFileFormat = new DotFileFormatNested(dependencyData.getBaseNode(),
                nodePathGenerator);
        //when
        String report = dotFileFormat.renderReport();
        //then
        assertThat(report, containsString("\n\"one\"->\"one.two\""));
        assertThat(report, not(containsString("ltail=\"clusterone\"")));
    }

}
