package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

/**
 * Tests for {@link DotFileReportGenerator}.
 *
 * @author pcampbell
 */
public class DotFileReportGeneratorTest {

    /**
     * Class under test.
     */
    private DotFileReportGenerator reportGenerator;

    private DependencyData dependencyData;

    @Mock
    private NodePathGenerator nodePathGenerator;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dependencyData = new NodeTreeDependencyData();
        reportGenerator = new DotFileReportGenerator(nodePathGenerator);
    }

    /**
     * Test that the intermediary node "test" is created.
     */
    @Test
    public void shouldCreateTestNode() {
        //given
        dependencyData.setBasePackage("test");
        dependencyData.addDependency("test.nested", "test.other");
        //when
        Node<PackageData> baseNode = dependencyData.getBaseNode();
        //then
        assertThat(baseNode, is(not(nullValue())));
        assertThat(baseNode.getData().getName(), is("test"));
    }

    /**
     * Test that the report is created as expected.
     */
    @Test
    public void shouldGenerateReport() {
        //given
        dependencyData.setBasePackage("test");
        dependencyData.addDependency("test.nested", "test.other");
        final Node<PackageData> baseNode = dependencyData.getBaseNode();

        doReturn("test").when(nodePathGenerator)
                .getPath(eq(baseNode), eq(baseNode), any(String.class));

        Node<PackageData> nestedNode = getChildNodeByName(baseNode, "nested");
        doReturn("nested").when(nodePathGenerator)
                .getPath(eq(nestedNode), eq(baseNode), any(String.class));

        Node<PackageData> otherNode = getChildNodeByName(baseNode, "other");
        doReturn("other").when(nodePathGenerator)
                .getPath(eq(otherNode), eq(baseNode), any(String.class));

        final String expected = "digraph{compound=true;node[shape=box]\n"
                + "subgraph \"clustertest\"{"
                + "label=\"test\";\"test\"[style=dotted]\n"
                + "\"nested\"[label=\"nested\"];\"other\"[label=\"other\"];}\n"
                + "\"nested\"->\"other\"\n"
                + "}";
        //when
        String report = reportGenerator.generate(baseNode);
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
        dependencyData.setBasePackage("test");
        dependencyData.addDependency("test.nested", "tested.other");
        final Node<PackageData> baseNode = dependencyData.getBaseNode();

        doReturn("test").when(nodePathGenerator)
                .getPath(eq(baseNode), eq(baseNode), any(String.class));

        Node<PackageData> nestedNode = getChildNodeByName(baseNode, "nested");
        doReturn("nested").when(nodePathGenerator)
                .getPath(eq(nestedNode), eq(baseNode), any(String.class));

        final String expected = "digraph{compound=true;node[shape=box]\n"
                + "subgraph \"clustertest\"{"
                + "label=\"test\";\"test\"[style=dotted]\n"
                + "\"nested\"[label=\"nested\"];}\n"
                + "}";
        //when
        String report = reportGenerator.generate(baseNode);
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
        dependencyData.setBasePackage("test");
        dependencyData.addDependency("tested.nested", "test.other");
        final Node<PackageData> baseNode = dependencyData.getBaseNode();

        doReturn("test").when(nodePathGenerator)
                .getPath(eq(baseNode), eq(baseNode), any(String.class));

        Node<PackageData> otherNode = getChildNodeByName(baseNode, "other");
        doReturn("other").when(nodePathGenerator)
                .getPath(eq(otherNode), eq(baseNode), any(String.class));

        final String expected = "digraph{compound=true;node[shape=box]\n"
                + "subgraph \"clustertest\"{"
                + "label=\"test\";\"test\"[style=dotted]\n"
                + "\"other\"[label=\"other\"];}\n"
                + "}";
        //when
        String report = reportGenerator.generate(baseNode);
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
        dependencyData.setBasePackage("test");
        dependencyData.addDependency("test.nested", "test.other");
        dependencyData.addDependency("test.nested", "test.other.more");
        dependencyData.addDependency("test.other", "test.yetmore");
        final Node<PackageData> baseNode = dependencyData.getBaseNode();

        doReturn("test").when(nodePathGenerator)
                .getPath(eq(baseNode), eq(baseNode), any(String.class));

        Node<PackageData> nestedNode = getChildNodeByName(baseNode, "nested");
        doReturn("nested").when(nodePathGenerator)
                .getPath(eq(nestedNode), eq(baseNode), any(String.class));

        Node<PackageData> yetmoreNode = getChildNodeByName(baseNode, "yetmore");
        doReturn("yetmore").when(nodePathGenerator)
                .getPath(eq(yetmoreNode), eq(baseNode), any(String.class));

        Node<PackageData> otherNode = getChildNodeByName(baseNode, "other");
        doReturn("other").when(nodePathGenerator)
                .getPath(eq(otherNode), eq(baseNode), any(String.class));

        Node<PackageData> moreNode = getChildNodeByName(otherNode, "more");
        doReturn("other.more").when(nodePathGenerator)
                .getPath(eq(moreNode), eq(baseNode), any(String.class));

        final String expected = "digraph{compound=true;node[shape=box]\n"
                + "subgraph \"clustertest\"{"
                + "label=\"test\";\"test\"[style=dotted]\n"
                + "\"nested\"[label=\"nested\"];"
                + "subgraph \"clusterother\"{"
                + "label=\"other\";\"other\"[style=dotted]\n"
                + "\"other.more\"[label=\"more\"];}\n"
                + "\"yetmore\"[label=\"yetmore\"];"
                + "}\n"
                + "\"nested\"->\"other.more\"\n"
                + "\"nested\"->\"other\"[lhead=\"clusterother\",]\n"
                + "\"other\"->\"yetmore\"[ltail=\"clusterother\",]\n"
                + "}";
        //when
        String report = reportGenerator.generate(baseNode);
        //then
        assertThat(report, is(expected));
    }

    private Node<PackageData> getChildNodeByName(
            final Node<PackageData> baseNode,
            final String name) {
        final Optional<Node<PackageData>> nestedOptional
                = baseNode.getChild(new PackageData(name));
        if (!nestedOptional.isPresent()) {
            fail("Child node not found");
        }
        return nestedOptional.get();
    }
}
