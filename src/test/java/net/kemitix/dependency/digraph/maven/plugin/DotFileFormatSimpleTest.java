package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import net.kemitix.node.Node;

/**
 * Tests for {@link DotFileFormatSimple}.
 *
 * @author pcampbell
 */
public class DotFileFormatSimpleTest {

    /**
     * Class under test.
     */
    private DotFileFormat dotFileFormat;

    private DependencyData dependencyData;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dependencyData = DigraphFactory.newDependencyData("test");
        dotFileFormat = new DotFileFormatSimple(
                dependencyData.getBaseNode(), new DefaultNodePathGenerator());
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
        final String expected = "digraph{\n"
                + "compound=\"true\"\n"
                + "node[shape=\"box\"]\n"
                + "\"nested\"\n"
                + "\"other\"\n"
                + "\"nested\" -> \"other\""
                + "}\n";
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
        final String expected = "digraph{\n"
                + "compound=\"true\"\n"
                + "node[shape=\"box\"]\n"
                + "\"nested\""
                + "}\n";
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
        final String expected = "digraph{\n"
                + "compound=\"true\"\n"
                + "node[shape=\"box\"]\n"
                + "\"other\""
                + "}\n";
        //when
        String report = dotFileFormat.renderReport();
        //then
        assertThat(report, is(expected));
    }

    /**
     * Test that nested packages are included.
     */
    @Test
    public void shouldHandleNestedPackages() {
        //given
        dependencyData.addDependency("test.nested", "test.other");
        dependencyData.addDependency("test.nested", "test.other.more");
        dependencyData.addDependency("test.other", "test.yetmore");
        final String expected = "digraph{\n"
                + "compound=\"true\"\n"
                + "node[shape=\"box\"]\n"
                + "\"nested\"\n"
                + "\"other\"\n"
                + "\"other.more\"\n"
                + "\"yetmore\"\n"
                + "\"nested\" -> \"other.more\"\n"
                + "\"nested\" -> \"other\"\n"
                + "\"other\" -> \"yetmore\""
                + "}\n";
        //when
        String report = dotFileFormat.renderReport();
        //then
        assertThat(report, is(expected));
    }

}
