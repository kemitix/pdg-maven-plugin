package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import net.kemitix.node.Node;
import net.kemitix.node.Nodes;

/**
 * Tests for {@link DefaultNodePathGenerator}.
 *
 * @author Paul Campbell
 */
public class DefaultNodePathGeneratorTest {

    /**
     * Class under test.
     */
    private NodePathGenerator generator;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        generator = new DefaultNodePathGenerator();
    }

    /**
     * Test path for root node.
     */
    @Test
    public void testGetPathForRoot() {
        //given
        final PackageData rootData = PackageData.newInstance("root");
        final Node<PackageData> root = Nodes.unnamedRoot(rootData);
        //when
        final String result = generator.getPath(root, root, ".");
        //then
        assertThat(result, is(""));
    }

    /**
     * Test path for child of root.
     */
    @Test
    public void testGetPathForChild() {
        //given
        // root
        final PackageData rootData = PackageData.newInstance("root");
        final Node<PackageData> root = Nodes.unnamedRoot(rootData);
        // child
        final String childName = "child";
        final PackageData childData = PackageData.newInstance(childName);
        final Node<PackageData> child = Nodes.unnamedChild(childData, root);
        //when
        final String result = generator.getPath(child, root, ".");
        //then
        assertThat(result, is(childName));
    }

    /**
     * Test path for grandchild of root.
     */
    @Test
    public void testGetPathForGrandchild() {
        //given
        // root
        final PackageData rootData = PackageData.newInstance("root");
        final Node<PackageData> root = Nodes.unnamedRoot(rootData);
        // child
        final String childName = "child";
        final PackageData childData = PackageData.newInstance(childName);
        final Node<PackageData> child = Nodes.unnamedChild(childData, root);
        // grandchild
        final String grandchildName = "grandchild";
        final PackageData grandchildData = PackageData.newInstance(grandchildName);
        final Node<PackageData> grandchild = Nodes.unnamedChild(grandchildData,
                child);
        //when
        final String result = generator.getPath(grandchild, root, ".");
        //then
        assertThat(result, is(childName + "." + grandchildName));
    }

}
